
export interface Review {
  id: string;
  userId: string;
  username: string;
  rating: number;
  comment: string;
  date: string;
}

export interface Package {
  id: string;
  name: string;
  description: string;
  price: number;
  features: string[];
}

export interface Gig {
  id: string;
  title: string;
  location: string;
  description: string;
  imageUrl: string;
  rating: number;
  reviewCount: number;
  duration: string;
  packages: Package[];
  reviews: Review[];
}

export const gigs: Gig[] = [
  {
    id: "g1",
    title: "Beach Paradise Getaway in Bali",
    location: "Bali, Indonesia",
    description: "Experience the ultimate beach paradise in Bali with crystal clear waters, golden sands, and luxurious accommodation. This all-inclusive package includes exciting activities like snorkeling, surfing lessons, and a traditional Balinese dinner.",
    imageUrl: "https://images.unsplash.com/photo-1537996194471-e657df975ab4?auto=format&fit=crop&w=800&h=500",
    rating: 4.7,
    reviewCount: 128,
    duration: "7 days",
    packages: [
      {
        id: "p1-1",
        name: "Economy",
        description: "Basic package with standard amenities",
        price: 599,
        features: ["3-star hotel accommodation", "Airport transfer", "Daily breakfast", "1 guided tour"]
      },
      {
        id: "p1-2",
        name: "Premium",
        description: "Enhanced experience with additional perks",
        price: 899,
        features: ["4-star resort accommodation", "Airport transfer", "All meals included", "3 guided tours", "Spa treatment"]
      },
      {
        id: "p1-3",
        name: "Luxury",
        description: "Exclusive experience with premium offerings",
        price: 1499,
        features: ["5-star luxury villa", "Private airport transfer", "All inclusive meals & drinks", "Private tours", "Daily spa treatment", "Personal concierge"]
      }
    ],
    reviews: [
      {
        id: "r1-1",
        userId: "u1",
        username: "TravelLover",
        rating: 5,
        comment: "The best vacation I've ever had! The beaches were amazing and the staff was incredibly friendly.",
        date: "2023-04-15"
      },
      {
        id: "r1-2",
        userId: "u2",
        username: "WanderlustQueen",
        rating: 4,
        comment: "Great experience overall. The premium package was worth every penny!",
        date: "2023-03-22"
      },
      {
        id: "r1-3",
        userId: "u3",
        username: "GlobeTrotter",
        rating: 5,
        comment: "Absolutely magical experience. Will definitely be booking another trip soon!",
        date: "2023-02-10"
      }
    ]
  },
  {
    id: "g2",
    title: "Safari Adventure in Serengeti",
    location: "Serengeti, Tanzania",
    description: "Embark on an unforgettable safari adventure in the heart of Serengeti National Park. Witness the majestic wildlife in their natural habitat and experience the breathtaking landscapes of Africa.",
    imageUrl: "https://images.unsplash.com/photo-1516426122078-c23e76319801?auto=format&fit=crop&w=800&h=500",
    rating: 4.9,
    reviewCount: 87,
    duration: "5 days",
    packages: [
      {
        id: "p2-1",
        name: "Explorer",
        description: "Essential safari experience",
        price: 799,
        features: ["Shared safari vehicle", "Tented camp accommodation", "Meals included", "2 game drives daily"]
      },
      {
        id: "p2-2",
        name: "Adventurer",
        description: "Enhanced safari with premium viewing",
        price: 1299,
        features: ["Semi-private safari vehicle", "Luxury tented camp", "All meals included", "3 game drives daily", "Bush dinner experience"]
      },
      {
        id: "p2-3",
        name: "Ultimate",
        description: "Exclusive safari experience",
        price: 2499,
        features: ["Private safari vehicle", "Luxury lodge accommodation", "Gourmet meals", "Unlimited game drives", "Hot air balloon safari", "Private guide"]
      }
    ],
    reviews: [
      {
        id: "r2-1",
        userId: "u4",
        username: "SafariEnthusiast",
        rating: 5,
        comment: "Saw the big five on our first day! Our guide was extremely knowledgeable and made the experience unforgettable.",
        date: "2023-05-18"
      },
      {
        id: "r2-2",
        userId: "u5",
        username: "AdventureSeeker",
        rating: 5,
        comment: "Worth every penny! The Ultimate package really delivered with the hot air balloon safari.",
        date: "2023-04-30"
      }
    ]
  },
  {
    id: "g3",
    title: "Cultural Tour of Kyoto",
    location: "Kyoto, Japan",
    description: "Immerse yourself in the rich history and culture of Kyoto. Visit ancient temples, participate in a traditional tea ceremony, and explore the beautiful gardens and architecture of this historic city.",
    imageUrl: "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?auto=format&fit=crop&w=800&h=500",
    rating: 4.5,
    reviewCount: 112,
    duration: "6 days",
    packages: [
      {
        id: "p3-1",
        name: "Cultural",
        description: "Essential cultural experience",
        price: 699,
        features: ["3-star traditional inn", "Daily breakfast", "Public transportation pass", "Guided temple tour"]
      },
      {
        id: "p3-2",
        name: "Heritage",
        description: "Enhanced cultural immersion",
        price: 999,
        features: ["4-star ryokan stay", "Daily breakfast and dinner", "Private tea ceremony", "Kimono rental experience", "Guided tours to major sites"]
      },
      {
        id: "p3-3",
        name: "Imperial",
        description: "Premium experience with exclusive access",
        price: 1699,
        features: ["Luxury ryokan with private onsen", "All meals included", "Private guide for all days", "Exclusive temple access", "Traditional arts workshops", "Gourmet kaiseki dinner"]
      }
    ],
    reviews: [
      {
        id: "r3-1",
        userId: "u6",
        username: "JapanExplorer",
        rating: 4,
        comment: "Beautiful temples and gardens. The Heritage package was perfect for our needs.",
        date: "2023-03-12"
      },
      {
        id: "r3-2",
        userId: "u7",
        username: "CultureVulture",
        rating: 5,
        comment: "The Imperial package was incredible! Having exclusive access to certain temples made all the difference.",
        date: "2023-02-27"
      },
      {
        id: "r3-3",
        userId: "u8",
        username: "ZenTraveler",
        rating: 4,
        comment: "A truly authentic experience. The ryokan stay was a highlight!",
        date: "2023-01-19"
      }
    ]
  },
  {
    id: "g4",
    title: "Northern Lights Expedition in Iceland",
    location: "Reykjavik, Iceland",
    description: "Chase the magical Northern Lights in the pristine landscapes of Iceland. This expedition includes glacier hikes, hot spring visits, and optimal aurora viewing opportunities.",
    imageUrl: "https://images.unsplash.com/photo-1579033385971-a7bc8c5f7417?auto=format&fit=crop&w=800&h=500",
    rating: 4.8,
    reviewCount: 94,
    duration: "4 days",
    packages: [
      {
        id: "p4-1",
        name: "Aurora",
        description: "Essential Northern Lights experience",
        price: 899,
        features: ["3-star hotel accommodation", "Daily breakfast", "Northern Lights tour", "Golden Circle day trip"]
      },
      {
        id: "p4-2",
        name: "Arctic",
        description: "Enhanced experience with additional activities",
        price: 1399,
        features: ["4-star hotel accommodation", "Daily breakfast and dinner", "Private Northern Lights tour", "Golden Circle and South Coast tours", "Blue Lagoon entry"]
      },
      {
        id: "p4-3",
        name: "Expedition",
        description: "Ultimate Iceland experience",
        price: 2299,
        features: ["Luxury accommodation", "All meals included", "Helicopter Northern Lights tour", "Super Jeep glacier expedition", "Private lagoon experience", "Snowmobile adventure"]
      }
    ],
    reviews: [
      {
        id: "r4-1",
        userId: "u9",
        username: "ArcticExplorer",
        rating: 5,
        comment: "We saw the Northern Lights on our second night! Absolutely breathtaking experience.",
        date: "2023-05-05"
      },
      {
        id: "r4-2",
        userId: "u10",
        username: "IceQueen",
        rating: 4,
        comment: "The Expedition package was incredible, especially the helicopter tour.",
        date: "2023-04-11"
      }
    ]
  },
  {
    id: "g5",
    title: "Vineyard Tour in Tuscany",
    location: "Florence, Italy",
    description: "Explore the rolling hills and vineyards of Tuscany. Sample world-class wines, learn about traditional winemaking, and enjoy the delicious local cuisine.",
    imageUrl: "https://images.unsplash.com/photo-1543361136-75641bf9f630?auto=format&fit=crop&w=800&h=500",
    rating: 4.6,
    reviewCount: 76,
    duration: "5 days",
    packages: [
      {
        id: "p5-1",
        name: "Taste",
        description: "Essential wine tasting experience",
        price: 599,
        features: ["3-star accommodation in Florence", "Daily breakfast", "2 winery tours with tastings", "Cooking class"]
      },
      {
        id: "p5-2",
        name: "Vintage",
        description: "Enhanced wine and food experience",
        price: 999,
        features: ["4-star country villa accommodation", "Daily breakfast and select dinners", "4 premium winery tours", "Cooking class with market visit", "Cheese and olive oil tastings"]
      },
      {
        id: "p5-3",
        name: "Grand Cru",
        description: "Luxury wine connoisseur experience",
        price: 1899,
        features: ["Luxury villa accommodation", "All gourmet meals included", "Private tours at exclusive wineries", "VIP vertical wine tastings", "Private chef dinner", "Truffle hunting experience"]
      }
    ],
    reviews: [
      {
        id: "r5-1",
        userId: "u11",
        username: "WineLover",
        rating: 5,
        comment: "The Grand Cru package was worth every penny! We had access to wineries that aren't open to the general public.",
        date: "2023-06-02"
      },
      {
        id: "r5-2",
        userId: "u12",
        username: "FoodieExplorer",
        rating: 4,
        comment: "Great wine, great food, and beautiful scenery. What more could you ask for?",
        date: "2023-05-17"
      }
    ]
  },
  {
    id: "g6",
    title: "Ancient Wonders of Egypt",
    location: "Cairo, Egypt",
    description: "Journey through time to discover the ancient wonders of Egypt. Explore the Pyramids of Giza, the Sphinx, and cruise along the Nile River to visit temples and tombs.",
    imageUrl: "https://images.unsplash.com/photo-1503177119275-0aa32b3a9368?auto=format&fit=crop&w=800&h=500",
    rating: 4.7,
    reviewCount: 105,
    duration: "8 days",
    packages: [
      {
        id: "p6-1",
        name: "Pharaoh",
        description: "Essential Egyptian experience",
        price: 899,
        features: ["3-star hotel accommodation", "Daily breakfast", "Pyramids and Sphinx tour", "Egyptian Museum visit"]
      },
      {
        id: "p6-2",
        name: "Nile",
        description: "Enhanced Egyptian adventure with cruise",
        price: 1499,
        features: ["4-star hotels and Nile cruise", "All meals on cruise, breakfast at hotels", "All major historical sites", "Valley of the Kings", "Abu Simbel day trip"]
      },
      {
        id: "p6-3",
        name: "Royal",
        description: "Luxury Egyptian exploration",
        price: 2799,
        features: ["5-star luxury accommodation", "All meals included", "Private guides", "Exclusive access to sites", "Hot air balloon over Luxor", "Private felucca sail", "Gourmet dining experiences"]
      }
    ],
    reviews: [
      {
        id: "r6-1",
        userId: "u13",
        username: "HistoryBuff",
        rating: 5,
        comment: "The Royal package gave us incredible access to archaeological sites. Our Egyptologist guide was phenomenal!",
        date: "2023-04-28"
      },
      {
        id: "r6-2",
        userId: "u14",
        username: "PyramidSeeker",
        rating: 4,
        comment: "The Nile cruise was a highlight. Seeing the temples from the river was magical.",
        date: "2023-03-15"
      },
      {
        id: "r6-3",
        userId: "u15",
        username: "AncientExplorer",
        rating: 5,
        comment: "Worth every penny for the exclusive access to tombs not open to the public.",
        date: "2023-02-22"
      }
    ]
  }
];
