import { HumaneDataService } from './../../../common/humane-data.service';
import { DataFeed } from './../../../model/GlobalData';
import { HumaneRestService } from './../../../common/humane-rest.service';
import { HumaneUser } from './../../../model/HumaneDataModel';
import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent implements OnInit {

  user = {} as HumaneUser;
  regexp = new RegExp(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/);
  inProgress = false;
  feed: DataFeed;

  constructor(private snackBar: MatSnackBar, private restService: HumaneRestService, private hds: HumaneDataService) {
    this.feed = hds.feed;
  }

  ngOnInit(): void {
    if (this.feed.user && this.feed.user.userId && this.feed.user.userId.length > 0) {
      this.user = this.feed.user;
    }
  }

  createOrUpdateUser(): void {
    if (this.user.userId && this.user.userId.length > 0) {
      this.updateUser();
    } else {
      this.createUser();
    }
  }

  clear(): void {
    this.user = {} as HumaneUser;
    this.user.active = true;
  }

  updateUser(): void {
    if (this.validateUser()) {
      this.inProgress = true;
      this.restService.updateUser(this.user).subscribe(
        data => {
          this.showMessage('User Updated', 'Successfully', 10000);
          this.inProgress = false;
        },
        error => {
          if (error.message){
            this.showMessage(error.message, 'Error', 10000);
          }
          this.inProgress = false;
        }
      );
    }
  }

  createUser(): void {
    if (this.validateUser()) {
      this.inProgress = true;
      this.restService.createUser(this.user).subscribe(
        data => {
          this.user.userId = data.data;
          this.showMessage('User created', 'Success', 10000);
          this.inProgress = false;
        },
        error => {
          if (error.message){
            this.showMessage(error.message, 'Error', 10000);
          }
          this.inProgress = false;
        }
      );
    }
  }

  private validateUser(): boolean {
    if ( this.regexp.test(this.user.email) ) {
      if (!(this.user.firstName.length > 0 && this.user.lastName.length > 0)) {
        this.showMessage('First and Last name is mandatory', 'Error', 10000);
      } else {
        return true;
      }
    } else {
      this.showMessage('Invalid email', 'Error', 10000);
    }
    return false;
  }

  private showMessage(message: string, type: string, timeOfDisplayInMillis: number): void {
    this.snackBar.open(message, type, {
      duration: timeOfDisplayInMillis,
      horizontalPosition: 'center',
      verticalPosition: 'top',
    });
  }

}
