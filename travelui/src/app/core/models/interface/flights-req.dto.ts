export interface FlightReqDTO {
  adults: number;
  children: number;
  infant: number;
  flights: FlightInfo[];
  totalPassengersValid?: boolean;
  page?: number;
  pageSize?: number;
}

export interface FlightInfo {
  start: string;
  end: string;
  date: string;
  airline?: string;
}