import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-auth-callback',
  templateUrl: './auth-callback.component.html',
  styleUrls: ['./auth-callback.component.scss']
})
export class AuthCallbackComponent implements OnInit {
  loadingMessage = 'Processing authentication...';
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const { code, error, otp } = this.route.snapshot.queryParams;

    if (error) {
      this.handleError('authentication_error', error);
      return;
    }

    if (otp) {
      this.handleAccountActivation(otp);
    } else if (code) {
      this.handleGoogleAuth(code);
    } else {
      this.router.navigate(['/auth']);
    }
  }

  private handleAccountActivation(otp: string): void {
    this.loadingMessage = 'Activating your account...';
    this.authService.activateAccount(otp).pipe(
      finalize(() => this.navigateAfterAuth())
    ).subscribe({
      error: (err) => this.handleError('activation_failed', err)
    });
  }

  private handleGoogleAuth(code: string): void {
    this.loadingMessage = 'Signing in with Google...';
    this.authService.handleGoogleCallback(code).pipe(
      finalize(() => this.navigateAfterAuth())
    ).subscribe({
      error: (err) => this.handleError('google_auth_failed', err)
    });
  }

  private navigateAfterAuth(): void {
    setTimeout(() => {
      this.router.navigate(['']);
    }, 1000);
  }

  private handleError(errorType: string, err: any): void {
    this.error = errorType;
    console.error(`${errorType.replace('_', ' ')} failed:`, err);
    
    setTimeout(() => {
      this.router.navigate(['/auth'], { 
        queryParams: { error: errorType } 
      });
    }, 3000);
  }
}