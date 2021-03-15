import { HumaneInterceptor } from './common/humane-interceptor';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import {MatTabsModule} from '@angular/material/tabs';
import {MatButtonModule} from '@angular/material/button';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatCardModule} from '@angular/material/card';
import {MatSelectModule} from '@angular/material/select';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatBottomSheetModule} from '@angular/material/bottom-sheet';
import {MatTableModule} from '@angular/material/table';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatRadioModule} from '@angular/material/radio';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HeaderComponent } from './common/header/header.component';
import { UserComponent } from './user/user/user.component';
import { RoleComponent } from './role/role/role.component';
import { MappingComponent } from './mapping/mapping/mapping.component';
import { SettingComponent } from './setting/setting/setting.component';
import { CreateUserComponent } from './user/create/create-user/create-user.component';
import {environment} from './../environments/environment';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { SearchUserComponent } from './user/search/search-user/search-user.component';
import { ContactDetailComponent } from './user/contact/contact-detail/contact-detail.component';
import { UserRoleComponent } from './user/user-role/user-role.component';


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    UserComponent,
    RoleComponent,
    MappingComponent,
    SettingComponent,
    CreateUserComponent,
    SearchUserComponent,
    ContactDetailComponent,
    UserRoleComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatTabsModule,
    MatGridListModule,
    MatInputModule,
    MatFormFieldModule,
    MatCardModule,
    MatSelectModule,
    MatCheckboxModule,
    MatSnackBarModule,
    MatProgressBarModule,
    MatBottomSheetModule,
    MatTableModule,
    MatExpansionModule,
    MatRadioModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: HumaneInterceptor, multi: true},
    {provide: 'BASE_API_URL', useValue: environment.apiUrl}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
