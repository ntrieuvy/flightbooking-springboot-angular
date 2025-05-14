export interface Invoice {
  taxId: string;
  name: string;
  address: string;
  city?: string;
  receiver: string;
  email: string;
}