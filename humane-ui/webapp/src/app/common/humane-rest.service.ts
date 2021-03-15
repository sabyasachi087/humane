import { HumaneUser, GenericResponse, GenericPageableResponseItems, GenericResponseItems, UserPhone, UserAddress, GenericListWrapper, UserRole, UserRoleMappingView } from './../model/HumaneDataModel';
import { Observable } from 'rxjs';
import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class HumaneRestService {

  constructor(@Inject('BASE_API_URL') private baseUrl: string, private httpClient: HttpClient) { }

  public createUser(user: HumaneUser): Observable<GenericResponse<string>> {
    const url = this.baseUrl + 'humane/api/v1/user';
    const options = {
      params: new HttpParams().set('responseType', 'json')
    };
    return this.httpClient.post<GenericResponse<string>>(url, user);
  }

  public updateUser(user: HumaneUser): Observable<GenericResponse<string>> {
    const url = this.baseUrl + 'humane/api/v1/user';
    const options = {
      params: new HttpParams().set('responseType', 'json')
    };
    return this.httpClient.put<GenericResponse<string>>(url, user);
  }

  public findUser(name: string, active: boolean, isDelete: boolean, page: number):
              Observable<GenericResponse<GenericPageableResponseItems<HumaneUser>>> {
    const url = this.baseUrl + 'humane/api/v1/user/all/' + page;
    const options = {
      params: new HttpParams().set('name', name).set('active', '' + active).set('delete', '' + isDelete)
    };
    return this.httpClient.get<GenericResponse<GenericPageableResponseItems<HumaneUser>>>(url, options);
  }

  public getPhones(userId: string): Observable<GenericResponse<GenericResponseItems<UserPhone>>> {
    const url = this.baseUrl + 'humane/api/v1/user/phone/' + userId;
    return this.httpClient.get<GenericResponse<GenericResponseItems<UserPhone>>>(url);
  }

  public getAddresses(userId: string): Observable<GenericResponse<GenericResponseItems<UserAddress>>> {
    const url = this.baseUrl + 'humane/api/v1/user/address/' + userId;
    return this.httpClient.get<GenericResponse<GenericResponseItems<UserAddress>>>(url);
  }

  public savePhones(userId: string, phones: UserPhone[]): Observable<GenericResponse<string>> {
    const url = this.baseUrl + 'humane/api/v1/user/phones/' + userId;
    const request = new GenericListWrapper();
    request.phones = phones;
    return this.httpClient.put<GenericResponse<string>>(url, request);
  }

  public saveAddresses(userId: string, addresses: UserAddress[]): Observable<GenericResponse<string>> {
    const url = this.baseUrl + 'humane/api/v1/user/addresses/' + userId;
    const request = new GenericListWrapper();
    request.addresses = addresses;
    return this.httpClient.put<GenericResponse<string>>(url, request);
  }

  public createRole(role: UserRole): Observable<GenericResponse<string>> {
    const url = this.baseUrl + 'humane/api/v1/role';
    const options = {
      params: new HttpParams().set('responseType', 'json')
    };
    return this.httpClient.post<GenericResponse<string>>(url, role);
  }

  public updateRole(role: UserRole): Observable<GenericResponse<string>> {
    const url = this.baseUrl + 'humane/api/v1/role/id/' + role.roleId + '/name/' +
        role.roleName + '/hierarchy/' + role.hierarchy;
    const options = {
      params: new HttpParams().set('responseType', 'json')
    };
    return this.httpClient.put<GenericResponse<string>>(url, null);
  }

  public toggleRoleDeprecation(roleId: string , deprecate: boolean): Observable<GenericResponse<string>> {
    const url = this.baseUrl + 'humane/api/v1/role/id/' + roleId + '/deprecate/' + deprecate;
    const options = {
      params: new HttpParams().set('responseType', 'json')
    };
    return this.httpClient.put<GenericResponse<string>>(url, null);
  }

  public getRoles(): Observable<GenericResponse<GenericResponseItems<UserRole>>> {
    const url = this.baseUrl + 'humane/api/v1/role';
    return this.httpClient.get<GenericResponse<GenericResponseItems<UserRole>>>(url);
  }

  public deleteRole(roleId: string): Observable<GenericResponse<string>> {
    const url = this.baseUrl + 'humane/api/v1/role/id/' + roleId;
    return this.httpClient.delete<GenericResponse<string>>(url);
  }

  public createUserRole(userId: string, roleId: string): Observable<GenericResponse<string>> {
    const url = this.baseUrl + 'humane/api/v1/userrolemap' + '/userId/' + userId + '/roleId/' + roleId;
    const options = {
      params: new HttpParams().set('responseType', 'json')
    };
    return this.httpClient.post<GenericResponse<string>>(url, null);
  }

  public getUserRoleMapping(userId: string): Observable<GenericResponse<GenericResponseItems<UserRoleMappingView>>> {
    const url = this.baseUrl + 'humane/api/v1/userrolemap' + '/view/userId/' + userId ;
    return this.httpClient.get<GenericResponse<GenericResponseItems<UserRoleMappingView>>>(url);
  }

  public deleteUserRole(userId: string, roleId: string): Observable<GenericResponse<string>> {
    const url = this.baseUrl + 'humane/api/v1/userrolemap' + '/userId/' + userId + '/roleId/' + roleId;
    return this.httpClient.delete<GenericResponse<string>>(url);
  }



}
