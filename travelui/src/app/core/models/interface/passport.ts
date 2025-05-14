export interface Passport {
  documentType: 'passport' | 'national_id' | 'driving_license';
  documentCode: string;
  documentExpiry: string;
  nationality: string;
  issueCountry: string;
}