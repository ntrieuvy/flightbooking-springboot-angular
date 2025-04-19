import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-language-switcher',
  templateUrl: './language-switcher.component.html',
  styleUrls: ['./language-switcher.component.scss']
})
export class LanguageSwitcherComponent {
  @Input() languages: any[] = [];
  @Input() currentLanguage!: any;
  @Output() languageChange = new EventEmitter<any>();

  onChange(event: Event): void {
    const locale = (event.target as HTMLSelectElement).value;
    const selected = this.languages.find(lang => lang.locale === locale);
    this.languageChange.emit(selected);
  }
}
