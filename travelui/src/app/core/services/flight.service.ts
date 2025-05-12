import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { FlightsResDTO } from '../models/interface/flights-res.dto';
import { FlightReqDTO } from '../models/interface/flights-req.dto';
import { ApiService } from './api.service';

@Injectable({
    providedIn: 'root'
})
export class FlightService {
    private readonly endpoint = '/flights';
    private readonly searchEndpoint = '/search';

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
}
