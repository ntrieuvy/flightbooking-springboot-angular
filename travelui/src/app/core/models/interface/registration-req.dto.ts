export interface RegistrationReqDTO {
  firstName: string;
  identifier: string;
  identifyType: 'EMAIL' | 'PHONE';
  lastName: string;
  password: string;
}
