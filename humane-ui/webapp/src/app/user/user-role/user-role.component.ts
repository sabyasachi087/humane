import { MatSnackBar } from '@angular/material/snack-bar';
import { HumaneUser, UserRole, UserRoleMappingView } from './../../model/HumaneDataModel';
import { HumaneRestService } from './../../common/humane-rest.service';
import { HumaneDataService } from './../../common/humane-data.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-user-role',
  templateUrl: './user-role.component.html',
  styleUrls: ['./user-role.component.css']
})
export class UserRoleComponent implements OnInit {

  user: HumaneUser;
  inProgress = false;
  roles = [] as UserRole[];
  selectedRole = {}  as UserRole;
  userRoleRecords = [] as UserRoleMappingView[];
  displayedColumns = ['userRoleId', 'roleId', 'roleCode', 'roleName' , 'action'];

  constructor(private snackBar: MatSnackBar, private hds: HumaneDataService, private hrs: HumaneRestService) {
    this.user = hds.feed.user;
  }

  ngOnInit(): void {
    this.loadRoles();
    this.loadMappings();
  }

  private loadRoles(): void {
    this.inProgress = true;
    this.hrs.getRoles().subscribe(
      data => {
        this.roles = data.data.items;
        this.inProgress = false;
      },
      error => {
        this.showMessage(error.message, 'ERROR', 10000);
        this.inProgress = false;
      }
    );
  }

  private loadMappings(): void {
    this.inProgress = true;
    this.hrs.getUserRoleMapping(this.user.userId).subscribe(
      data => {
        this.userRoleRecords = data.data.items;
        this.inProgress = false;
      },
      error => {
        this.showMessage(error.message, 'ERROR', 10000);
        this.inProgress = false;
      }
    );

  }

  private showMessage(message: string, type: string, timeOfDisplayInMillis: number): void {
    this.snackBar.open(message, type, {
      duration: timeOfDisplayInMillis,
      horizontalPosition: 'center',
      verticalPosition: 'top',
    });
  }

  deleteRow(row: UserRoleMappingView): void {
    this.inProgress = true;
    this.hrs.deleteUserRole(this.user.userId, row.roleId).subscribe(
      data => {
        this.loadMappings();
        this.inProgress = false;
      },
      error => {
        this.showMessage(error.message, 'ERROR', 10000);
        this.inProgress = false;
      }
    );
  }

  addRole(): void {
    if (this.selectedRole && this.selectedRole.roleId && this.selectedRole.roleId.length > 0) {
      if (this.user && this.user.userId && this.user.userId.length > 0) {
        this.inProgress = true;
        this.hrs.createUserRole(this.user.userId, this.selectedRole.roleId).subscribe(
          data => {
               this.loadMappings();
               this.inProgress = false;
          },
          error => {
            this.showMessage(error.message, 'ERROR', 10000);
            this.inProgress = false;
          }
        );
      } else {
        this.showMessage('Invalid User selected', 'VALIDATION', 5000);
      }
    } else {
      this.showMessage('Invalid Role selected', 'VALIDATION', 5000);
    }
  }

}
