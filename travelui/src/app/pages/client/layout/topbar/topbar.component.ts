import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { Currency } from '../../../../core/models/interface/currency';
import { Language } from '../../../../core/models/interface/language';

@Component({
  selector: 'app-topbar',
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.scss']
})
export class TopbarComponent implements OnInit {

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
  isAuthenticated = true;
  isVendor = false;
  isAdmin = false;
  avatarUrl = '';
  userName = 'John Doe';
  userNameInitial = 'J';
  email = "trangnq@teamsolutions.vn";

  currencies: Currency[] = [
    { code: 'USD', name: 'US Dollar' },
    { code: 'VND', name: 'Việt Nam Đồng' },
    { code: 'JPY', name: 'Japanese Yen' }
  ];
  currentCurrency: Currency = this.currencies[0];

  languages: Language[] = [
    { locale: 'en', name: 'English', flag: 'us' },
    { locale: 'vi', name: 'Tiếng Việt', flag: 'vn' },
    { locale: 'ja', name: '日本語', flag: 'jp' }
  ];
  currentLanguage: Language = this.languages[0];

  isModalOpen: boolean = false;

  constructor() {}

  ngOnInit(): void {}

  switchCurrency(currency: Currency): void {
    if (currency) {
      this.currentCurrency = currency;
      console.log('Switched currency:', currency);
    }
  }

  switchLanguage(language: Language): void {
    if (language) {
      this.currentLanguage = language;
      console.log('Switched language:', language);
    }
  }

  toggleDropdown(): void {
    this.dropdownOpen = !this.dropdownOpen;
  }

  toggleMobileMenu(): void {
    this.mobileMenuOpen = !this.mobileMenuOpen;
  }

  logout(): void {
    console.log('User logged out');
    this.isAuthenticated = false;
  }

  openModal() {
    this.openAuthModel.emit();
  }

  closeModal() {
    this.isModalOpen = false;
  }

  login() {
    console.log('Login with email or phone');
  }
}
