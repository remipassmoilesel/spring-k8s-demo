import Vue from 'vue';
import {createRouter} from './router/router';
import {store, StoreW} from './store';
import {Logger} from './lib/util/Logger';
import {ConfigHelper} from './lib/config/ConfigHelper';
import BootstrapVue from 'bootstrap-vue';

import './sass/main.scss';
import './components/componentsInit';
import './mixins';

Vue.use(BootstrapVue);

const logger = new Logger('main.ts');
logger.info('Starting app with configuration: ', ConfigHelper.getAppConfig());

// tslint:disable-next-line:no-unused-expression
new Vue({
    el: '#app-main',
    store,
    router: createRouter(),
    template: require('./views/main-view.html'),
    computed: {
        title: () => {
            return StoreW.getTitle();
        },
    },
});
