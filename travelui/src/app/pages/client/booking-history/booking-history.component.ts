import { Component, OnInit } from '@angular/core';
import { OrderService } from 'src/app/core/services/order.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { Order, OrderDetail } from 'src/app/core/models/interface/order';
import { Passenger } from 'src/app/shared/models/passenger.model';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-booking-history',
  templateUrl: './booking-history.component.html',
  styleUrls: ['./booking-history.component.scss'],
  providers: [DatePipe]
})
export class BookingHistoryComponent implements OnInit {
  isLoading = true;
  orders: Order[] = [];
  selectedOrder: Order | null = null;
  expandedOrderId: number | null = null;

  constructor(
    private orderService: OrderService,
    private authService: AuthService,
    private datePipe: DatePipe
  ) { }

  ngOnInit(): void {
    this.loadBookingHistory();
  }

  loadBookingHistory(): void {
    this.isLoading = true;

    this.orderService.getUserOrders().subscribe({
      next: (orders) => {
        this.orders = orders;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Failed to load booking history', err);
        this.isLoading = false;
      }
    });
  }

  toggleOrderDetails(orderId: number): void {
    if (this.expandedOrderId === orderId) {
      this.expandedOrderId = null;
      this.selectedOrder = null;
    } else {
      this.expandedOrderId = orderId;
      this.selectedOrder = this.orders.find(o => o.id === orderId) || null;
    }
  }

  getOrderStatus(statusCode: number): string {
    const statusMap: { [key: number]: string } = {
      1: 'Confirmed',
      2: 'Pending',
      3: 'Cancelled',
      4: 'Completed'
    };
    return statusMap[statusCode] || 'Unknown';
  }

  calculateTotalPrice(orderDetail: OrderDetail): number {
    return orderDetail.totalPrice || 0;
  }

  getPassengerCount(passengers: Passenger[]): { adults: number, children: number, infants: number } {
    return {
      adults: passengers.filter(p => p.type === 'ADT').length,
      children: passengers.filter(p => p.type === 'CHD').length,
      infants: passengers.filter(p => p.type === 'INF').length
    };
  }

  calculateTotalOrderPrice(order: Order): number {
    return order.orderDetails.reduce((total, detail) => total + detail.totalPrice, 0);
  }

  hasTickets(order: Order): boolean {
    return order.orderDetails.some(detail => detail.tickets && detail.tickets.length > 0);
  }
}