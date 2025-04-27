import { PaginationMetadata } from "./PaginationMetadata";
import { FlightDatacomResDTO } from "./flights-datacom-res.dto";

export interface FlightsResDTO{
    paginationMetadata: PaginationMetadata;
    data: FlightDatacomResDTO;
}