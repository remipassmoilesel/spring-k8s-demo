import {Component, Vue} from 'vue-property-decorator';
import {Logger} from '../../lib/util/Logger';
import {ApiClient} from '../../lib/api/ApiClient';
import {IDocument} from '../../lib/entities/IDocument';
import {Toaster} from '../../lib/Toaster';

import './DocumentsView.scss';

@Component({
    template: require('./DocumentsView.html'),
})
export class DocumentsView extends Vue {
    protected logger: Logger = new Logger('DocumentsView');
    protected apiClient: ApiClient;

    protected documents: IDocument[] | null = null;

    protected mounted() {
        this.loadData();
    }

    protected onDelete(documentId: number) {

        this.apiClient.deleteDocument(documentId).then(() => {
            Toaster.info('Document have been deleted !');
            this.loadData();
        }).catch(() => {
            this.loadData();
        });

    }

    private loadData() {
        this.apiClient.getDocuments().then((documents: IDocument[]) => {
            this.documents = documents;
        });
    }
}
