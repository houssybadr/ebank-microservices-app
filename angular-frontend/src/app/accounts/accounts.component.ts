import { Component, OnInit, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, map, of } from 'rxjs';
import { AsyncPipe } from '@angular/common';
import { Account, AccountListState, RequestStatus } from '../models/account.model';
import { LoadingService } from '../services/loading.service';

@Component({
  selector: 'app-accounts',
  standalone: true,
  imports: [AsyncPipe],
  templateUrl: './accounts.component.html'
})
export class AccountsComponent implements OnInit {

  private readonly http = inject(HttpClient);
  readonly loadingService = inject(LoadingService);
  readonly RequestStatus = RequestStatus;

  accountsState$!: Observable<AccountListState>;

  ngOnInit() {
    this.accountsState$ = this.http.get<Account[]>('http://localhost:9999/ebank-service/accounts').pipe(
      map((accounts) => ({ status: RequestStatus.SUCCESS, accounts })),
      catchError((error) => of({ status: RequestStatus.ERROR, errorMessage: error.statusText }))
    );
  }
}
