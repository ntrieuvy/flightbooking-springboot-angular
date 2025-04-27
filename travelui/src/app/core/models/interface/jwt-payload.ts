export interface JwtPayload {
    fullName: string;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    sub: string;
    iat: number;
    exp: number;
    authorities: string[];
}