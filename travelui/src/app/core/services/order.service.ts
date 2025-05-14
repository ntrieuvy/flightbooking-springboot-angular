import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ApiService } from './api.service';
import { Order } from '../models/interface/order';

@Injectable({
    providedIn: 'root'
})
export class OrderService {
    private readonly endpoint = '/orders';
    private readonly endpointUserOrders = '/user/null';

    constructor(private apiService: ApiService) {}

    getUserOrders(): Observable<Order[]> {
        return this.apiService.get<Order[]>(`${this.endpoint + this.endpointUserOrders}`)
            .pipe(
                catchError(error => {
                    console.error('Get User Orders Error:', error);
                    return throwError(() => new Error('Failed to get user orders'));
                })
            );
    }
}