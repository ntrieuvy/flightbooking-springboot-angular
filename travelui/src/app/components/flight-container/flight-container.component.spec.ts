import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FlightContainerComponent } from './flight-container.component';

describe('SearchComponent', () => {
  let component: FlightContainerComponent;
  let fixture: ComponentFixture<FlightContainerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FlightContainerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FlightContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
