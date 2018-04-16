import {Component, Vue} from 'vue-property-decorator';
import {Logger} from '../../lib/util/Logger';

import './HomeView.scss';

@Component({
  template: require('./HomeView.html'),
})
export class HomeView extends Vue {
  protected logger: Logger = new Logger('HomeView');

}
