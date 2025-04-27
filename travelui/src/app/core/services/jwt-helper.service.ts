import { Injectable } from '@angular/core';
import { JwtPayload } from '../models/interface/jwt-payload';

@Injectable({ providedIn: 'root' })
export class JwtHelperService {
  private readonly TOKEN_KEY = 'authToken';
  private cachedPayload: JwtPayload | null = null;

  decodeToken(token: string): JwtPayload | null {
    if (this.cachedPayload) {
      return this.cachedPayload;
    }

    try {
      const base64Url = token.split('.')[1];
      if (!base64Url) throw new Error('Invalid token format');
      
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      );

      this.cachedPayload = JSON.parse(jsonPayload) as JwtPayload;
      return this.cachedPayload;
    } catch (e) {
      console.error('Error decoding token', e);
      return null;
    }
  }

  isTokenExpired(token: string): boolean {
    const payload = this.decodeToken(token);
    if (!payload?.exp) return true;
    return Date.now() >= payload.exp * 1000;
  }

  hasAuthority(token: string, authority: string): boolean {
    const payload = this.decodeToken(token);
    return payload?.authorities?.includes(authority) || false;
  }

  getCurrentUser(): JwtPayload | null {
    const token = localStorage.getItem(this.TOKEN_KEY);
    if (!token) return null;
    return this.decodeToken(token);
  }

  getUserEmail(): string | null {
    return this.getCurrentUser()?.sub || null;
  }

  getFullName(): string | null {
    return this.getCurrentUser()?.fullName || null;
  }

  getFirstName(): string | null {
    return this.getCurrentUser()?.firstName || null;
  }

  getLastName(): string | null {
    return this.getCurrentUser()?.lastName || null;
  }

  getPhoneNumber(): string | null {
    return this.getCurrentUser()?.phoneNumber || null;
  }

  clearCache(): void {
    this.cachedPayload = null;
  }
}