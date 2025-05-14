export interface Ticket {
  id?: number;
  airline: string;
  ticketNumber: string;
  issueDate: string | Date;
  bookingCode: string;
  passengerName: string;
  segments?: string;
  ticketImage?: string;
  ticketType?: string;
  totalPrice: number;
  status?: string;
  errorMessage?: string;
  itinerary?: number;
  startPoint?: string;
  endPoint?: string;
  departDate?: string;
  returnDate?: string;
  fareClass?: string;
  fareBasis?: string;
  flightType?: string;
  createdAt?: string | Date;
  updatedAt?: string | Date;
  isSendMail: boolean;
}