import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { ApiService } from './api.service';
import { HttpParams } from '@angular/common/http';
import { RegistrationReqDTO } from '../models/interface/registration-req.dto';
import { AuthenticationReqDTO } from '../models/interface/authentication-req.dto';
import { AuthenticationResDTO } from '../models/interface/authentication-res.dto';
import { JwtHelperService } from './jwt-helper.service';
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private readonly endpoint = '/auth';
    private readonly AUTH_TOKEN_KEY = 'authToken';

    private authStatusSubject = new BehaviorSubject<boolean>(this.checkAuthStatus());
    public authStatus$ = this.authStatusSubject.asObservable();

    constructor(
        private apiService: ApiService,
        private jwtHelper: JwtHelperService,
        private router: Router
    ) { }

    checkUserExists(identifier: string): Observable<boolean> {
        const params = new HttpParams().set('identifier', identifier);
        return this.apiService.get<boolean>(`${this.endpoint}/check-user-exists`, params).pipe(
            catchError(error => {
                console.error('API Error:', error);
                return throwError(() => new Error('Failed to check user existence'));
            })
        );
    }

    register(userData: RegistrationReqDTO): Observable<AuthenticationResDTO> {
        const data = {
            ...userData,
            identifyType: this.determineIdentifyType(userData.identifier)
        };
        return this.apiService.post<AuthenticationResDTO>(`${this.endpoint}/register`, data);
    }

    login(credentials: AuthenticationReqDTO): Observable<AuthenticationResDTO> {
        return this.apiService.post<AuthenticationResDTO>(`${this.endpoint}/authenticate`, credentials)
            .pipe(
                tap(response => this.handleLoginSuccess(response))
            );
    }

    activateAccount(otp: string, sessionId?: string): Observable<AuthenticationResDTO> {
        let params = new HttpParams().set('otp', otp);
        if (sessionId) {
            params = params.set('sessionId', sessionId);
        }
        return this.apiService.get<AuthenticationResDTO>(`${this.endpoint}/activate-account`, params)
            .pipe(
                tap(response => this.handleLoginSuccess(response))
            );
    }

    resendOtp(identifier: string): Observable<void> {
        const params = new HttpParams().set('identifier', identifier);
        return this.apiService.get<void>(`${this.endpoint}/resend-otp`, params);
    }

    initiateSocialLogin(type: string): void {
        this.apiService.post<{ redirectUrl: string }>(
            `${this.endpoint}/social/authenticate?type=${type}`
        ).subscribe({
            next: (response) => {
                window.location.href = response.redirectUrl;
            },
            error: (err) => {
                console.error('Failed to initiate social login', err);
            }
        });
    }

    handleGoogleCallback(code: string): Observable<AuthenticationResDTO> {
        return this.apiService.post<AuthenticationResDTO>(`${this.endpoint}/google/callback`, { code })
            .pipe(
                tap(response => this.handleLoginSuccess(response)),
                catchError(error => {
                    this.router.navigate(['/auth'], { queryParams: { error: 'google_auth_failed' } });
                    return throwError(() => error);
                })
            );
    }

    private handleLoginSuccess(response: AuthenticationResDTO): void {
        if (response.token) {
            this.setAuthToken(response.token);
        }
    }

    setAuthToken(token: string): void {
        localStorage.setItem(this.AUTH_TOKEN_KEY, token);
        this.authStatusSubject.next(true);
    }

    logout(): void {
        localStorage.removeItem(this.AUTH_TOKEN_KEY);
        this.jwtHelper.clearCache();
        this.authStatusSubject.next(false);
    }

    isAuthenticated(): boolean {
        const token = this.getToken();
        return token ? !this.jwtHelper.isTokenExpired(token) : false;
    }

    getCurrentUser(): { fullName: string; email: string } | null {
        const token = this.getToken();
        if (!token) return null;

        const payload = this.jwtHelper.decodeToken(token);
        return payload ? {
            fullName: payload.fullName,
            email: payload.sub
        } : null;
    }

    hasRole(role: string): boolean {
        const token = this.getToken();
        return token ? this.jwtHelper.hasAuthority(token, role) : false;
    }

    getToken(): string | null {
        return localStorage.getItem(this.AUTH_TOKEN_KEY);
    }

    private checkAuthStatus(): boolean {
        return this.isAuthenticated();
    }

    determineIdentifyType(identifier: string): 'EMAIL' | 'PHONE' {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(identifier) ? 'EMAIL' : 'PHONE';
    }
}