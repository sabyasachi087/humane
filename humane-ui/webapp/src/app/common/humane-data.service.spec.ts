import { TestBed } from '@angular/core/testing';

import { HumaneDataService } from './humane-data.service';

describe('HumaneDataService', () => {
  let service: HumaneDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HumaneDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
