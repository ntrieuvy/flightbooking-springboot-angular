import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { FlightComponent } from './flight/flight.component';
import { AuthComponent } from './auth/auth.component';
import { LayoutComponent } from './layout/layout.component';
import { CheckoutComponent } from './checkout/checkout.component';
import { ProfileComponent } from './profile/profile.component';
import { IndexComponent } from './index/index.component';
import { AuthCallbackComponent } from './auth-callback/auth-callback.component';
import { BookingHistoryComponent } from './booking-history/booking-history.component';
import { BookingConfirmationComponent } from './booking-confirmation/booking-confirmation.component';

import { AuthGuard } from 'src/app/core/guards/auth.guard';

const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: '', component: IndexComponent, pathMatch: 'full' },
      { path: 'auth', component: AuthComponent },
      { path: 'auth/callback', component: AuthCallbackComponent},
      { path: 'flight', component: FlightComponent },
      { path: 'checkout', component: CheckoutComponent, canActivate: [AuthGuard] },
      { path: 'profile', component: ProfileComponent },
      { path: 'booking/history', component: BookingHistoryComponent, canActivate: [AuthGuard] },
      { path: 'booking/comfirmation', component: BookingConfirmationComponent, canActivate: [AuthGuard] }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientRoutingModule { }
