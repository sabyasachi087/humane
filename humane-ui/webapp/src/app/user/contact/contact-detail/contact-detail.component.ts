import { MatSnackBar } from '@angular/material/snack-bar';
import { HumaneDataService } from './../../../common/humane-data.service';
import { HumaneRestService } from './../../../common/humane-rest.service';
import { UserPhone, UserPhoneEditableView, HumaneUser, UserAddress } from './../../../model/HumaneDataModel';
import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-contact-detail',
  templateUrl: './contact-detail.component.html',
  styleUrls: ['./contact-detail.component.css']
})
export class ContactDetailComponent implements OnInit {

  panelOpenState = true;
  userPhoneList = [] as UserPhoneEditableView[];
  phoneRecordDataSource = new MatTableDataSource<UserPhoneEditableView>();
  addressRecordDataSource = new MatTableDataSource<UserAddress>();
  phoneDisplayedCols = ['countryCode', 'phoneNumber', 'primary' , 'action'];
  addressDisplayCols = ['id', 'tag', 'line1', 'city', 'pinOrCode', 'primary', 'action'];
  isPrimaryPhone = false;
  isPhoneRecordsEditable = false;
  newUserPhone: any = null;
  selectedUser: HumaneUser;
  inProgress = false;
  editAddr = {} as UserAddress;
  userAddresses = [] as UserAddress[];

  constructor(private snackBar: MatSnackBar, private hrs: HumaneRestService, private hds: HumaneDataService) {
    this.selectedUser = this.hds.feed.user;
  }

  ngOnInit(): void {
    this.syncPhoneRecords();
    this.loadUserAddresses();
  }

  syncPhoneRecords(): void {
    this.inProgress = true;
    this.hrs.getPhones(this.hds.feed.user.userId).subscribe(
      data => {
        this.userPhoneList = [];
        data.data.items.forEach(up => {
          this.userPhoneList.push(new UserPhoneEditableView(up));
        });
        this.reloadPhoneRecords();
        this.inProgress = false;
      },
      error => {
        this.showMessage(error.message, 'ERROR', 5000);
        this.inProgress = false;
      }
    );
  }

  reloadPhoneRecords(): void {
    this.phoneRecordDataSource.data = this.userPhoneList;
  }

  loadUserAddresses(): void {
    this.inProgress = true;
    this.hrs.getAddresses(this.hds.feed.user.userId).subscribe(
      data => {
        this.userAddresses = data.data.items;
        console.log(data);
        this.inProgress = false;
        this.addressRecordDataSource.data = this.userAddresses;
      },
      error => {
        this.showMessage(error.message, 'ERROR', 5000);
        this.inProgress = false;
      }
    );
  }

  addNewPhone(): void {
    const newUserPhone = {} as UserPhone;
    newUserPhone.countryCode = '';
    newUserPhone.phoneNumber = '';
    newUserPhone.primary = false;
    this.userPhoneList.push(new UserPhoneEditableView(newUserPhone));
    this.reloadPhoneRecords();
  }

  deletePhoneRecord(row: UserPhoneEditableView): void {
    const tempPhones = [] as UserPhoneEditableView[];
    this.userPhoneList.forEach( (up) => {
      if (up.id === row.id) {
        // to delete
      } else {
        tempPhones.push(up);
      }
    });
    this.userPhoneList = tempPhones;
    this.reloadPhoneRecords();
  }

  deleteUserAddress(addr: UserAddress): void {
    this.inProgress = true;
    const addresses = [] as UserAddress[];
    this.addressRecordDataSource.data.forEach(ua => {
      if ( !(ua.id === addr.id) ) {
        addresses.push(ua);
      }
    });
    if (addresses.length > 0) {
      this.hrs.saveAddresses(this.hds.feed.user.userId,addresses).subscribe(
        data => {
          this.loadUserAddresses();
        },
        error => {
          this.showMessage(error.message, 'ERROR', 5000);
          this.inProgress = false;
        }
      );
    } else {
      this.showMessage('At least one address has to be present', 'Validation', 5000);
      this.inProgress = false;
    }
  }

  savePhoneRecords(): void {
    this.inProgress = true;
    const upsToSave = this.buildPhoneRecords();
    if (upsToSave.length > 0 && this.validatePhoneRecords(upsToSave)) {
      this.hrs.savePhones(this.selectedUser.userId, upsToSave).subscribe(
        data => {
          this.syncPhoneRecords();
        },
        error => {
          this.inProgress = false;
          this.showMessage(error.message, 'ERROR', 5000);
        }
      );
    } else if (upsToSave.length === 0) {
      this.showMessage('No phone records', 'VALIDATION', 5000);
      this.inProgress = false;
    } else {
      this.inProgress = false;
    }
  }

