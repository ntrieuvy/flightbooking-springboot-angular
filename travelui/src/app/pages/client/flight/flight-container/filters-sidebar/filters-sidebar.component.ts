import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-filters-sidebar',
  templateUrl: './filters-sidebar.component.html',
  styleUrls: ['./filters-sidebar.component.scss']
})
export class FiltersSidebarComponent {
  @Input() airlines: string[] = [];
  @Input() selectedAirlines: string[] = [];
  @Input() sortBy: 'price' | 'duration' = 'price';
  @Input() minPrice: number | null = null;
  @Input() maxPrice: number | null = null;

  @Output() sortChange = new EventEmitter<'price' | 'duration'>();
  @Output() airlineToggle = new EventEmitter<{airline: string, checked: boolean}>();
  @Output() priceChange = new EventEmitter<{min: number | null, max: number | null}>();

  maxPossiblePrice: number = 10000000;
  isMobileSidebarOpen = false;

  onSortChange(sortBy: 'price' | 'duration') {
    this.sortChange.emit(sortBy);
  }

  onAirlineToggle(airline: string, checked: boolean) {
    this.airlineToggle.emit({airline, checked});
  }

  onMinPriceChange(value: string) {
    const min = value ? parseInt(value) : null;
    this.priceChange.emit({min, max: this.maxPrice});
  }

  onMaxPriceChange(value: string) {
    const max = value ? parseInt(value) : null;
    this.priceChange.emit({min: this.minPrice, max});
  }

  toggleMobileSidebar() {
    this.isMobileSidebarOpen = !this.isMobileSidebarOpen;
  }
}