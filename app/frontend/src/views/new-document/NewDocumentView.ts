import {Component, Vue} from 'vue-property-decorator';
import {Logger} from '../../lib/util/Logger';
import {ApiClient} from '../../lib/api/ApiClient';
import {Toaster} from '../../lib/Toaster';

import './NewDocumentView.scss';

@Component({
    template: require('./NewDocumentView.html'),
})
export class NewDocumentView extends Vue {
    protected logger: Logger = new Logger('NewDocumentView');
    protected apiClient: ApiClient;

    protected form = {
        document: '' as any,
    };

    protected onFileSelected(e: Event) {
        const inputField = (e.target as any);
        this.form.document = inputField.files[0];
    }

    protected onSubmit() {
        this.logger.info('onSubmit');

        if (!this.form.document) {
            Toaster.error('You must select a valid file');
            return;
        }

        const data = new FormData();
        data.append('document', this.form.document, this.form.document.name);

        this.apiClient.createAndSignDocument(data).then(() => {
            Toaster.info('Document sent !');
        });

    }

}
