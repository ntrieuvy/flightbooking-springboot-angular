export interface Flight {
  id: string;
  flightNumber: string;
  airline: string;
  departureCity: string;
  arrivalCity: string;
  departureTime: Date;
  arrivalTime: Date;
  duration: string;
  price: number;
  availableSeats: number;
  aircraft: string;
  status: 'scheduled' | 'delayed' | 'cancelled' | 'completed';
} 