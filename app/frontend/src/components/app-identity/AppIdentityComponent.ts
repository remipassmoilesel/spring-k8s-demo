import {Component} from 'vue-property-decorator';
import {Logger} from '../../lib/util/Logger';
import {AbstractUiComponent} from '../AbstractUiComponent';
import {IAppIdentity} from '../../lib/entities/IAppIdentity';

import './AppIdentityComponent.scss';

@Component({
    template: require('./AppIdentityComponent.html'),
})
export class AppIdentityComponent extends AbstractUiComponent {
    protected logger: Logger = new Logger('AppIdentityComponent');

    public componentName: string = "Identité de l'application";
    public componentDescription: string = '';
    public componentTagName: string = 'app-identity';

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
