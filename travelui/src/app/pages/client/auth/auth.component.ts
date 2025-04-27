import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { debounceTime, distinctUntilChanged, switchMap, catchError } from 'rxjs/operators';
import { Subscription, EMPTY } from 'rxjs';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';
import { RegistrationReqDTO } from '../../../core/models/interface/registration-req.dto';
import { AuthenticationReqDTO } from '../../../core/models/interface/authentication-req.dto';
import { AuthenticationResDTO } from '../../../core/models/interface/authentication-res.dto';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss']
})
export class AuthComponent implements OnInit, OnDestroy {
  authForm: FormGroup;
  otpForm: FormGroup;
  showPasswordFields = false;
  showOtpField = false;
  isExistingUser = false;
  isLoading = false;
  errorMessage = '';
  pendingVerificationData: { identifier: string; sessionId?: string } | null = null;

  emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  phoneRegex = /^\+\d{1,3}\d{9,}$/;

  private identifyCheckSubscription!: Subscription;
  private passwordChangesSubscription!: Subscription;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.authForm = this.fb.group({
      identifier: ['', [Validators.required, this.identifierValidator.bind(this)]],
      password: ['', Validators.required],
      confirmPassword: [''],
      firstName: [''],
      lastName: ['']
    }, { validator: this.passwordMatchValidator.bind(this) });

    this.otpForm = this.fb.group({
      otp: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]]
    });
  }

  ngOnInit() {
    this.setupIdentifierCheck();
  }

  ngOnDestroy() {
    this.identifyCheckSubscription?.unsubscribe();
    this.passwordChangesSubscription?.unsubscribe();
  }

  private identifierValidator(control: FormControl): { [key: string]: any } | null {
    const value = control.value;
    if (!value) return null;

    const isEmail = this.emailRegex.test(value);
    const isPhone = this.phoneRegex.test(value);

    return isEmail || isPhone ? null : { invalidIdentifier: true };
  }

  private passwordMatchValidator(form: FormGroup): { [key: string]: any } | null {
    if (this.isExistingUser) return null;

    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  private setupIdentifierCheck(): void {
    this.identifyCheckSubscription = this.authForm.get('identifier')!.valueChanges.pipe(
      debounceTime(1000),
      distinctUntilChanged(),
      switchMap(value => {
        if (!this.authForm.get('identifier')!.valid) {
          return EMPTY;
        }

        this.isLoading = true;
        this.errorMessage = '';
        return this.authService.checkUserExists(value).pipe(
          catchError(error => {
            this.handleIdentifyError(error);
            return EMPTY;
          })
        );
      })
    ).subscribe({
      next: (exists: boolean) => {
        if (typeof exists !== 'boolean') {
          console.error('Invalid response type:', exists);
          this.handleIdentifyError(new Error('Invalid server response'));
          return;
        }
        this.handleIdentifyResponse(exists);
      },
      error: error => this.handleIdentifyError(error)
    });
  }

  private handleIdentifyError(error: any): void {
    this.errorMessage = 'Error checking user existence. Please try again.';
    console.error('Error checking user:', error);
  }

  private handleIdentifyResponse(response: boolean): void {
    this.isLoading = false;
    this.isExistingUser = response;
    this.showPasswordFields = true;

    console.log('User exists:', this.isExistingUser);

    if (this.isExistingUser) {
      this.authForm.get('firstName')?.reset();
      this.authForm.get('lastName')?.reset();
      this.authForm.get('confirmPassword')?.reset();
    }

    const fieldsToUpdate = ['firstName', 'lastName', 'confirmPassword'];
    fieldsToUpdate.forEach(field => {
      const control = this.authForm.get(field)!;
      if (!this.isExistingUser) {
        control.setValidators(field === 'confirmPassword'
          ? [Validators.required]
          : [Validators.required]);
      } else {
        control.clearValidators();
      }
      control.updateValueAndValidity();
    });

    this.passwordChangesSubscription = this.authForm.get('password')!.valueChanges.subscribe(() => {
      this.authForm.get('confirmPassword')?.updateValueAndValidity();
    });
  }

  onSubmit() {
    if (this.authForm.invalid) return;

    const { identifier, password, firstName, lastName } = this.authForm.value;
    this.isLoading = true;
    this.errorMessage = '';

    this.isExistingUser
      ? this.handleLogin(identifier, password)
      : this.handleRegister(identifier, password, firstName, lastName);
  }

  private handleLogin(identifier: string, password: string): void {
    this.authService.login({ identifier, password }).subscribe({
      next: (response) => this.handleAuthSuccess(response),
      error: (error) => this.handleAuthError(error)
    });
  }

  private handleRegister(identifier: string, password: string, firstName: string, lastName: string): void {
    const userData: RegistrationReqDTO = {
      identifier,
      password,
      firstName,
      lastName,
      identifyType: this.authService.determineIdentifyType(identifier)
    };

    this.authService.register(userData).subscribe({
      next: (response) => this.handleRegisterSuccess(response),
      error: (error) => this.handleAuthError(error)
    });
  }

  private handleAuthSuccess(response: AuthenticationResDTO): void {
    this.isLoading = false;
    this.router.navigate(['/']);
  }

  private handleRegisterSuccess(response: AuthenticationResDTO): void {
    this.showOtpField = true;
    this.pendingVerificationData = { identifier: this.authForm.value.identifier };
    this.isLoading = false;
  }

  private handleAuthError(error: any): void {
    this.isLoading = false;
    const err = error?.error || {};

    if (err.businessErrorCode === 303) {
      this.showOtpField = true;
      this.pendingVerificationData = { identifier: this.authForm.value.identifier };
      this.errorMessage = 'Account not verified. Please enter the OTP.';
    } else {
      this.errorMessage = err.message || err.businessErrorDescription || 'Authentication failed.';
    }
  }

  onOtpSubmit() {
    if (this.otpForm.invalid || !this.pendingVerificationData) return;

    this.isLoading = true;
    this.errorMessage = '';

    this.authService.activateAccount(
      this.otpForm.value.otp,
      this.pendingVerificationData.sessionId
    ).subscribe({
      next: (response) => this.handleAuthSuccess(response),
      error: (error) => {
        this.errorMessage = error.error?.message || 'Invalid OTP.';
        this.isLoading = false;
      }
    });
  }

  resendOtp() {
    if (!this.pendingVerificationData) return;

    this.isLoading = true;
    this.authService.resendOtp(this.pendingVerificationData.identifier).subscribe({
      next: () => {
        this.errorMessage = 'New OTP has been sent.';
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage = 'Failed to resend OTP. Please try again.';
        this.isLoading = false;
      }
    });
  }

  loginWithGoogle() {

  }
}