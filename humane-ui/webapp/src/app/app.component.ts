import { HumanePage } from 'src/app/model/GlobalData';
import { HumaneDataService } from './common/humane-data.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Humane';
  private dataService: HumaneDataService;
  HumanePageType = HumanePage;

  constructor(private hds: HumaneDataService) {
    this.dataService = hds;
  }

  get selectedPage(): HumanePage {
    return this.dataService.feed.selectedPage;
  }
}
