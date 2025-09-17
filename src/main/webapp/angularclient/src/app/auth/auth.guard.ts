import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(): boolean {
    const loggedIn = !!localStorage.getItem('token'); // Mock auth
    if (!loggedIn) {
      this.router.navigate(['/login']);
      return false;
    }
    return true;
  }
}


