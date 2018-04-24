import {IAppConfig} from '../config/IAppConfig';

export class ApiRoutes {
    private config: IAppConfig;

    private readonly API_APP_IDENTITY = '/app/identity';
    private readonly DOCUMENTS = '/document-management/documents';
    private readonly DOC_SIGNATURE = '/signature-management/document';

    constructor(config: IAppConfig){
        this.config = config;
    }

    public appIdentity(){
        return this.config.apiBaseUrl + this.API_APP_IDENTITY;
    }

    public documents() {
        return this.config.apiBaseUrl + this.DOCUMENTS;
    }

    public docSignature() {
        return this.config.apiBaseUrl + this.DOC_SIGNATURE;
    }
}
