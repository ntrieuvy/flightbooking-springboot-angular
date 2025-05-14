import { Component, OnInit } from '@angular/core';
import { FlightService } from 'src/app/core/services/flight.service';
import { FlightsResDTO } from 'src/app/core/models/interface/flights-res.dto';
import { FlightReqDTO } from 'src/app/core/models/interface/flights-req.dto';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-flight-container',
  templateUrl: './flight-container.component.html',
  styleUrls: ['./flight-container.component.scss'],
  providers: [DatePipe]
})
export class FlightContainerComponent implements OnInit {
  loading = true;
  error: string | null = null;
  flightResults: FlightsResDTO;
  filteredGroups: any[] = [];

  airlines: string[] = ['Vietnam Airlines', 'VietJet Air', 'Bamboo Airways'];
  selectedAirlines: string[] = [];
  sortBy: 'price' | 'duration' = 'price';
  minPrice: number | null = null;
  maxPrice: number | null = null;
  activeTabIndex = 0;

  showDetailModal: boolean = false;
  selectedFlight: any;
  selectedFare: any;
  selectedFlights: any[] = [];

  pageSize = 20;
  currentPage = 1;
  totalPages = 1;

  constructor(
    private flightService: FlightService,
    private datePipe: DatePipe,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const from = params['from'];
      const to = params['to'];
      const departureDate = params['departureDate'];
      const returnDate = params['returnDate'];
      const adults = parseInt(params['adults'] || '1', 10);
      const children = parseInt(params['children'] || '0', 10);

      if (from && to && departureDate) {
        const requestData: FlightReqDTO = {
          adults: adults,
          children: children,
          infant: 0,
          flights: [
            {
              start: from,
              end: to,
              date: this.formatDate(departureDate)
            },
            ...(returnDate ? [{
              start: to,
              end: from,
              date: this.formatDate(returnDate)
            }] : [])
          ]
        };

        console.log(requestData);

        this.handleSearch({ formData: params, requestData });
      }
    });
  }

  handleSearch(event: { formData: any; requestData: FlightReqDTO }) {
    this.loading = true;
    this.error = null;
    this.selectedAirlines = [];
    this.minPrice = null;
    this.maxPrice = null;
    this.flightResults = null;

    this.flightService.searchFlights(event.requestData).subscribe({
      next: (res: FlightsResDTO) => {
        this.flightResults = res;
        this.airlines = this.extractAirlines(res);
        this.applyFilters();
        this.setDefaultSelectedFare();
        this.activeTabIndex = 0;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.message || 'Failed to search flights';
        this.loading = false;
      }
    });
  }

  private extractAirlines(res: FlightsResDTO): string[] {
    const airlines = new Set<string>();
    res.data.ListGroup.forEach(group => {
      group.ListAirOption.forEach(option => {
        airlines.add(option.Airline);
      });
    });
    return Array.from(airlines);
  }

  applyFilters() {
    if (!this.flightResults?.data?.ListGroup) return;

    this.filteredGroups = this.flightResults.data.ListGroup
      .map(group => ({ ...group }))
      .filter(group => {
        group.ListAirOption = group.ListAirOption
          .filter(option => this.filterByAirline(option))
          .filter(option => this.filterByPrice(option))
          .sort((a, b) => this.sortOptions(a, b));
        return group.ListAirOption.length > 0;
      });

    this.updatePagination();
  }

  onSortChange(sortBy: 'price' | 'duration') {
    this.sortBy = sortBy;
    this.applyFilters();
  }

  toggleAirlineSelection(airline: string, checked: boolean): void {
    if (checked) {
      if (!this.selectedAirlines.includes(airline)) {
        this.selectedAirlines.push(airline);
      }
    } else {
      this.selectedAirlines = this.selectedAirlines.filter(a => a !== airline);
    }
    this.applyFilters();
  }

  onPriceChange(min: number | null, max: number | null) {
    this.minPrice = min;
    this.maxPrice = max;
    this.applyFilters();
  }

  private filterByAirline(option: any): boolean {
    return this.selectedAirlines.length === 0 ||
      this.selectedAirlines.includes(option.Airline);
  }

  private filterByPrice(option: any): boolean {
    if (!this.minPrice && !this.maxPrice) return true;

    return option.ListFareOption.some(fare => {
      const price = fare.TotalFare;
      return (!this.minPrice || price >= this.minPrice) &&
        (!this.maxPrice || price <= this.maxPrice);
    });
  }

  private sortOptions(a: any, b: any): number {
    if (this.sortBy === 'price') {
      const aPrice = Math.min(...a.ListFareOption.map(f => f.TotalFare));
      const bPrice = Math.min(...b.ListFareOption.map(f => f.TotalFare));
      return aPrice - bPrice;
    }

    const aDuration = a.ListFlightOption.reduce((sum, f) => sum + f.ListFlight[0].Duration, 0);
    const bDuration = b.ListFlightOption.reduce((sum, f) => sum + f.ListFlight[0].Duration, 0);
    return aDuration - bDuration;
  }

  onFareSelect(event: { option: any, value: string }) {
    const { option, value } = event;
    option.selectedFare = option.ListFareOption.find(f => f.FareBasis === value);

    const flightNumber = option.ListFlightOption[0].ListFlight[0].FlightNumber;
    const selectedFlight = this.selectedFlights.find(f =>
      f.ListFlightOption[0].ListFlight[0].FlightNumber === flightNumber
    );

    if (selectedFlight) {
      selectedFlight.selectedFare = option.selectedFare;
    }

    console.log('Fare selected and updated:', {
      flightNumber,
      fare: option.selectedFare,
      isBooked: !!selectedFlight
    });
  }

  bookFlight(option: any) {
    if (!option.selectedFare) {
      alert('Please select a fare first');
      return;
    }

    const existingIndex = this.selectedFlights.findIndex(f =>
      f.ListFlightOption[0].ListFlight[0].FlightNumber ===
      option.ListFlightOption[0].ListFlight[0].FlightNumber
    );

    if (existingIndex >= 0) {
      this.selectedFlights[existingIndex] = {
        ...option,
        selectedFare: option.selectedFare
      };
    } else {
      if (this.selectedFlights.length < 2) {
        this.selectedFlights.push({
          ...option,
          selectedFare: option.selectedFare
        });
      } else {
        alert('You can only select up to 2 flights');
      }
    }

    console.log('Current selected flights:', this.selectedFlights);
  }

  submitBooking() {
    const bookingData = this.selectedFlights.map(flight => ({
      session: this.flightResults.data.Session,
      airOptionId: flight.OptionId,
      flightOptionId: flight.ListFlightOption[0].OptionId,
      fareOptionId: flight.selectedFare.OptionId,
      flightNumber: flight.ListFlightOption[0].ListFlight[0].FlightNumber,
      airline: flight.Airline,
      departure: flight.ListFlightOption[0].ListFlight[0].StartDate,
      arrival: flight.ListFlightOption[0].ListFlight[0].EndDate,
      fare: {
        type: flight.selectedFare.FareBasis,
        price: flight.selectedFare.PriceAdt,
        currency: flight.selectedFare.Currency
      }
    }));

    console.log('Submitting booking:', bookingData);
    const formData = this.route.snapshot.queryParams;
    console.log('Form data:', formData);
    console.log("selected", this.selectedFlights)
    this.router.navigate(['/checkout'], {
      state: {
        bookingData,
        formData
      }
    });
    this.selectedFlights = [];
  }

  setDefaultSelectedFare() {
    if (!this.filteredGroups?.length) return;
    this.filteredGroups.forEach(group => {
      group.ListAirOption.forEach(option => {
        if (option.ListFareOption?.length > 0) {
          option.selectedFare = option.ListFareOption[0];
        }
      });
    });
  }

  getRemarkClass(remark: string): string {
    const remarkMap: { [key: string]: string } = {
      'Promo': 'promo',
      'Best Price': 'best-price',
      'Refundable': 'refundable'
    };
    return remarkMap[remark] || 'default';
  }

  private formatDate(dateString: string): string {
    return this.datePipe.transform(dateString, 'ddMMyyyy') || '';
  }

  formatTime(dateTimeString: string): string {
    return this.datePipe.transform(dateTimeString, 'HH:mm') || '';
  }

  formatDuration(minutes: number): string {
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return `${hours}h ${mins}m`;
  }

  getAirlineName(code: string): string {
    const airlines: { [key: string]: string } = {
      'VN': 'Vietnam Airlines',
      'VJ': 'VietJet Air',
      'QH': 'Bamboo Airways',
      'BL': 'Pacific Airlines'
    };
    return airlines[code] || code;
  }

  openFlightDetailModal(flight: any) {
    this.selectedFlight = flight;
    this.showDetailModal = true;
  }

  closeDetailModal() {
    this.showDetailModal = false;
  }

  getPaginatedOptions() {
    const currentGroup = this.filteredGroups[this.activeTabIndex];
    if (!currentGroup || !currentGroup.ListAirOption) return [];

    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    return currentGroup.ListAirOption.slice(startIndex, endIndex);
  }

  updatePagination() {
    const currentGroup = this.filteredGroups[this.activeTabIndex];
    if (currentGroup && currentGroup.ListAirOption) {
      this.totalPages = Math.ceil(currentGroup.ListAirOption.length / this.pageSize);
      this.currentPage = Math.min(this.currentPage, this.totalPages || 1);
    } else {
      this.totalPages = 1;
      this.currentPage = 1;
    }
  }

  onPageChange(page: number) {
    this.currentPage = page;
  }

  isFlightSelected(option: any): boolean {
    return this.selectedFlights.some(f =>
      f.ListFlightOption[0].ListFlight[0].FlightNumber ===
      option.ListFlightOption[0].ListFlight[0].FlightNumber
    );
  }

  removeFlight(flightToRemove: any) {
    this.selectedFlights = this.selectedFlights.filter(flight => 
      flight.ListFlightOption[0].ListFlight[0].FlightNumber !== 
      flightToRemove.ListFlightOption[0].ListFlight[0].FlightNumber
    );
  }
}