// auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'YOUR_BACKEND_API_URL'; // Replace with your backend API URL
  private _isAuthenticated = new BehaviorSubject<boolean>(false);
  isAuthenticated$ = this._isAuthenticated.asObservable();

  constructor(private http: HttpClient) {
    this.checkAuthenticationStatus();
  }

  login(credentials: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, credentials).pipe(
      tap((response: any) => {
        if (response && response.token) {
          localStorage.setItem('authToken', response.token);
          this._isAuthenticated.next(true);
        }
      })
    );
  }

  logout(): void {
    localStorage.removeItem('authToken');
    this._isAuthenticated.next(false);
  }

  getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  private checkAuthenticationStatus(): void {
    const token = this.getToken();
    this._isAuthenticated.next(!!token);
  }
}
