import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { FlightReqDTO } from 'src/app/core/models/interface/flights-req.dto';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-flight-search',
  templateUrl: './flight-search.component.html',
  styleUrls: ['./flight-search.component.scss']
})
export class FlightSearchComponent implements OnInit {
  @Output() searchSubmitted = new EventEmitter<{ formData: any; requestData: FlightReqDTO }>();

  searchForm: FormGroup;
  tripTypes = ['One Way', 'Round Trip'];
  passengersOptions = Array.from({ length: 9 }, (_, i) => i);
  minDate = new Date();
  maxDate = new Date(new Date().setFullYear(this.minDate.getFullYear() + 1));
  loading = false;
  error: string | null = null;
  showPassengerDropdown = false;
  airports = [
    { name: 'Hà Nội', code: 'HAN' },
    { name: 'TP HCM', code: 'SGN' },
    { name: 'Đà Nẵng', code: 'DAD' },
    { name: 'Nha Trang', code: 'CXR' },
  ];

  constructor(
    private fb: FormBuilder,
    private datePipe: DatePipe,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.initForm();
  }

  ngOnInit() {
    this.setupFormListeners();
    this.route.queryParams.subscribe(params => {
      const isEmptyParams = Object.keys(params).length === 0;
      const isFlightsPage = this.router.url.startsWith('/flight');

      if (isEmptyParams && isFlightsPage) {
        this.onSubmit();
        return;
      }

      const from = params['from'];
      const to = params['to'];
      const departureDate = params['departureDate'];
      const returnDate = params['returnDate'];
      const adults = parseInt(params['adults'] || '1', 10);
      const children = parseInt(params['children'] || '0', 10);
      const infant = parseInt(params['infant'] || '0', 2);

      this.searchForm.patchValue({
        from: from || 'HAN',
        to: to || 'SGN',
        adults: adults,
        children: children,
        infant: infant,
        departDate: departureDate || this.formatDateUI(new Date())
      });

      if (returnDate) {
        this.searchForm.patchValue({
          tripType: 'Round Trip',
          returnDate: returnDate
        });
      } else {
        this.searchForm.patchValue({
          tripType: 'One Way',
          returnDate: ''
        });
      }
    });
  }

  private initForm(): void {
    this.searchForm = this.fb.group({
      tripType: ['One Way', Validators.required],
      adults: [1, [Validators.required, Validators.min(1)]],
      children: [0, [Validators.min(0)]],
      infant: [0, [Validators.min(0)]],
      from: ['HAN', Validators.required],
      to: ['SGN', Validators.required],
      departDate: [this.formatDateUI(new Date()), Validators.required],
      returnDate: ['']
    });
  }

  private setupFormListeners(): void {
    this.searchForm.get('tripType')?.valueChanges.subscribe(value => {
      this.handleTripTypeChange(value);
    });

    this.searchForm.get('departDate')?.valueChanges.subscribe(() => {
      this.searchForm.get('returnDate')?.updateValueAndValidity();
    });
  }

  private handleTripTypeChange(tripType: string): void {
    const returnDateControl = this.searchForm.get('returnDate');
    if (tripType === 'Round Trip') {
      returnDateControl?.setValidators([Validators.required]);
    } else {
      returnDateControl?.clearValidators();
      returnDateControl?.reset();
    }
    returnDateControl?.updateValueAndValidity();
  }

  onSubmit(): void {
    if (this.searchForm.invalid) {
      this.searchForm.markAllAsTouched();
      return;
    }

    const formValue = this.searchForm.value;

    const queryParams = {
      from: formValue.from,
      to: formValue.to,
      departureDate: formValue.departDate,
      adults: formValue.adults,
      children: formValue.children,
      infant: formValue.infant
    };

    if (formValue.tripType === 'Round Trip') {
      queryParams['returnDate'] = formValue.returnDate;
    } else {
      queryParams['returnDate'] = null;
    }

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: queryParams,
      queryParamsHandling: 'merge'
    });

    const requestData: FlightReqDTO = {
      adults: formValue.adults,
      children: formValue.children,
      infant: formValue.infant,
      flights: [
        {
          start: formValue.from,
          end: formValue.to,
          date: this.formatDate(formValue.departDate),
          airline: formValue.airline || undefined
        }
      ],
      totalPassengersValid: true
    };

    if (formValue.tripType === 'Round Trip') {
      requestData.flights.push({
        start: formValue.to,
        end: formValue.from,
        date: this.formatDate(formValue.returnDate),
        airline: formValue.airline || undefined
      });
    }

    this.searchSubmitted.emit({
      formData: formValue,
      requestData: requestData
    });
  }

  private formatDate(dateString: string): string {
    return this.datePipe.transform(dateString, 'ddMMyyyy') || '';
  }

  private formatDateUI(date: Date): string {
    return date.toISOString().split('T')[0];
  }

  swapLocations(): void {
    const from = this.searchForm.get('from')?.value;
    const to = this.searchForm.get('to')?.value;
    this.searchForm.patchValue({
      from: to,
      to: from
    });
  }

  getAirportCode(cityName: string): string {
    if (!cityName) return '';
    const airport = this.airports.find(a =>
      cityName.toLowerCase().includes(a.name.toLowerCase())
    );
    return airport?.code || '';
  }

  togglePassengerDropdown() {
    this.showPassengerDropdown = !this.showPassengerDropdown;
  }

  closePassengerDropdown() {
    this.showPassengerDropdown = false;
  }

  adjustPassengers(type: string, delta: number) {
    const current = this.searchForm.get(type)?.value || 0;
    const newValue = Math.max(type === 'adults' ? 1 : 0, current + delta);
    this.searchForm.get(type)?.setValue(newValue);
  }

  totalPassengers() {
    return this.searchForm.value.adults + this.searchForm.value.children + this.searchForm.value.infant;
  }

}
