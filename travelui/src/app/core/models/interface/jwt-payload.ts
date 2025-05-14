export interface JwtPayload {
    id: string;
    fullName: string;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    loginProvider: string;
    sub: string;
    iat: number;
    exp: number;
    authorities: string[];
}