import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FlightCheckout } from 'src/app/core/models/interface/flight-checkout';
import { Passenger } from 'src/app/core/models/interface/passenger';
import { ContactInfo } from 'src/app/core/models/interface/contact-info';
import { Invoice } from 'src/app/core/models/interface/invoice';
import { AuthService } from 'src/app/core/services/auth.service';
import { BookFlightReqDTO } from 'src/app/core/models/interface/book-flight-req.dto';
import { FlightService } from 'src/app/core/services/flight.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss']
})
export class CheckoutComponent implements OnInit, OnDestroy {
  bookingData: FlightCheckout[] = [];
  formData: any = {};
  
  passengers: Passenger[] = [];

  contactInfo: ContactInfo = {
    fullName: this.authService.getCurrentUser().fullName,
    email: this.authService.getCurrentUser().email,
    phone: this.authService.getCurrentUser().phone,
    address: ''
  };

  invoiceInfo: Invoice = {
    taxId: '',
    name: '',
    address: '',
    city: '',
    receiver: '',
    email: ''
  };
  
  currentStep = 1;
  totalSteps = 3;
  countries: string[] = ['Viet Nam'];
  selectedPayment: string = 'cash';
  needInvoice: boolean = false;
  isSubmitting: boolean = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService,
    private flightService: FlightService
  ) {}

  ngOnInit(): void {
    if (!this.restoreBookingData()) {
      this.router.navigate(['/'], { replaceUrl: true });
    }
  }

  ngOnDestroy(): void {
    sessionStorage.removeItem('currentBooking');
  }

  private restoreBookingData(): boolean {
    const sources = [
      () => history.state,
      () => ({
        bookingData: this.safeJsonParse(this.route.snapshot.queryParams['bookingData']),
        formData: this.safeJsonParse(this.route.snapshot.queryParams['formData'])
      }),
      () => this.safeJsonParse(sessionStorage.getItem('currentBooking'))
    ];

    for (const source of sources) {
      try {
        const data = source();
        if (data?.bookingData && data?.formData) {
          this.bookingData = data.bookingData;
          this.formData = data.formData;
          this.initPassengers();
          return true;
        }
      } catch (e) {
        console.warn('Failed to parse booking data', e);
      }
    }
    
    return false;
  }

  private safeJsonParse(data: any): any {
    try {
      return data && JSON.parse(data);
    } catch {
      return null;
    }
  }

  private initPassengers(): void {
    this.passengers = [];
    
    const adults = parseInt(this.formData.adults || '1', 10);
    const children = parseInt(this.formData.children || '0', 10);
    const infants = parseInt(this.formData.infant || '0', 10);

    for (let i = 0; i < adults; i++) {
      this.passengers.push(this.createPassenger('ADT'));
    }
    
    for (let i = 0; i < children; i++) {
      this.passengers.push(this.createPassenger('CHD'));
    }
    
    for (let i = 0; i < infants; i++) {
      const infant = this.createPassenger('INF');
      const adultIndex = i % adults;
      if (adults > 0) {
        infant.parentId = adultIndex;
      }
      this.passengers.push(infant);
    }
  }

  private createPassenger(type: 'ADT' | 'CHD' | 'INF'): Passenger {
    return {
      type,
      firstName: '',
      lastName: '',
      dob: '',
      gender: 'male',
      parentId: undefined,
      passport: {
        documentType: 'passport',
        documentCode: '',
        documentExpiry: '',
        nationality: '',
        issueCountry: ''
      },
      baggages: []
    };
  }

  getAdults(): Passenger[] {
    return this.passengers.filter(p => p.type === 'ADT');
  }

  getAdultOptions(): {id: number, name: string}[] {
    return this.getAdults().map((adult, index) => ({
      id: index,
      name: `${adult.firstName} ${adult.lastName}` || `Adult ${index + 1}`
    }));
  }

  calculateAge(dob: string): number {
    if (!dob) return 0;
    const birthDate = new Date(dob);
    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();
    
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
      age--;
    }
    
    return age;
  }

  isDocumentExpired(expiryDate: string): boolean {
    if (!expiryDate) return false;
    return new Date(expiryDate) < new Date();
  }

  selectPayment(method: string) {
    this.selectedPayment = method;
  }

  private validatePassengerAge(passenger: Passenger): boolean {
    if (!passenger.dob) return false;
    
    const age = this.calculateAge(passenger.dob);
    
    switch (passenger.type) {
      case 'ADT': return age >= 12;
      case 'CHD': return age >= 2 && age <= 12;
      case 'INF': return age < 2;
      default: return false;
    }
  }

  private validateFormPassenger(): boolean {
    for (const passenger of this.passengers) {
      if (!passenger.firstName || !passenger.lastName) {
        alert('Please fill in all passenger names');
        return false;
      }

      if (!passenger.dob) {
        alert('Please enter date of birth for all passengers');
        return false;
      }

      if (!this.validatePassengerAge(passenger)) {
        let message = '';
        switch (passenger.type) {
          case 'ADT': message = 'Adult must be 12 years or older'; break;
          case 'CHD': message = 'Child must be between 2-12 years old'; break;
          case 'INF': message = 'Infant must be under 2 years old'; break;
        }
        alert(message);
        return false;
      }

      if (passenger.type === 'INF') {
        if (passenger.parentId === undefined || passenger.parentId === null) {
          alert('Please select an accompanying adult for the infant');
          return false;
        }
        
        const parent = this.passengers[passenger.parentId];
        if (!parent || parent.type !== 'ADT') {
          alert('Infant must be accompanied by an adult');
          return false;
        }
      }

      if (passenger.type === "ADT" &&
          (!passenger.passport.documentCode || 
          !passenger.passport.nationality || 
          !passenger.passport.issueCountry)) {
        alert('Please fill in all required passport information');
        return false;
      }

      if (passenger.type === "ADT" && (!passenger.passport.documentExpiry || 
          this.isDocumentExpired(passenger.passport.documentExpiry))) {
        alert('Please provide a valid document expiry date in the future');
        return false;
      }
    }
    
    return true;
  }

  private validateFormContact(): boolean {
    if (!this.contactInfo.fullName || !this.contactInfo.email || !this.contactInfo.phone) {
      alert('Please fill in all contact information');
      return false;
    }
    
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.contactInfo.email)) {
      alert('Please enter a valid email address');
      return false;
    }

    if (this.needInvoice) {
      if (!this.invoiceInfo.taxId || !this.invoiceInfo.name || !this.invoiceInfo.address || 
          !this.invoiceInfo.city || !this.invoiceInfo.receiver || !this.invoiceInfo.email) {
        alert('Please fill in all invoice information');
        return false;
      }

      if (!emailRegex.test(this.invoiceInfo.email)) {
        alert('Please enter a valid receiver email address');
        return false;
      }
    }

    return true;
  }

  nextStep(): void {
    if (this.currentStep == 1 && !this.validateFormPassenger()) {
      return;
    }

    if (this.currentStep == 2 && !this.validateFormContact()) {
      return;
    }

    if (this.currentStep < this.totalSteps) {
      this.currentStep++;
    }
  }

  prevStep(): void {
    if (this.currentStep > 1) {
      this.currentStep--;
    }
  }

  async onSubmit() {
    if (!this.validateFormPassenger() && !this.validateFormContact()) {
      return;
    }
    
    const bookingDetails: BookFlightReqDTO = {
      session: this.bookingData[0]?.session,
      type: 'verify',
      listAirOption: this.bookingData.map(b => ({
        session: this.bookingData[0]?.session,
        sessionType: 'verify',
        airlineOptionId: b.airOptionId,
        fareOptionId: b.fareOptionId,
        flightOptionId: b.flightOptionId
      })),
      listPassenger: this.passengers,
      contact: this.contactInfo,
      invoice: this.needInvoice ? this.invoiceInfo : null,
      paymentType: this.selectedPayment
    };

    this.isSubmitting = true;

    try {
      await this.flightService.bookFlight(bookingDetails).subscribe({
        next: (response) => {
          if (response?.Success) {
            alert('Booking successful: ' + response.message);
            this.router.navigate(['/booking/comfirmation']);
          } else {
            alert('Booking failed: ' + response.message);
          }
        },
        error: (err) => {
          alert('An error occurred while booking the flight.');
        }
      });
    } catch (error) {
      alert('An error occurred while booking the flight.');
    } finally {
      this.isSubmitting = false;
    }
  }

  getTotalPrice(): number {
    return this.bookingData.reduce((total, flight) => total + flight.fare.price, 0);
  }

  formatDate(dateString: string): string {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      weekday: 'short',
      month: 'short',
      day: 'numeric',
      year: 'numeric'
    });
  }

  formatTime(dateString: string): string {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}