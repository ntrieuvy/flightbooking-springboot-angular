import { Injectable } from '@angular/core';
import { Gig, gigs } from './../utils/lib/mockData';

@Injectable({
  providedIn: 'root'
})
export class GigService {
  constructor() { }

  getFeaturedGigs(): Gig[] {
    return gigs.slice(0, 3);
  }

  getAllGigs(): Gig[] {
    return gigs;
  }

  getGigById(id: string): Gig | undefined {
    return gigs.find(gig => gig.id === id);
  }
}