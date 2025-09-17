import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h2>Login</h2>
    <button (click)="login()">Login as Test User</button>
  `
})
export class LoginComponent {
  constructor(private router: Router) {}

  login() {
    localStorage.setItem('token', 'mock-token');
    this.router.navigate(['/dashboard']);
  }
}

