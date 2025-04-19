import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss']
})
export class AuthComponent {
  @Input() isAuthenticated: boolean = false;
  @Output() isAuthenticatedChange = new EventEmitter<boolean>();
  isModalOpen: boolean = false;
  isProcessing: boolean = false;

  emailOrPhone: string = '';

  onInputChange(value: string): void {
    this.nextProgress();
  }

  nextProgress(): void {
    if (this.emailOrPhone.length > 0) {
      this.isProcessing = true;
    } else {
      this.isProcessing = false;
    }
  }

  openAuthModal(): void {
    this.isModalOpen = true;
  }

  closeModal(): void {
    this.isModalOpen = false;
  }

  closeAuthModal(): void {
    this.closeModal();
  }

  login() {
    this.isAuthenticated = true;
    this.isAuthenticatedChange.emit(this.isAuthenticated);
  }

  logout() {
    this.isAuthenticated = false;
    this.isAuthenticatedChange.emit(this.isAuthenticated);
  }

}
