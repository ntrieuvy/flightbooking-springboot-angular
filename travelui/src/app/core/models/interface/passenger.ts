import { Baggage } from "./baggage";
import { Passport } from "./passport";

export interface Passenger {
  type: 'ADT' | 'CHD' | 'INF';
  firstName: string;
  lastName: string;
  parentId?: number;
  dob: string;
  gender: 'male' | 'female';
  passport: Passport;
  baggages: Baggage[];
}
