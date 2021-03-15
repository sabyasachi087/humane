import { DataFeed } from './../../model/GlobalData';
import { HumaneDataService } from './../../common/humane-data.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  dataFeed: DataFeed;

  constructor(private hds: HumaneDataService) {
    this.dataFeed = hds.feed;
  }

  ngOnInit(): void {
  }

  setUserTabIndex(event: number): void {
    this.hds.feed.userTabIndex = event;
  }

}
