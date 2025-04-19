import { Component, OnInit } from '@angular/core';
import { FlightSearchResultComponent } from './flight-search-result/flight-search-result.component';
import { Flight } from 'src/app/core/models/interface/flight';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  searchResults: Flight[] = [];
  loading = false;
  error: string | null = null;
  searchForm: any = null;

  onFlightSearch(searchParams: any): void {
    this.loading = true;
    this.error = null;
    this.searchForm = searchParams;

    setTimeout(() => {
      try {
        this.searchResults = this.getFilteredFlights(searchParams);
        this.loading = false;
      } catch (err) {
        this.error = 'Đã xảy ra lỗi khi tìm kiếm chuyến bay. Vui lòng thử lại.';
        this.loading = false;
      }
    }, 1500);
  }

  private getFilteredFlights(params: any): Flight[] {
    return [
      {
        id: 'FL001',
        airline: 'Vietnam Airlines',
        airlineLogo: 'https://upload.wikimedia.org/wikipedia/vi/b/bc/Vietnam_Airlines_logo.svg',
        flightNumber: 'VN 123',
        departureTime: '08:00',
        arrivalTime: '10:30',
        duration: '2h 30m',
        price: 2500000,
        stops: 0,
        departureAirport: 'HAN',
        arrivalAirport: 'SGN',
        departureCity: 'Hà Nội',
        arrivalCity: 'Hồ Chí Minh'
      },
      {
        id: 'FL002',
        airline: 'Bamboo Airways',
        airlineLogo: 'https://upload.wikimedia.org/wikipedia/commons/7/78/Bamboo_Airways_Logo.svg',
        flightNumber: 'QH 456',
        departureTime: '12:15',
        arrivalTime: '14:45',
        duration: '2h 30m',
        price: 2200000,
        stops: 0,
        departureAirport: 'HAN',
        arrivalAirport: 'SGN',
        departureCity: 'Hà Nội',
        arrivalCity: 'Hồ Chí Minh'
      },
      {
        id: 'FL003',
        airline: 'Vietjet Air',
        airlineLogo: 'assets/images/vietjet-logo.png',
        flightNumber: 'VJ 789',
        departureTime: '15:30',
        arrivalTime: '18:15',
        duration: '2h 45m',
        price: 1800000,
        stops: 0,
        departureAirport: 'HAN',
        arrivalAirport: 'SGN',
        departureCity: 'Hà Nội',
        arrivalCity: 'Hồ Chí Minh'
      },
      {
        id: 'FL004',
        airline: 'Vietnam Airlines',
        airlineLogo: 'https://upload.wikimedia.org/wikipedia/vi/b/bc/Vietnam_Airlines_logo.svg',
        flightNumber: 'VN 246',
        departureTime: '19:00',
        arrivalTime: '21:30',
        duration: '2h 30m',
        price: 2700000,
        stops: 0,
        departureAirport: 'HAN',
        arrivalAirport: 'SGN',
        departureCity: 'Hà Nội',
        arrivalCity: 'Hồ Chí Minh'
      },
      {
        id: 'FL005',
        airline: 'Bamboo Airways',
        airlineLogo: 'https://upload.wikimedia.org/wikipedia/commons/7/78/Bamboo_Airways_Logo.svg',
        flightNumber: 'QH 135',
        departureTime: '06:45',
        arrivalTime: '10:15',
        duration: '3h 30m',
        price: 2100000,
        stops: 1,
        departureAirport: 'HAN',
        arrivalAirport: 'SGN',
        departureCity: 'Hà Nội',
        arrivalCity: 'Hồ Chí Minh'
      }
    ];
  }

}
