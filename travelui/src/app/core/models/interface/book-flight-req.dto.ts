import { AirOptionReqDTO } from "./air-option-req.dto";
import { ContactInfo } from "./contact-info";
import { Invoice } from "./invoice";
import { Passenger } from "./passenger";

export interface BookFlightReqDTO {
  session: string;
  type: string;
  listAirOption: AirOptionReqDTO[];
  listPassenger: Passenger[];
  contact: ContactInfo;
  invoice: Invoice | null;
  paymentType: string;
}


