export class GenericListWrapper {
    phones: UserPhone[] = [];
    addresses: UserAddress[] = [];
}

export interface GenericResponse<T> {
    data: T;
    message: string;
    status: string;
}

export interface GenericResponseItems<E> {
    items: E[];
}

export interface GenericPageableResponseItems<E> {
    items: E[];
    page: number;
    hasMoreItems: boolean;
}

export class UserPhoneEditableView  {
    id: string;
    phone: UserPhone;

    constructor(phone: UserPhone) {
        this.id = Math.random().toString().slice(2);
        this.phone = phone;
    }
}

export interface UserPhone {
    countryCode: string;
    phoneNumber: string;
    primary: boolean;
}

export interface UserAddress {
    id: string;
    tag: string;
    line1: string;
    line2: string;
    landmark: string;
    codeOrPin: string;
    city: string;
    state: string;
    country: string;
    primary: boolean;
}

export interface HumaneUser {
    userId: string;
    email: string;
    username: string;
    firstName: string;
    lastName: string;
    middleName: string;
    active: boolean;
    delete: boolean;
}

export interface UserRole {
    roleId: string;
    roleCode: string;
    roleName: string;
    hierarchy: number;
    deprecate: boolean;
}

export interface UserRoleMappingView {
    userRoleId: string;
    userId: string;
    roleId: string;
    roleCode: string;
    roleName: string;
}
