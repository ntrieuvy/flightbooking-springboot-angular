import { Component, OnInit, EventEmitter, Output, OnDestroy } from '@angular/core';
import { Currency } from '../../../../core/models/interface/currency';
import { Language } from '../../../../core/models/interface/language';
import { AuthService } from '../../../../core/services/auth.service';
import { Subscription } from 'rxjs';
import { Role } from 'src/app/core/models/enum/role.enum';

@Component({
  selector: 'app-topbar',
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.scss']
})
export class TopbarComponent implements OnInit, OnDestroy {
  @Output() openAuthModel = new EventEmitter<void>();

  dropdownOpen = false;
  mobileMenuOpen = false;

  logoUrl = './assets/images/logo.png';
  siteTitle = 'Travel Booking';
  localeUrl = '/';
  headerRightMenu = true;
  enableHeaderSticky = true;
  containerClass = 'container';
  enableRegistration = true;
  
  isAuthenticated = false;
  isVendor = false;
  isAdmin = false;
  avatarUrl = '';
  userName = '';
  userNameInitial = '';
  email = '';

  private authSubscription!: Subscription;

  currencies: Currency[] = [
    { code: 'USD', name: 'US Dollar' },
    { code: 'VND', name: 'Việt Nam Đồng' },
    { code: 'JPY', name: 'Japanese Yen' }
  ];
  currentCurrency: Currency = this.currencies[1];

  languages: Language[] = [
    { locale: 'en', name: 'English', flag: 'us' },
    { locale: 'vi', name: 'Tiếng Việt', flag: 'vn' },
    { locale: 'ja', name: '日本語', flag: 'jp' }
  ];
  currentLanguage: Language = this.languages[0];

  isModalOpen: boolean = false;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.updateAuthState();
    
    this.authSubscription = this.authService.authStatus$.subscribe(
      (isAuthenticated: boolean) => {
        this.isAuthenticated = isAuthenticated;
        this.updateAuthState();
      }
    );
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  private updateAuthState(): void {
    if (this.isAuthenticated) {
      const currentUser = this.authService.getCurrentUser();
      if (currentUser) {
        this.userName = currentUser.fullName || '';
        this.email = currentUser.email;
        this.userNameInitial = this.userName.charAt(0).toUpperCase();
      }
      
      this.isVendor = this.authService.hasRole(Role.VENDOR);
      this.isAdmin = this.authService.hasRole(Role.ADMIN);

    } else {
      this.userName = '';
      this.email = '';
      this.userNameInitial = '';
      this.isVendor = false;
      this.isAdmin = false;
    }
  }

  switchCurrency(currency: Currency): void {
    if (currency) {
      this.currentCurrency = currency;
    }
  }

  switchLanguage(language: Language): void {
    if (language) {
      this.currentLanguage = language;
    }
  }

  toggleDropdown(): void {
    this.dropdownOpen = !this.dropdownOpen;
  }

  toggleMobileMenu(): void {
    this.mobileMenuOpen = !this.mobileMenuOpen;
  }

  logout(): void {
    this.authService.logout();
    this.dropdownOpen = false;
  }

  openModal() {
    this.openAuthModel.emit();
  }

  closeModal() {
    this.isModalOpen = false;
  }

  login() {

  }
}