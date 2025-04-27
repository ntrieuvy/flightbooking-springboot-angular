import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private readonly apiUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  get<T>(path: string, params: HttpParams = new HttpParams()): Observable<T> {
    return this.http.get<T>(`${this.apiUrl}${path}`, { params });
  }

  post<T>(path: string, body: object = {}): Observable<T> {
    return this.http.post<T>(
      `${this.apiUrl}${path}`,
      body,
      { headers: this.getHeaders() }
    );
  }

  put<T>(path: string, body: object = {}): Observable<T> {
    return this.http.put<T>(
      `${this.apiUrl}${path}`,
      body,
      { headers: this.getHeaders() }
    );
  }

  delete<T>(path: string): Observable<T> {
    return this.http.delete<T>(
      `${this.apiUrl}${path}`,
      { headers: this.getHeaders() }
    );
  }

  private getHeaders(): HttpHeaders {
    const headersConfig = {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    };

    const token = localStorage.getItem('token');
    if (token) {
      headersConfig['Authorization'] = `Bearer ${token}`;
    }

    return new HttpHeaders(headersConfig);
  }
}