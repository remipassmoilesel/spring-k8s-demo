import {Component, Vue} from 'vue-property-decorator';
import {Logger} from '../../lib/util/Logger';
import {AbstractUiComponent} from '../AbstractUiComponent';

import './CustomFooterComponent.scss';

@Component({
  template: require('./CustomFooterComponent.html'),
})
export class CustomFooterComponent extends AbstractUiComponent {
  protected logger: Logger = new Logger('CustomFooterComponent');

  public componentName: string = 'Pied de page';
  public componentDescription: string = 'Pied de page avec liens de bas de page';
  public componentTagName: string = 'custom-footer';

}
