import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-currency-switcher',
  
})
export class CurrencySwitcherComponent {
  @Input() currencies: string[] = [];
  @Input() currentCurrency!: string;
  @Output() currencyChange = new EventEmitter<string>();

  onChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.currencyChange.emit(value);
  }
}
