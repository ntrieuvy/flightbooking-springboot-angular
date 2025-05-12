import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { gigs } from 'src/app/core/utils/lib/mockData';

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.scss']
})
export class IndexComponent implements OnInit {
  featuredGigs = gigs;
  popularDestinations = [
    {
      name: 'Bali',
      experiences: 125,
      image: 'https://images.unsplash.com/photo-1589308078059-be1415eab4c3?auto=format&fit=crop&w=500&h=500'
    },
    {
      name: 'Paris',
      experiences: 98,
      image: 'https://images.unsplash.com/photo-1523906834658-6e24ef2386f9?auto=format&fit=crop&w=500&h=500'
    },
    {
      name: 'Tokyo',
      experiences: 72,
      image: 'https://images.unsplash.com/photo-1533106497176-45ae19e68ba2?auto=format&fit=crop&w=500&h=500'
    },
    {
      name: 'Santorini',
      experiences: 56,
      image: 'https://images.unsplash.com/photo-1517935706615-2717063c2225?auto=format&fit=crop&w=500&h=500'
    }
  ];

  categories = [
    {
      name: 'Adventure',
      description: 'Thrilling experiences',
      iconPath: 'M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2V9z M9 22V12h6v10'
    },
    {
      name: 'Cultural',
      description: 'Immersive local experiences',
      iconPath: 'M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z'
    },
    {
      name: 'Food & Drink',
      description: 'Culinary discoveries',
      iconPath: 'M21 15.546c-.523 0-1.046.151-1.5.454a2.704 2.704 0 01-3 0 2.704 2.704 0 00-3 0 2.704 2.704 0 01-3 0 2.704 2.704 0 00-3 0 2.704 2.704 0 01-3 0 2.701 2.701 0 00-1.5-.454M9 6v2m3-2v2m3-2v2M9 3h.01M12 3h.01M15 3h.01M21 21v-7a2 2 0 00-2-2H5a2 2 0 00-2 2v7h18z'
    },
    {
      name: 'Wellness',
      description: 'Relaxing retreats',
      iconPath: 'M5 3v4M3 5h4M6 17v4m-2-2h4m5-16l2.286 6.857L21 12l-5.714 2.143L13 21l-2.286-6.857L5 12l5.714-2.143L13 3z'
    }
  ];

  testimonials = [
    {
      text: '"I had an amazing experience with the Safari Adventure. Our guide was extremely knowledgeable and we saw all the animals we hoped to see. Worth every penny!"',
      author: '- Sarah Thompson'
    },
    {
      text: '"The Cultural Tour of Kyoto was exceptional. The local knowledge provided by our guide gave us insights we would never have discovered on our own."',
      author: '- David Chen'
    },
    {
      text: '"Booking through TravelGigs made our Bali vacation stress-free. The Premium package had the perfect balance of activities and relaxation time."',
      author: '- Jessica Williams'
    }
  ];

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  handleSearch(event: { formData: any }) {

    this.router.navigate(['/flight'], {
      queryParams: {
        from: event.formData.from,
        to: event.formData.to,
        departureDate: event.formData.departDate,
        returnDate: event.formData.returnDate,
        adults: event.formData.adults,
        children: event.formData.children,
        infant: event.formData.infant
      }
    });
  }

  navigate(path: string): void {
    this.router.navigate([path]);
  }
}