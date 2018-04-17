import * as moment from 'moment';
import {Component, Prop} from 'vue-property-decorator';
import {Logger} from '../../lib/util/Logger';
import {AbstractUiComponent} from '../AbstractUiComponent';
import {IDocument} from '../../lib/entities/IDocument';

import './DocumentComponent.scss';

@Component({
    template: require('./DocumentComponent.html'),
})
export class DocumentComponent extends AbstractUiComponent {
    protected logger: Logger = new Logger('DocumentComponent');

    public componentName: string = 'Signed document';
    public componentDescription: string = '';
    public componentTagName: string = 'document';

    @Prop()
    private document: IDocument;

    private getReadableDate() {
        return moment(this.document.date).format('dddd, MMMM Do YYYY, h:mm:ss a');
    }

    private limitLength(value: string) {
        if (value && value.length > 100) {
            return value.substr(0, 97) + '...';
        }
        return value;
    }
}
