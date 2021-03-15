import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HumaneRestService } from './../../common/humane-rest.service';
import { HumaneDataService } from './../../common/humane-data.service';
import { UserRole } from './../../model/HumaneDataModel';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-role',
  templateUrl: './role.component.html',
  styleUrls: ['./role.component.css']
})
export class RoleComponent implements OnInit {

  inProgress = false;
  editRole = {} as UserRole;
  roleRecords = new MatTableDataSource<UserRole>();
  roleDisplayColumns = ['roleId', 'roleCode', 'roleName' , 'hierarchy', 'deprecate', 'action'];

  constructor(private snackBar: MatSnackBar, private hrs: HumaneRestService, private hds: HumaneDataService) { }

  ngOnInit(): void {
    this.loadRoles();
    this.clear();
  }

  clear(): void {
    this.editRole = {} as UserRole;
    this.editRole.hierarchy = 0;
    this.editRole.deprecate = false;
  }

  loadRoles(): void {
    this.inProgress = true;
    this.hrs.getRoles().subscribe(
      data => {
        this.roleRecords.data = data.data.items;
        this.inProgress = false;
      },
      error => {
        this.showMessage(error.message, 'ERROR', 10000);
        this.inProgress = false;
      }
    );
  }

  saveRole(): void {
    this.inProgress = true;
    if (this.validateRole()) {
      if (this.editRole.roleId && this.editRole.roleId.length > 0) {
        this.updateRole();
      } else {
        this.createRole();
      }
    } else {
      this.inProgress = false;
    }
  }

  private createRole(): void {
    this.inProgress = true;
    this.hrs.createRole(this.editRole).subscribe(
      data => {
        this.loadRoles();
      }, error => {
        this.showMessage(error.message, 'ERROR', 10000);
        this.inProgress = false;
      }
    );
  }

  private updateRole(): void {
    this.inProgress = true;
    this.hrs.updateRole(this.editRole).subscribe(
      data => {
        this.loadRoles();
      }, error => {
        this.showMessage(error.message, 'ERROR', 10000);
        this.inProgress = false;
      }
    );
  }

  validateRole(): boolean {
    if (this.editRole.roleCode && this.editRole.roleCode.length > 0) {
      if (!this.editRole.roleName || !(this.editRole.roleName.length > 0) ) {
        this.showMessage('Code is mandatory', 'VALIDATION', 5000);
      } else if (!this.editRole.hierarchy || !(this.editRole.roleName.length > 0) ) {
        this.editRole.hierarchy = 0;
        return true;
      } else if (isNaN(this.editRole.hierarchy)) {
        this.showMessage('Hierarchy must be numeric', 'VALIDATION', 5000);
      } else {
        return true;
      }
    } else {
      this.showMessage('Code is mandatory', 'VALIDATION', 5000);
    }
    return false;
  }

  deleteRow(role: UserRole): void {
    this.inProgress = true;
    this.hrs.deleteRole(role.roleId).subscribe(
      data => {
        this.loadRoles();
      }, error => {
        this.showMessage(error.message, 'ERROR', 5000);
        this.inProgress = false;
      }
    );
  }

  toggleDeprecate(role: UserRole): void {
    this.inProgress = true;
    this.hrs.toggleRoleDeprecation(role.roleId, !role.deprecate).subscribe(
      data => {
        this.loadRoles();
      }, error => {
        this.showMessage(error.message, 'ERROR', 5000);
        this.inProgress = false;
      }
    );
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.roleRecords.filter = filterValue.trim().toLowerCase();
  }

  editRow(role: UserRole): void {
    this.editRole = {} as UserRole;
    this.editRole.roleId = role.roleId;
    this.editRole.roleName = role.roleName;
    this.editRole.roleCode = role.roleCode;
    this.editRole.hierarchy = role.hierarchy;
    this.editRole.deprecate = role.deprecate;
  }

  private showMessage(message: string, type: string, timeOfDisplayInMillis: number): void {
    this.snackBar.open(message, type, {
      duration: timeOfDisplayInMillis,
      horizontalPosition: 'center',
      verticalPosition: 'top',
    });
  }

}
