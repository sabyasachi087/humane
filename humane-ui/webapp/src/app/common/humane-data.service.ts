import { DataFeed, HumanePage } from './../model/GlobalData';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class HumaneDataService {

  private dataFeed: DataFeed;

  constructor() {
    this.dataFeed = new DataFeed();
  }

  get feed(): DataFeed {
    return this.dataFeed;
  }

  set page(page: HumanePage) {
    this.dataFeed.selectedPage = page;
  }
}
