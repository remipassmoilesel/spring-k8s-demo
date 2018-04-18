import * as moment from 'moment';
import {Component, Prop} from 'vue-property-decorator';
import {Logger} from '../../lib/util/Logger';
import {AbstractUiComponent} from '../AbstractUiComponent';
import {IGpgValidationResult} from '../../lib/entities/IGpgValidationResult';

import './ValidationResultComponent.scss';

@Component({
    template: require('./ValidationResultComponent.html'),
})
export class ValidationResultComponent extends AbstractUiComponent {
    protected logger: Logger = new Logger('ValidationResultComponent');

    public componentName: string = 'Résultat de validation';
    public componentDescription: string = '';
    public componentTagName: string = 'validation-result';

    @Prop()
    private result: IGpgValidationResult;

    private getReadableDate() {
        return moment(this.result.document.date).format('dddd, MMMM Do YYYY, h:mm:ss a');
    }

    private limitLength(value: string) {
        if (value && value.length > 100) {
            return value.substr(0, 97) + '...';
        }
        return value;
    }
}
