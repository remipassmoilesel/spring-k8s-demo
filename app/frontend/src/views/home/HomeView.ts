import {Component, Vue} from 'vue-property-decorator';
import {Logger} from '../../lib/util/Logger';
import {IAppIdentity} from '../../lib/entities/IAppIdentity';
import {ApiClient} from '../../lib/api/ApiClient';

import './HomeView.scss';

@Component({
    template: require('./HomeView.html'),
})
export class HomeView extends Vue {
    protected logger: Logger = new Logger('HomeView');
    protected apiClient: ApiClient;

    private appIdentity: null | IAppIdentity = null;

    public mounted() {
        this.apiClient.getAppIdentity().then((data: IAppIdentity) => {
            this.appIdentity = data;
        });
    }

    private limitLength(value: string) {
        if (value && value.length > 100) {
            return value.substr(0, 97) + '...';
        }
        return value;
    }
}
