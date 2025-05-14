import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-flight-detail-modal',
  templateUrl: './flight-detail-modal.component.html',
  styleUrls: ['./flight-detail-modal.component.scss']
})
export class FlightDetailModalComponent {
  @Input() showDetailModal: boolean = false;
  @Input() selectedOption: any;
  @Output() closeModal = new EventEmitter<void>();

  onClose() {
    this.closeModal.emit();
  }

  formatTime(dateString: string): string {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  }

  formatDuration(minutes: number): string {
    if (!minutes) return '';
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return `${hours}h ${mins}m`;
  }

  formatDate(dateString: string): string {
    if (!dateString) return '';
    // Assuming date is in DDMMYYYY format
    const day = dateString.substring(0, 2);
    const month = dateString.substring(2, 4);
    const year = dateString.substring(4, 8);
    return `${day}/${month}/${year}`;
  }
}