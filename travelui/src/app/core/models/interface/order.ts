import { ContactInfo } from "./contact-info";
import { Passenger } from "./passenger";
import { Ticket } from "./ticket";

export interface Order {
  id: number;
  providerBookingId?: string;
  customer: ContactInfo;
  type: string;
  status: number;
  note?: string;
  createdAt: string;
  updatedAt: string;
  createdFrom: string;
  isSendMail: boolean;
  orderDetails: OrderDetail[];
  passengers: Passenger[];
}

export interface OrderDetail {
  id: number;
  bookingCode: string;
  flightValue: string;
  numberFlight: number;
  status: number;
  isCancel: boolean;
  createdAt: string;
  updatedAt: string;
  numberAdt: number;
  numberChd: number;
  numberInf: number;
  fareAdt: number;
  fareChd: number;
  fareInf: number;
  taxAdt: number;
  taxChd: number;
  taxInf: number;
  feeAdt: number;
  feeChd: number;
  feeInf: number;
  serviceFeeAdt: number;
  serviceFeeChd: number;
  serviceFeeInf: number;
  totalNetPrice: number;
  totalServiceFee: number;
  totalDiscount: number;
  totalCommission: number;
  totalPrice: number;
  currency: string;
  promo: boolean;
  airline: string;
  expiryDate: string;
  session?: string;
  bookingImage?: string;
  commissionAdt: number;
  commissionChd: number;
  commissionInf: number;
  commissionTotal: number;
  systemCommissionAdt: number;
  systemCommissionChd: number;
  systemCommissionInf: number;
  systemCommissionTotal: number;
  leg: number;
  flights: Flight[];
  tickets: Ticket[];
}

export interface Flight {
  id: number;
  uniqueKey?: string;
  code: string;
  airline: string;
  departure: string;
  arrival: string;
  date: string;
  std: string;
  sta: string;
  number: string;
  duration?: number;
  groupClass?: string;
  fareClass?: string;
  promo?: boolean;
  segments?: string;
  handBaggage?: string;
  allowanceBaggage?: string;
  operating?: string;
}