  saveUserAddresses(): void {
    this.inProgress = true;
    const userAdressesToSave = this.prepareUserAddresses();
    if (userAdressesToSave.length > 0) {
      this.hrs.saveAddresses(this.hds.feed.user.userId, userAdressesToSave).subscribe(
        data => {
          this.loadUserAddresses();
        },
        error => {
          this.showMessage(error.message, 'ERROR', 5000);
          this.inProgress = false;
        }
      );
    } else {
      this.inProgress = false;
    }
  }

  clearEditAddress(): void {
    this.editAddr = {} as UserAddress;
  }

  editAddressDetails(addr: UserAddress): void {
    this.editAddr = {} as UserAddress;
    this.editAddr.city = addr.city;
    this.editAddr.codeOrPin = addr.codeOrPin;
    this.editAddr.country = addr.country;
    this.editAddr.id = addr.id;
    this.editAddr.landmark = addr.landmark;
    this.editAddr.line1 = addr.line1;
    this.editAddr.line2 = addr.line2;
    this.editAddr.primary = addr.primary;
    this.editAddr.state = addr.state;
    this.editAddr.tag = addr.tag;
  }

  prepareUserAddresses(): UserAddress[] {
    const userAdressesToSave = [] as UserAddress[];
    if (this.validateUserAddress()) {
      this.userAddresses.forEach(ua => {
        if ( !(ua.id === this.editAddr.id) ) {
          userAdressesToSave.push(ua);
        }
        if (ua.primary && this.editAddr.primary) {
          ua.primary = false;
        }
      });
      if (userAdressesToSave.length === 0) {
        this.editAddr.primary = true;
      }
      userAdressesToSave.push(this.editAddr);
    }
    return userAdressesToSave;
  }

  validateUserAddress(): boolean {
    let result = false;
    if (this.editAddr.line1 && this.editAddr.line1.length > 0 ){
      if (!this.editAddr.state || !(this.editAddr.state.length > 0) ) {
        this.showMessage('State is mandatory field', 'VALIDATION', 5000);
      } else if (!this.editAddr.city || !(this.editAddr.city.length > 0) ) {
        this.showMessage('City is mandatory field', 'VALIDATION', 5000);
      }  else if (!this.editAddr.codeOrPin || !(this.editAddr.codeOrPin.length > 0) ) {
        this.showMessage('Code/Pin is mandatory field', 'VALIDATION', 5000);
      } else if (!this.editAddr.tag || !(this.editAddr.tag.length > 0) ) {
        this.showMessage('Tag/Name is mandatory field', 'VALIDATION', 5000);
      } else if (!this.editAddr.country || !(this.editAddr.country.length > 0) ) {
        this.showMessage('Country is mandatory field', 'VALIDATION', 5000);
      } else {
        result = true;
      }
    } else {
      this.showMessage('Line 1 is mandatory field', 'VALIDATION', 5000);
    }
    return result;
  }

  validatePhoneRecords(upsToSave: UserPhone[]): boolean {
    let atLeastOneIsPrimary = false;
    for (const up of upsToSave) {
      if (!up.countryCode || up.countryCode.length === 0) {
        this.showMessage('Country code is mandatory', 'VALIDATION', 5000);
        return false;
      }
      if (!up.phoneNumber || up.phoneNumber.length === 0) {
        this.showMessage('Phone Number is mandatory', 'VALIDATION', 5000);
        return false;
      }
      if (!atLeastOneIsPrimary && up.primary) {
        atLeastOneIsPrimary = true;
      }
    }
    if (!atLeastOneIsPrimary) {
      this.showMessage('At least one phone number must be primary', 'VALIDATION', 5000);
      return false;
    }
    return true;
  }

  buildPhoneRecords(): UserPhone[] {
    const upsToSave = [] as UserPhone[];
    this.userPhoneList.forEach(up => {
      const item = {}  as UserPhone;
      item.countryCode = up.phone.countryCode;
      item.phoneNumber = up.phone.phoneNumber;
      item.primary = up.phone.primary;
      upsToSave.push(item);
    });
    return upsToSave;
  }

  radiobuttonClick(row: UserPhoneEditableView): void {
    setTimeout(() => {
      this.resetAllPrimaryPhoneRecords(row);
    }, 100);
  }

  private resetAllPrimaryPhoneRecords(primary: UserPhoneEditableView): void {
    this.userPhoneList.forEach( (up) => {
      if (up.id === primary.id) {
        up.phone.primary = true;
      } else {
        up.phone.primary = false;
      }
    });
  }

  isAddPhoneRecordAllowed(): boolean {
    return this.newUserPhone == null;
  }

  editPhoneRecords(): void {
    this.isPhoneRecordsEditable = true;
  }


  private showMessage(message: string, type: string, timeOfDisplayInMillis: number): void {
    this.snackBar.open(message, type, {
      duration: timeOfDisplayInMillis,
      horizontalPosition: 'center',
      verticalPosition: 'top',
    });
  }

}
