import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from '../model/user';
import { Observable } from 'rxjs/Observable';

@Injectable({
  providedIn: 'root'
})
export class UserServiceService {

  private usersUrl: string;

  constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:8080/users';
  }

  public findAll(): Observable<User[]> {
    return this.http.get<User[]>(this.usersUrl);
  }
  
  // Transform the response to get only the needed data
  getUserNames(): Observable<string[]> {
    return this.http.get<User[]>('/api/users').pipe(
      map((users: User[]) => users.map(user => user.name)) // Extract just the names
    );
  }  

  public save(user: User) {
    return this.http.post<User>(this.usersUrl, user);
  }
}
