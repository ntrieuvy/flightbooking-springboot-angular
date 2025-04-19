import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Flight } from '../../../../../core/models/interface/flight';

@Component({
  selector: 'app-flight-search-result',
  templateUrl: './flight-search-result.component.html',
  styleUrls: ['./flight-search-result.component.scss']
})
export class FlightSearchResultComponent {

  constructor(private router: Router) { }

  @Input() flights: Flight[] = [];
  @Input() loading = false;
  @Input() error: string | null = null;
  @Input() tripType: string = 'One Way';
  @Input() searchForm: any = null;


  showDetailModal = false;
  showBookingModal = false;
  selectedFlight: Flight | null = null;
  selectedFlights: Flight[] = [];

  showFlightDetails(flight: Flight): void {
    this.selectedFlight = flight;
    this.showDetailModal = true;
  }

  toggleFlightSelection(flight: Flight, event: Event): void {
    event.stopPropagation();

    const index = this.selectedFlights.findIndex(f => f.id === flight.id);

    if (index > -1) {
      this.selectedFlights.splice(index, 1);
    } else {
      if (this.tripType === 'One Way' && this.selectedFlights.length < 1) {
        this.selectedFlights = [flight];
      } else if (this.tripType === 'Round Trip' && this.selectedFlights.length < 2) {
        this.selectedFlights.push(flight);
      }
    }
  }

  isFlightSelected(flight: Flight): boolean {
    return this.selectedFlights.some(f => f.id === flight.id);
  }

  confirmSelection(): void {
    if ((this.tripType === 'One Way' && this.selectedFlights.length === 1) ||
      (this.tripType === 'Round Trip' && this.selectedFlights.length === 2)) {
      this.showBookingModal = true;
    }
  }

  getTotalPrice(): number {
    return this.selectedFlights.reduce((sum, flight) => sum + flight.price, 0);
  }

  completeBooking(): void {
    // console.log('Booking completed:', this.selectedFlights);
    // this.showBookingModal = false;
    // const flightIds = this.selectedFlights.map(flight => flight.id);
    // this.selectedFlights = [];
    // this.router.navigate(['/flight/booking'], { queryParams: { ids: flightIds.join(',') } });
    // console.log(this.searchForm);
    this.showBookingModal = false;
    const flightIds = this.selectedFlights.map(flight => flight.id);
    const queryParams: any = {
      ids: flightIds.join(',')
    };

    // Thêm các trường trong searchForm vào queryParams
    if (this.searchForm) {
      Object.keys(this.searchForm).forEach(key => {
        queryParams[key] = this.searchForm[key];
      });
    }

    this.selectedFlights = [];

    this.router.navigate(['/flight/booking'], { queryParams });
    console.log('Search Form sent:', this.searchForm);
  }

}