import {RouteConfig} from 'vue-router';

export interface IRouteConfigAugmented extends RouteConfig {
  hideInNavbar?: boolean;
  path: string;
  name?: string;
  text: string;
  description: string;
  component: any;
}
