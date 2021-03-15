import { TestBed } from '@angular/core/testing';

import { HumaneRestService } from './humane-rest.service';

describe('HumaneRestService', () => {
  let service: HumaneRestService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HumaneRestService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
