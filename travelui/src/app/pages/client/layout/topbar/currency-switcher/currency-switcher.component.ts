import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-currency-switcher',
  templateUrl: './currency-switcher.component.html',
  styleUrls: ['./currency-switcher.component.scss']
})
export class CurrencySwitcherComponent {
  @Input() currencies: any[] = [];
  @Input() currentCurrency!: any;
  @Output() currencyChange = new EventEmitter<any>();

  onChange(event: Event): void {
    const code = (event.target as HTMLSelectElement).value;
    const selected = this.currencies.find(currency => currency.code === code);
    this.currencyChange.emit(selected);
  }
}
