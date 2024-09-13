import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ParamControllerComponent } from './param-controller.component';

describe('ParamControllerComponent', () => {
  let component: ParamControllerComponent;
  let fixture: ComponentFixture<ParamControllerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ParamControllerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ParamControllerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
