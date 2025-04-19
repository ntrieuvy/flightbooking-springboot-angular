import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModalComponent } from './components/modal/modal.component';
import { TabTableComponent } from './components/tab-table/tab-table.component';

@NgModule({
  declarations: [
    ModalComponent,
    TabTableComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    ModalComponent,
    TabTableComponent]
})
export class SharedModule { }
