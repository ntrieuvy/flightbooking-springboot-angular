import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { FlightsResDTO } from '../models/interface/flights-res.dto';
import { FlightReqDTO } from '../models/interface/flights-req.dto';
import { ApiService } from './api.service';
import { BookFlightReqDTO } from '../models/interface/book-flight-req.dto';
import { BookFlightResDTO } from '../models/interface/book-flight-res.dto';

@Injectable({
    providedIn: 'root'
})
export class FlightService {

    private readonly endpoint = '/flights';
    private readonly searchEndpoint = '/search';
    private readonly bookEndpoint = '/book';

    constructor(private apiService: ApiService) {}

    searchFlights(searchData: FlightReqDTO): Observable<FlightsResDTO> {
        const data: FlightReqDTO = {
            ...searchData,
            page: searchData.page || 1,
            pageSize: searchData.pageSize || 10
        };

        return this.apiService.post<FlightsResDTO>(`${this.endpoint + this.searchEndpoint}`, data)
            .pipe(
                catchError(error => {
                    console.error('Search Flights Error:', error);
                    return throwError(() => new Error('Failed to search flights'));
                })
            );
    }

    bookFlight(data: BookFlightReqDTO): Observable<BookFlightResDTO> {
    return this.apiService.post<BookFlightResDTO>(`${this.endpoint + this.bookEndpoint}`, data)
      .pipe(
        catchError(error => {
          console.error('Booking Flight Error:', error);
          return throwError(() => new Error('Failed to book flight'));
        })
      );
  }
}
