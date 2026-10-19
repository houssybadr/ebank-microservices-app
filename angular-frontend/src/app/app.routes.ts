import { Routes } from '@angular/router';
import { AccountsComponent } from './accounts/accounts.component';
import { BotComponent } from './bot/bot.component';

export const routes: Routes = [
  { path: 'accounts', component: AccountsComponent },
  { path: 'bot', component: BotComponent },
  { path: '', redirectTo: 'accounts', pathMatch: 'full' }
];
