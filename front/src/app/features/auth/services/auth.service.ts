import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/app/interfaces/user.interface';
import { environment } from 'src/environments/environment.prod';
import { AuthSuccess } from '../interfaces/authSuccess.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly baseUrl = environment.baseUrl;
  private readonly pathService = `${this.baseUrl}messages`;

  constructor(private readonly httpClient: HttpClient) {}

  public register(registerRequest: RegisterRequest): Observable<AuthSuccess> {
    return this.httpClient.post<AuthSuccess>(
      `${this.pathService}/register`,
      registerRequest,
    );
  }

  public login(loginRequest: LoginRequest): Observable<AuthSuccess> {
    return this.httpClient.post<AuthSuccess>(
      `${this.pathService}/login`,
      loginRequest,
    );
  }

  public me(): Observable<User> {
    return this.httpClient.get<User>(`${this.pathService}/me`);
  }
}
