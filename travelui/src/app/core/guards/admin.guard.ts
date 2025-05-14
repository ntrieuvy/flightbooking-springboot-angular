import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from 'src/app/core/services/auth.service';
import { map, take, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {
  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> {
    return this.authService.authStatus$.pipe(
      take(1),
      map(isAuthenticated => {
        const isAdmin = isAuthenticated && this.authService.hasRole('ROLE_ADMIN');
        if (!isAdmin) {
          this.router.navigate(['/auth'], {
            queryParams: { unauthorized: true }
          });
        }
        return isAdmin;
      })
    );
  }
}
