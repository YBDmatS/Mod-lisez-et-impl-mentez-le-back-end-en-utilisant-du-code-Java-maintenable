import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.prod';
import { MessageRequest } from '../interfaces/api/messageRequest.interface';
import { MessageResponse } from '../interfaces/api/messageResponse.interface';

@Injectable({
  providedIn: 'root',
})
export class MessagesService {
  private readonly baseUrl = environment.baseUrl;
  private readonly pathService = `${this.baseUrl}messages`;

  constructor(private readonly httpClient: HttpClient) {}

  public send(messageRequest: MessageRequest): Observable<MessageResponse> {
    return this.httpClient.post<MessageResponse>(
      this.pathService,
      messageRequest,
    );
  }
}
