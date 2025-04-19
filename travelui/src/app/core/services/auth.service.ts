import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';
import { User } from '../models/interface/user';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUser: User | null = null;

  constructor(private http: HttpClient) {}

  getCurrentUser(): Observable<User | null> {
    if (this.currentUser) {
      return of(this.currentUser);
    }
    return this.http.get<User>('/api/user').pipe(
      tap(user => this.currentUser = user)
    );
  }

  logout(): Observable<any> {
    return this.http.post('/api/logout', {}).pipe(
      tap(() => this.currentUser = null)
    );
  }
}