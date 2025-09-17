import { Injectable } from '@angular/core';
import { of } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UserService {
  private users = Array.from({ length: 200 }, (_, i) => ({
    id: i + 1,
    name: `User ${i + 1}`,
    email: `user${i + 1}@test.com`
  }));

  getUsers(page: number, size: number) {
    const start = (page - 1) * size;
    const end = start + size;
    return of({
      users: this.users.slice(start, end),
      total: this.users.length
    });
  }
}
