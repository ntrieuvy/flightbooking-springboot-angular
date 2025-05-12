import { Component } from '@angular/core';
import { NavItem } from '../../../../core/models/interface/nav-item';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  logoUrl: string = 'assets/images/logo.png';
  logoAlt: string = 'Logo';
  // navItems: NavItem[] = [];
  isMenuOpen: boolean = false;

  navItems: NavItem[] = [
    { label: 'FLIGHT TICKETS' , slug: 'flight' },
    { label: 'HOTELS', slug: 'hotels' },
    { label: 'VILLAS & HOMESTAYS', slug: 'villas-homestays' },
    {
      label: 'BUS & BOAT TICKETS',
      slug: 'bus-boat-tickets',
      children: [
        { label: 'BUS TICKETS', slug: 'bus-tickets' },
        { label: 'BOAT TICKETS', slug: 'boat-tickets' },
      ]
    },
    { label: 'CONTACT', slug: 'contact' },
  ];

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  } 

}