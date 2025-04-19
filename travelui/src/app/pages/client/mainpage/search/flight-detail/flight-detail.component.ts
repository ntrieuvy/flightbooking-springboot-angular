import { Component, Input } from '@angular/core';
import { Flight } from 'src/app/core/models/interface/flight';

@Component({
  selector: 'app-flight-detail',
  templateUrl: './flight-detail.component.html',
  styleUrls: ['./flight-detail.component.scss']
})
export class FlightDetailComponent {
  @Input() flight: Flight | null = null;

  getCurrentDate(): string {
    const options: Intl.DateTimeFormatOptions = { 
      weekday: 'long', 
      year: 'numeric', 
      month: 'long', 
      day: 'numeric' 
    };
    return new Date().toLocaleDateString('vi-VN', options);
  }
}