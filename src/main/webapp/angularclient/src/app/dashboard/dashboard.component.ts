import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { CommonModule } from '@angular/common';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, MatPaginatorModule],
  template: `
    <h2 class="text-xl font-bold mb-4">User Dashboard</h2>
    <div *ngFor="let user of users" class="p-2 border-b">
      {{ user.name }} - {{ user.email }}
    </div>

    <mat-paginator
      [length]="totalUsers"
      [pageSize]="pageSize"
      (page)="onPageChange($event)"
    ></mat-paginator>
  `,
  styles: [`
    h2 { margin: 20px 0; }
    div { font-size: 16px; }
  `]
})
export class DashboardComponent implements OnInit {
  users: any[] = [];
  totalUsers = 0;
  pageSize = 20;
  currentPage = 1;

  constructor(private userService: UserService) {}

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.userService.getUsers(this.currentPage, this.pageSize)
      .subscribe(res => {
        this.users = res.users;
        this.totalUsers = res.total;
      });
  }

  onPageChange(event: PageEvent) {
    this.currentPage = event.pageIndex + 1;
    this.loadUsers();
  }
}

