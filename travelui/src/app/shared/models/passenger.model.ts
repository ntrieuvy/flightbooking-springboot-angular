export interface Passenger {
  id?: string;
  firstName: string;
  lastName: string;
  dateOfBirth: Date;
  gender: 'male' | 'female' | 'other';
  passportNumber: string;
  passportExpiry: Date;
  nationality: string;
  type: 'ADT' | 'CHD' | 'INF';
} 