import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-flight-search',
  templateUrl: './flight-search.component.html',
  styleUrls: ['./flight-search.component.scss']
})
export class FlightSearchComponent implements OnInit {
  @Output() search = new EventEmitter<any>();

  flightForm: FormGroup;
  tripTypes = ['One Way', 'Round Trip'];
  cabinClasses = ['Economy', 'Premium Economy', 'Business', 'First Class'];
  passengersOptions = Array.from({ length: 9 }, (_, i) => i + 1);

  minDate: Date;
  maxDate: Date;

  constructor(private fb: FormBuilder) {
    const currentDate = new Date();
    this.minDate = new Date();
    this.maxDate = new Date();
    this.maxDate.setFullYear(currentDate.getFullYear() + 1);
  }

  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {
    this.flightForm = this.fb.group({
      tripType: ['Round Trip', Validators.required],
      from: ['', Validators.required],
      to: ['', Validators.required],
      departDate: ['', Validators.required],
      returnDate: [''],
      adults: [1, Validators.required],
      children: [0],
      infants: [0],
      cabinClass: ['Economy', Validators.required]
    });

    this.flightForm.get('tripType').valueChanges.subscribe(val => {
      if (val === 'One Way') {
        this.flightForm.get('returnDate').clearValidators();
      } else {
        this.flightForm.get('returnDate').setValidators(Validators.required);
      }
      this.flightForm.get('returnDate').updateValueAndValidity();
    });
  }

  onSubmit(): void {
    if (this.flightForm.valid) {
      this.search.emit(this.flightForm.value);
    }
  }

  swapLocations(): void {
    const from = this.flightForm.get('from').value;
    const to = this.flightForm.get('to').value;
    this.flightForm.get('from').setValue(to);
    this.flightForm.get('to').setValue(from);
  }
}