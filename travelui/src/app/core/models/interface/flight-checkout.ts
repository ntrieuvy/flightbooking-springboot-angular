export interface FlightCheckout {
  flightNumber: string;
  session: string;
  fareOptionId: number;
  airOptionId: number;
  flightOptionId: number;
  airline: string;
  departure: string;
  arrival: string;
  fare: {
    type: string;
    price: number;
    currency: string;
  };
}