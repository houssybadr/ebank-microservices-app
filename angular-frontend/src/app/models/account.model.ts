export interface Customer {
  id?: number;
  name?: string;
  email?: string;
}

export interface Account {
  id?: string;
  createdAt?: string;
  balance?: number;
  type?: string;
  customerId?: number;
  customer?: Customer;
}

export enum RequestStatus {
  SUCCESS = 'SUCCESS',
  ERROR = 'ERROR'
}

export interface AccountListState {
  status: RequestStatus;
  accounts?: Account[];
  errorMessage?: string;
}
