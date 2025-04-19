import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AbstractControl, FormArray, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';

@Component({
  selector: 'app-booking',
  templateUrl: './booking.component.html',
  styleUrls: ['./booking.component.scss']
})
export class BookingComponent implements OnInit {
  slug: string | null = null;
  ids: string[] = [];
  searchForm: any = {};
  bookingForm: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder
  ) {
    this.bookingForm = this.fb.group({
      adults: this.fb.array([]),
      children: this.fb.array([]),
      infants: this.fb.array([]),
      contactInfo: this.fb.group({
        fullName: ['', Validators.required],
        phone: ['', [Validators.required, Validators.pattern(/^[0-9]{10,15}$/)]],
        email: ['', [Validators.required, Validators.email]],
        idNumber: ['', Validators.required],
        address: ['', Validators.required]
      })
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.slug = params.get('slug');
    });

    this.route.queryParams.subscribe(params => {
      this.ids = params['ids']?.split(',') || [];
      this.searchForm = { ...params };
      this.initPassengerForms();
    });
  }

  initPassengerForms(): void {
    this.adults.clear();
    this.children.clear();
    this.infants.clear();

    for (let i = 0; i < +this.searchForm.adults; i++) {
      this.addAdult();
    }

    for (let i = 0; i < +this.searchForm.children; i++) {
      this.addChild();
    }

    for (let i = 0; i < +this.searchForm.infants; i++) {
      this.addInfant();
    }
  }

  get adults(): FormArray {
    return this.bookingForm.get('adults') as FormArray;
  }

  addAdult(): void {
    this.adults.push(this.fb.group({
      fullName: ['', Validators.required],
      idNumber: ['', Validators.required],
      dob: ['', [Validators.required, this.validateAge(12, 150)]], // 12+ tuổi
      gender: ['male', Validators.required]
    }));
  }

  get children(): FormArray {
    return this.bookingForm.get('children') as FormArray;
  }

  addChild(): void {
    this.children.push(this.fb.group({
      fullName: ['', Validators.required],
      idNumber: ['', Validators.required],
      dob: ['', [Validators.required, this.validateAge(2, 11)]], // 2-11 tuổi
      gender: ['male', Validators.required]
    }));
  }

  get infants(): FormArray {
    return this.bookingForm.get('infants') as FormArray;
  }

  addInfant(): void {
    this.infants.push(this.fb.group({
      fullName: ['', Validators.required],
      birthCertNumber: ['', Validators.required],
      dob: ['', [Validators.required, this.validateAge(0, 1)]], // 0-1 tuổi
      gender: ['male', Validators.required]
    }));
  }


  onSubmit(): void {
    if (this.bookingForm.valid) {
      console.log('Form submitted:', this.bookingForm.value);
    } else {
      this.markFormGroupTouched(this.bookingForm);
    }
  }

  private validateAge(minAge: number, maxAge: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) return null;

      const birthDate = new Date(control.value);
      const today = new Date();
      
      let age = today.getFullYear() - birthDate.getFullYear();
      const monthDiff = today.getMonth() - birthDate.getMonth();
      
      if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        age--;
      }

      if (age < minAge) {
        return { underAge: `Must be at least ${minAge} years old` };
      }
      
      if (age > maxAge) {
        return { overAge: `Must be under ${maxAge + 1} years old` };
      }

      return null;
    };
  }

  private markFormGroupTouched(formGroup: FormGroup | FormArray): void {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();

      if (control instanceof FormGroup || control instanceof FormArray) {
        this.markFormGroupTouched(control);
      }
    });
  }
}