import * as _ from 'lodash';
import {IAppConfig} from './IAppConfig';

declare const window: any;

const defaultConfig: IAppConfig = {
    apiBaseUrl: '/api',
};

export class ConfigHelper {

    public static getAppConfig(){
        const windowConfig: IAppConfig = window.appConfig;
        return _.defaultsDeep({}, windowConfig, defaultConfig);
    }

}
