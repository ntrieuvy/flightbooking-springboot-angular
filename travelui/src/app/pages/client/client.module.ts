import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

import { ClientRoutingModule } from './client-routing.module';
import { HeaderComponent } from './layout/header/header.component';
import { NavItemComponent } from './layout/header/nav-item/nav-item.component';
import { FooterComponent } from './layout/footer/footer.component';
import { TopbarComponent } from './layout/topbar/topbar.component';
import { CurrencySwitcherComponent } from './layout/topbar/currency-switcher/currency-switcher.component';
import { LanguageSwitcherComponent } from './layout/topbar/language-switcher/language-switcher.component';
import { AuthComponent } from './auth/auth.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MainpageComponent } from './mainpage/mainpage.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { LayoutComponent } from './layout/layout.component';
import { SearchComponent } from './mainpage/search/search.component';
import { BookingComponent } from './booking/booking.component';
import { ProfileComponent } from './profile/profile.component';
import { FlightSearchComponent } from './mainpage/search/flight-search/flight-search.component';
// import { FlightCheckoutComponent } from './booking/flight-checkout/flight-checkout.component';


@NgModule({
  declarations: [
    MainpageComponent,
    NavItemComponent,
    TopbarComponent,
    HeaderComponent,
    FooterComponent,
    CurrencySwitcherComponent,
    LanguageSwitcherComponent,
    AuthComponent,
    LayoutComponent,
    SearchComponent,
    BookingComponent,
    ProfileComponent,
    FlightSearchComponent,
    // FlightCheckoutComponent
  ],
  imports: [
    CommonModule,
    ClientRoutingModule,
    FormsModule,
    SharedModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [DatePipe]
})
export class ClientModule { }
