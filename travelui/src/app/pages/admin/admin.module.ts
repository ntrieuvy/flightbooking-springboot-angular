import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { AdminRoutingModule } from './admin-routing.module';
import { AdminDoashboardComponent } from './admin-doashboard/admin-doashboard.component';

@NgModule({
  declarations: [
  AdminDoashboardComponent],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    AdminRoutingModule
  ],
  exports: [
  ]
})
export class AdminModule { }
