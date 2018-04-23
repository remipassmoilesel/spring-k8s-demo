import Vue from 'vue';
import Vuex from 'vuex';
import {IRouteConfigAugmented} from './router/IRouteConfigAugmented';

Vue.use(Vuex);

// Create a main vuex store
export const store = new Vuex.Store({
  state: {
    title: 'Bienvenue !',
    route: {} as IRouteConfigAugmented,
  },
  mutations: {
    setTitle(state: any, title: string) {
      state.title = title;
    },
    setRoute(state: any, route: IRouteConfigAugmented) {
      state.route = route;
    },
  },
  actions: {
    setTitle: (context: any, title: string) => {
      context.commit('setTitle', title);
    },
    setRoute: (context: any, route: IRouteConfigAugmented) => {
      context.commit('setRoute', route);
    },
  },
});

export class StoreW {

  public static getTitle(): string {
    return store.state.title;
  }

  public static setTitle(title: string) {
    return store.dispatch('setTitle', title);
  }

  public static getRoute(): IRouteConfigAugmented {
    return store.state.route;
  }

  public static setRoute(route: IRouteConfigAugmented) {
    return store.dispatch('setRoute', route);
  }
}
