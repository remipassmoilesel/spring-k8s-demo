import * as _ from 'lodash';
import {Component, Vue} from 'vue-property-decorator';
import {Logger} from '../../lib/util/Logger';
import {ApiClient} from '../../lib/api/ApiClient';
import {Toaster} from '../../lib/Toaster';
import {IDocument} from '../../lib/entities/IDocument';
import {IGpgValidationResult} from '../../lib/entities/IGpgValidationResult';

import './CheckDocumentView.scss';

interface IOption {
    value: string;
    text: string;
}

@Component({
    template: require('./CheckDocumentView.html'),
})
export class CheckDocumentView extends Vue {
    protected logger: Logger = new Logger('CheckDocumentView');
    protected apiClient: ApiClient;

    protected form = {
        document: '' as any,
        originalDocument: 0,
    };

    private validationResult: IGpgValidationResult | null = null;
    private documents: IDocument[] | null = null;
    private options: IOption[] = [];

    protected mounted() {
        this.loadData();
    }

    protected onSubmit() {
        this.logger.info('onSubmit');

        if (!this.form.document) {
            Toaster.error('You must select a valid file');
            return;
        }

        const data = new FormData();
        data.append('candidate', this.form.document, this.form.document.name);
        data.append('documentId', String(this.form.originalDocument));

        this.apiClient.verifyDocument(data).then((result: IGpgValidationResult) => {
            this.validationResult = result;
        });

    }

    private loadData() {
        this.apiClient.getDocuments().then((documents) => {
            this.documents = documents;
            this.documentsToOptions();
        });
    }

    private documentsToOptions() {
        this.options = _.map(this.documents, (doc: IDocument) => {
            return {value: doc.id, text: doc.name};
        });
    }
}
