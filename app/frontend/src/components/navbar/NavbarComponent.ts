import * as _ from 'lodash';
import {Component} from 'vue-property-decorator';
import {Logger} from '../../lib/util/Logger';
import {AbstractUiComponent} from '../AbstractUiComponent';
import {IRouteConfigAugmented} from '../../router/IRouteConfigAugmented';
import {getAllRoutes} from '../../router/routes';

import './NavbarComponent.scss';

@Component({
  template: require('./NavbarComponent.html'),
})
export class NavbarComponent extends AbstractUiComponent {
  public componentName: string = 'Navigation bar';
  public componentDescription: string = 'Top level navigation bar';
  public componentTagName: string = 'navbar';

  protected logger: Logger = new Logger('NavbarComponent');
  public routes: IRouteConfigAugmented[] = _.filter(getAllRoutes(), (rt: IRouteConfigAugmented) => !rt.hideInNavbar);

}
