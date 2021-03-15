import { HumaneDataService } from './../humane-data.service';

import { Component, OnInit } from '@angular/core';
import { HumanePage } from 'src/app/model/GlobalData';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  private dataService: HumaneDataService;
  SelectedPage = HumanePage;
  currentPage: HumanePage = HumanePage.USER;

  constructor(private hds: HumaneDataService) {
    this.dataService = hds;
  }

  ngOnInit(): void {
  }

  navigateTo(page: HumanePage): void {
    this.dataService.page = page;
    this.currentPage = page;
  }


}
