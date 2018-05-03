import {AbstractUiComponent} from '../AbstractUiComponent';
import Component from 'vue-class-component';
import {Prop} from 'vue-property-decorator';

@Component({
    template: require('./NumberComponent.html'),
})
export class NumberComponent extends AbstractUiComponent {
    public componentName: string = 'Number';
    public componentDescription: string = 'Show a number';
    public componentTagName: string = 'number';

    @Prop()
    protected value: string;
}
