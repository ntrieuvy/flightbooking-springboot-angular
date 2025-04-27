export interface FlightDatacomResDTO {
    StatusCode: string;
    Success: boolean;
    Message: string;
    Language: string;
    RequestID: number;
    Session: string;
    ListGroup: Group[];
}

export interface FareItem {
    Code: string;
    Amount: number;
    Name: string;
}

export interface TaxDetail {
    Code: string;
    Amount: number;
    Name: string;
}

export interface FareInfo {
    SegmentId: string;
    StartPoint: string;
    EndPoint: string;
    FareClass: string;
    FareBasis: string;
    HandBaggage: string;
    FreeBaggage: string;
    Availability: number;
}

export interface FarePax {
    PaxType: string;
    PaxNumb: number;
    BaseFare: number;
    BaseFareOrigin: number;
    NetFare: number;
    NetFareOrigin: number;
    TotalFare: number;
    TotalFareOrigin: number;
    ListFareItem: FareItem[];
    ListTaxDetail: TaxDetail[];
    ListFareInfo: FareInfo[];
}

export interface FareOption {
    OptionId: number;
    FareClass: string;
    FareBasis: string;
    FareFamily: string;
    CabinCode: string;
    CabinName: string;
    Refundable: boolean;
    Availability: number;
    Unavailable: boolean;
    ExpiryDate: string;
    BaseFare: number;
    BaseFareOrigin: number;
    PriceAdt: number;
    PriceAdtOrigin: number;
    NetFare: number;
    NetFareOrigin: number;
    TotalFare: number;
    TotalFareOrigin: number;
    Currency: string;
    Airline: string;
    System: string;
    Source: string;
    Tourcode: string;
    ListFarePax: FarePax[];
}

export interface Segment {
    Leg: number;
    SegmentId: string;
    Airline: string;
    Operator: string;
    StartPoint: string;
    EndPoint: string;
    StartDate: string;
    EndDate: string;
    DepartDate: string;
    ArriveDate: string;
    StartTerminal: string;
    EndTerminal: string;
    FlightNumber: string;
    Equipment: string;
    FareClass: string;
    FareBasis: string;
    Duration: number;
    HasStop: boolean;
    StopPoint: string;
    StopTime: number;
    TechnicalStop: string;
    MarriageGrp: string;
    FlightsMiles: number;
    Status: string;
}

export interface Flight {
    Leg: number;
    FlightId: string;
    Airline: string;
    Operator: string;
    StartPoint: string;
    EndPoint: string;
    StartDate: string;
    EndDate: string;
    DepartDate: string;
    ArriveDate: string;
    FlightNumber: string;
    StopNum: number;
    Duration: number;
    ListSegment: Segment[];
}

export interface FlightOption {
    OptionId: number;
    ListFlight: Flight[];
}

export interface AirOption {
    OptionId: number;
    Leg: number;
    Itinerary: number;
    Airline: string;
    System: string;
    Source: string;
    Remark: string;
    ListFlightOption: FlightOption[];
    ListFareOption: FareOption[];
}

export interface Group {
    Leg: number;
    TripType: string;
    Journey: string;
    StartPoint: string;
    EndPoint: string;
    DepartDate: string;
    ListAirOption: AirOption[];
}
