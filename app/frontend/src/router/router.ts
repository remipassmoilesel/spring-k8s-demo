import * as _ from 'lodash';
import Vue from 'vue';
import VueRouter from 'vue-router';
import {getAllRoutes} from './routes';
import {StoreW} from '../store';
import {IRouteConfigAugmented} from './IRouteConfigAugmented';

Vue.use(VueRouter);

export const createRouter = (): VueRouter => {

  const routes: IRouteConfigAugmented[] = getAllRoutes();
  const router = new VueRouter({
    mode: 'hash',
    routes,
    scrollBehavior(to, from, savedPosition) {
      return new Promise((resolve, reject) => {
        setTimeout(() => {
          resolve({x: 0, y: 0});
        }, 400); // scroll to top for each new route
      });
    },
  });

  router.beforeEach((to, from, next) => {
    const route: IRouteConfigAugmented = _.find(routes, (rt) => rt.path === to.fullPath);
    if (route) {
      StoreW.setTitle(route.text);
      StoreW.setRoute(route);
    }

    next();
  });

  return router;
};
