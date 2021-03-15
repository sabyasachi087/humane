import { HumaneUser } from './HumaneDataModel';
export class DataFeed {

    selectedPage = HumanePage.USER;
    userTabIndex = 0;
    user = {} as HumaneUser;

    constructor() {}

}

export enum HumanePage {
    USER, ROLE, MAPPING, SETTING
}
