import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class LoadingService {

  private readonly loading = signal(false);
  readonly isLoading = this.loading.asReadonly();

  setLoading(value: boolean) {
    this.loading.set(value);
  }
}
