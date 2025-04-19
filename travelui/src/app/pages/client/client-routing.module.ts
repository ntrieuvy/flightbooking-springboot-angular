import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MainpageComponent } from './mainpage/mainpage.component';
import { AuthComponent } from './auth/auth.component';
import { LayoutComponent } from './layout/layout.component';
import { BookingComponent } from './booking/booking.component';
import { ProfileComponent } from './profile/profile.component';

const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: '', component: MainpageComponent},
      { path: 'login', component: AuthComponent },
      { path: ':slug/booking', component: BookingComponent },
      { path: 'user/profile', component: ProfileComponent },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientRoutingModule { }
