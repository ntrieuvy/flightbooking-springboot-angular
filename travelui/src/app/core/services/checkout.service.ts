import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CheckoutService {
  private tripDetails = new BehaviorSubject<any>(null);
  currentTripDetails = this.tripDetails.asObservable();

  constructor() { }

  updateTripDetails(details: any) {
    this.tripDetails.next(details);
  }

  getPassengerTypeLabel(type: string): string {
    switch(type) {
      case 'ADT': return 'Adult';
      case 'CHD': return 'Child';
      case 'INF': return 'Infant';
      default: return type;
    }
  }
}