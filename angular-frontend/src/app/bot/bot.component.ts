import { Component, inject } from '@angular/core';
import { HttpClient, HttpDownloadProgressEvent, HttpEventType, HttpResponse } from '@angular/common/http';
import { map } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { MarkdownComponent } from 'ngx-markdown';
import { LoadingService } from '../services/loading.service';

@Component({
  selector: 'app-bot',
  standalone: true,
  imports: [FormsModule, MarkdownComponent],
  templateUrl: './bot.component.html'
})
export class BotComponent {

  private readonly http = inject(HttpClient);
  readonly loadingService = inject(LoadingService);

  query = '';
  response = '';

  askAgent() {
    const url = `http://localhost:9999/ebank-bot/chat?query=${encodeURIComponent(this.query)}`;
    this.http.get(url, { responseType: 'text' }).subscribe((res) => (this.response = res));
  }

  askAgentStream() {
    const url = `http://localhost:9999/ebank-bot/chat-stream?query=${encodeURIComponent(this.query)}`;
    this.response = '';
    this.http
      .get(url, { responseType: 'text', observe: 'events', reportProgress: true })
      .pipe(
        map((event) => {
          switch (event.type) {
            case HttpEventType.DownloadProgress:
              return (event as HttpDownloadProgressEvent).partialText ?? '';
            case HttpEventType.Response:
              return (event as HttpResponse<string>).body ?? '';
            default:
              return '';
          }
        })
      )
      .subscribe((chunk) => (this.response += chunk));
  }
}
