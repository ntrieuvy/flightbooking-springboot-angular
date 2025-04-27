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

  constructor(
    private flightService: FlightService,
    private datePipe: DatePipe
  ) { }

  handleSearch(event: { formData: any; requestData: FlightReqDTO }) {
    this.loading = true;
    this.error = null;

    this.flightService.searchFlights(event.requestData).subscribe({
      next: (res: FlightsResDTO) => {
        this.flightResults = res;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.message || 'Failed to search flights';
        this.loading = false;
      }
    });
  }

  formatDate(dateString: string): string {
    if (!dateString) return '';
    // Assuming format is DDMMYYYY
    const day = dateString.substring(0, 2);
    const month = dateString.substring(2, 4);
    const year = dateString.substring(4, 8);
    return `${day}/${month}/${year}`;
  }

  formatTime(dateTimeString: string): string {
    if (!dateTimeString) return '';
    return this.datePipe.transform(dateTimeString, 'HH:mm') || '';
  }

  formatDuration(minutes: number): string {
    if (!minutes) return '';
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
  
}