import Vue from 'vue';
import {ApiClient} from './lib/api/ApiClient';
import {ConfigHelper} from './lib/config/ConfigHelper';

const appConfig = ConfigHelper.getAppConfig();
export const apiClient = new ApiClient(appConfig);

Vue.mixin({
    data: () => {
        return {
            apiClient,
        };
    },
});
