import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-results-list',
  templateUrl: './results-list.component.html',
  styleUrls: ['./results-list.component.scss'],
  providers: [DatePipe]
})

export class ResultsListComponent {
  @Input() loading: boolean = false;
  @Input() error: string | null = null;
  @Input() flightResults: any;
  @Input() filteredGroups: any[] = [];
  @Input() activeTabIndex: number = 0;
  @Input() currentPage: number = 1;
  @Input() totalPages: number = 1;
  @Input() pageSize: number = 20;
  @Input() selectedFlights: any[] = [];
  @Input() showDetailModal: boolean = false;
  @Input() selectedFlight: any;

  @Output() tabChange = new EventEmitter<number>();
  @Output() pageChange = new EventEmitter<number>();
  @Output() fareSelect = new EventEmitter<{ option: any, value: string }>();
  @Output() flightBook = new EventEmitter<any>();
  @Output() flightRemove = new EventEmitter<any>();
  @Output() flightDetail = new EventEmitter<any>();
  @Output() closeModal = new EventEmitter<void>();
  @Output() submitBooking = new EventEmitter<void>();

  constructor(
    private datePipe: DatePipe,
    private route: ActivatedRoute,
  ) { }

  toggleFareOptions(option: any, event: MouseEvent) {
    event.stopPropagation();
    this.getPaginatedOptions().forEach(item => {
      item.showFareOptions = item === option ? !item.showFareOptions : false;
    });
  }

  hasDayDifference(option: any): boolean {
    const startDate = new Date(option.ListFlightOption[0].ListFlight[0].StartDate);
    const endDate = new Date(option.ListFlightOption[0].ListFlight[0].EndDate);
    return startDate.getDate() !== endDate.getDate();
  }

  isDirectFlight(option: any): boolean {
    return option.ListFlightOption[0].ListFlight.length === 1;
  }

  onTabChange(index: number) {
    this.tabChange.emit(index);
  }

  onPageChange(page: number) {
    if (page >= 1 && page <= this.totalPages) {
      this.pageChange.emit(page);
    }
  }

  onFareSelect(option: any, value: string) {
    option.showFareOptions = false;
    this.fareSelect.emit({ option, value });
  }

  onBookFlight(option: any) {
    if (this.isFlightSelected(option)) {
      this.flightRemove.emit(option);
    } else {
      this.flightBook.emit(option);
    }
  }

  onFlightDetail(flight: any) {
    this.flightDetail.emit(flight);
  }

  onCloseModal() {
    this.closeModal.emit();
  }

  onSubmitBooking() {
    this.submitBooking.emit();
  }

  getPaginatedOptions() {
    const currentGroup = this.filteredGroups[this.activeTabIndex];
    if (!currentGroup || !currentGroup.ListAirOption) return [];

    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    return currentGroup.ListAirOption.slice(startIndex, endIndex);
  }

  getRemarkClass(remark: string): string {
    const remarkMap: { [key: string]: string } = {
      'Promo': 'promo',
      'Best Price': 'best-price',
      'Refundable': 'refundable'
    };
    return remarkMap[remark] || 'default';
  }

  formatTime(dateTimeString: string): string {
    return this.datePipe.transform(dateTimeString, 'HH:mm') || '';
  }

  formatDuration(minutes: number): string {
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return `${hours}h ${mins}m`;
  }

  getAirlineName(code: string): string {
    const airlines: { [key: string]: string } = {
      'VN': 'Vietnam Airlines',
      'VJ': 'VietJet Air',
      'QH': 'Bamboo Airways',
      'BL': 'Pacific Airlines'
    };
    return airlines[code] || code;
  }

  getAirportName(code: string): string {
    const airports: { [key: string]: string } = {
      'SGN': 'Tan Son Nhat',
      'HAN': 'Noi Bai',
      'HUI': 'Phu Bai',
      'UIH': 'Phu Cat',
      'DAD': 'Da Nang',
      'CXR': 'Cam Ranh',
    };
    return airports[code] || code;
  }

  isFlightSelected(option: any): boolean {
    return this.selectedFlights.some(f =>
      f.ListFlightOption[0].ListFlight[0].FlightNumber ===
      option.ListFlightOption[0].ListFlight[0].FlightNumber
    );
  }

  getTotalPrice(): number {
    return this.selectedFlights.reduce((total, flight) => {
      const fareList = flight.selectedFare?.ListFarePax || [];

      let numPassengers = this.getQuantityPassengers();

      const flightTotal = fareList.reduce((sum, pax) => {
        let count = 0;
        switch (pax.PaxType) {
          case 'ADT':
            count = numPassengers.adt;
            break;
          case 'CHD':
            count = numPassengers.chd;
            break;
          case 'INF':
            count = numPassengers.inf;
            break;
        }
        return sum + (pax.TotalFare || 0) * count;
      }, 0);

      return total + flightTotal;
    }, 0);
  }

  getQuantityPassengers(): {adt: number, chd: number, inf: number, total: number} {
    const params = this.route.snapshot.queryParams;
    const adultsCount = parseInt(params['adults'], 10) || 0;
    const childrenCount = parseInt(params['children'], 10) || 0;
    const infantCount = parseInt(params['infant'], 10) || 0;
    return { adt: adultsCount, chd: childrenCount, inf: infantCount, total: adultsCount + childrenCount + infantCount }
  }


  getAirlineLogo(code: string): string {
    const url = "https://plg.datacom.vn/img/airlines/";
    const logoUrl = `${url}${code}.gif`;
    return logoUrl;
  }
}