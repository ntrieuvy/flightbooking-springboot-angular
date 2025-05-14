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
import { FlightComponent } from './flight/flight.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { LayoutComponent } from './layout/layout.component';
import { FlightContainerComponent } from '../../components/flight-container/flight-container.component';
import { CheckoutComponent } from './checkout/checkout.component';
import { ProfileComponent } from './profile/profile.component';
import { FlightSearchComponent } from '../../components/flight-container/flight-search/flight-search.component';
import { FlightDetailModalComponent } from '../../components/flight-container/flight-detail-modal/flight-detail-modal.component';
import { FiltersSidebarComponent } from '../../components/flight-container/filters-sidebar/filters-sidebar.component';
import { ResultsListComponent } from '../../components/flight-container/results-list/results-list.component';
import { IndexComponent } from './index/index.component';
import { AuthCallbackComponent } from './auth-callback/auth-callback.component';
import { BookingHistoryComponent } from './booking-history/booking-history.component';
import { BookingConfirmationComponent } from './booking-confirmation/booking-confirmation.component';
// import { FlightCheckoutComponent } from './booking/flight-checkout/flight-checkout.component';

@NgModule({
  declarations: [
    FlightComponent,
    NavItemComponent,
    TopbarComponent,
    HeaderComponent,
    FooterComponent,
    CurrencySwitcherComponent,
    LanguageSwitcherComponent,
    AuthComponent,
    LayoutComponent,
    FlightContainerComponent,
    CheckoutComponent,
    ProfileComponent,
    FlightSearchComponent,
    FlightDetailModalComponent,
    FiltersSidebarComponent,
    ResultsListComponent,
    IndexComponent,
    AuthCallbackComponent,
    BookingHistoryComponent,
    BookingConfirmationComponent,
    // FlightCheckoutComponent,
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
