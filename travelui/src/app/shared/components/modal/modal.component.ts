import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss']
})
export class ModalComponent {
  @Input() visible: boolean = false;
  @Output() close = new EventEmitter<void>();

  onClose() {
    this.close.emit();
  }
}
