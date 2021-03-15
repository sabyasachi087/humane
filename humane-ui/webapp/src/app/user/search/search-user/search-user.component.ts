import { DataFeed } from './../../../model/GlobalData';
import { HumaneDataService } from './../../../common/humane-data.service';
import { HumaneUser } from './../../../model/HumaneDataModel';
import { HumaneRestService } from './../../../common/humane-rest.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';

@Component({
  selector: 'app-search-user',
  templateUrl: './search-user.component.html',
  styleUrls: ['./search-user.component.css']
})
export class SearchUserComponent implements OnInit {

  searchText = '';
  inProgress = false;
  isActive = true;
  isDelete = false;
  pageNumber = 0;
  userResultList: HumaneUser[] = [];
  feed: DataFeed;
  displayedColumns = ['userId', 'email', 'active', 'delete' , 'action'];

  @ViewChild("searchBox")
  set setSearchBox(elem: ElementRef) {
    setTimeout(() => {
      elem.nativeElement.focus();
    }, 100);
  }


  constructor(private snackBar: MatSnackBar, private restService: HumaneRestService, private hds: HumaneDataService) {
    this.feed = hds.feed;
  }

  ngOnInit(): void {
  }

  findUser(): void {
    if (this.searchText.length >= 4) {
      this.inProgress = true;
      this.restService.findUser(this.searchText, this.isActive, this.isDelete, this.pageNumber).subscribe(
        data => {
          // console.log(data);
          this.userResultList = data.data.items;
          this.inProgress = false;
        },
        error => {
          this.showMessage(error.message, 'ERROR', 10000);
          this.inProgress = false;
        }
      );
    }else {
      this.showMessage('Search text should be more than 4 charachters', 'ERROR', 10000);
    }
  }

  editUser(user: HumaneUser): void {
    this.feed.user = user;
    // for create or edit user tab
    this.feed.userTabIndex = 1;
  }

  editContact(user: HumaneUser): void {
    this.feed.user = user;
    // for create or edit user tab
    this.feed.userTabIndex = 2;
  }

  editRoles(user: HumaneUser): void {
    this.feed.user = user;
    // for create or edit user tab
    this.feed.userTabIndex = 3;
  }

  private showMessage(message: string, type: string, timeOfDisplayInMillis: number): void {
    this.snackBar.open(message, type, {
      duration: timeOfDisplayInMillis,
      horizontalPosition: 'center',
      verticalPosition: 'top',
    });
  }

}
