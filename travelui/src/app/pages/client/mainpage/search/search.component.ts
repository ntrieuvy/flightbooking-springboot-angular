import { Component } from '@angular/core';
import { FlightService } from 'src/app/core/services/flight.service';
import { FlightsResDTO } from 'src/app/core/models/interface/flights-res.dto';
import { FlightReqDTO } from 'src/app/core/models/interface/flights-req.dto';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss'],
  providers: [DatePipe]
})
export class SearchComponent {
  loading = false;
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

  constructor(
    private flightService: FlightService,
    private datePipe: DatePipe
  ) { }

  handleSearch(event: { formData: any; requestData: FlightReqDTO }) {
    this.loading = true;
    this.error = null;
    this.selectedAirlines = [];
    this.minPrice = null;
    this.maxPrice = null;

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
  }

  toggleAirlineSelection(airline: string, event: Event): void {
    const checkbox = event.target as HTMLInputElement;
    if (checkbox.checked) {
      if (!this.selectedAirlines.includes(airline)) {
        this.selectedAirlines.push(airline);
      }
    } else {
      this.selectedAirlines = this.selectedAirlines.filter(a => a !== airline);
    }
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

  onFareSelect(event: Event, option: any) {
    const select = event.target as HTMLSelectElement;
    option.selectedFare = option.ListFareOption.find(f =>
      f.CabinName === select.value.split(' - ')[0]
    );
  }

  bookFlight(option: any) {
    if (!option.selectedFare) {
      alert('Please select a fare first');
      return;
    }
    console.log('Booking flight:', option);
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

  formatDate(dateString: string): string {
    const date = new Date(
      parseInt(dateString.slice(4, 8)),
      parseInt(dateString.slice(2, 4)) - 1,
      parseInt(dateString.slice(0, 2))
    );
    return this.datePipe.transform(date, 'dd/MM/yyyy') || '';
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
    console.log(this.selectedFlight);
  }

  closeDetailModal() {
    this.showDetailModal = false;
  }
}