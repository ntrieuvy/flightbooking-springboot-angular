import { Component, Input, Output, EventEmitter, HostListener } from '@angular/core';
import { TabConfig } from '../../../core/models/interface/tab-config';

@Component({
  selector: 'app-tab-table',
  templateUrl: './tab-table.component.html',
  styleUrls: ['./tab-table.component.scss'],
})
export class TabTableComponent {
  @Input() tabs: TabConfig[] = [];
  @Input() activeTabId: string | null = null;
  @Output() tabChange = new EventEmitter<string>();
  
  @HostListener('keydown.arrowRight') nextTab() {
    const index = this.tabs.findIndex(t => t.id === this.activeTabId);
    this.selectTab(this.tabs[(index + 1) % this.tabs.length].id);
  }

  @HostListener('keydown.arrowLeft') prevTab() {
    const index = this.tabs.findIndex(t => t.id === this.activeTabId);
    this.selectTab(this.tabs[(index - 1 + this.tabs.length) % this.tabs.length].id);
  }

  ngOnInit() {
    if (!this.activeTabId && this.tabs.length > 0) {
      this.activeTabId = this.tabs[0].id;
    }
  }

  selectTab(tabId: string) {
    const tab = this.tabs.find(t => t.id === tabId);
    if (tab && !tab.disabled) {
      this.activeTabId = tabId;
      this.tabChange.emit(tabId);
    }
  }

  trackById(index: number, tab: TabConfig) {
    return tab.id;
  }

  isString(content: any): content is string {
    return typeof content === 'string';
  }
}