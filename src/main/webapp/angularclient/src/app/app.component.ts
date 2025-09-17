/*
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'angularclient - Angular Application';
}
*/


import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule],
  template: `
    <nav class="p-4 bg-gray-200">
      <a routerLink="/dashboard" class="mr-4">Dashboard</a>
      <a (click)="logout()" href="javascript:void(0)">Logout</a>
    </nav>
    <router-outlet></router-outlet>
  `
})
export class AppComponent {
  logout() {
    localStorage.removeItem('token');
    window.location.href = '/login';
  }
}
