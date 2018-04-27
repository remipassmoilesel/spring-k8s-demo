import axios, {AxiosRequestConfig} from 'axios';
import {IAppIdentity} from '../entities/IAppIdentity';
import {ApiRoutes} from './ApiRoutes';
import {IAppConfig} from '../config/IAppConfig';
import {Toaster} from '../Toaster';
import {Logger} from '../util/Logger';
import {IDocument} from '../entities/IDocument';
import {IGpgValidationResult} from '../entities/IGpgValidationResult';

declare type HttpMethod = 'GET' | 'POST' | 'DELETE';

export class ApiClient {
    private logger = new Logger('ApiClient');

    private routes: ApiRoutes;

    constructor(config: IAppConfig) {
        this.routes = new ApiRoutes(config);
    }

    public getAppIdentity(): Promise<IAppIdentity> {
        return this.request('GET', this.routes.appIdentity());
    }

    public getDocuments(): Promise<IDocument[]> {
        return this.request('GET', this.routes.documents());
    }

    public createAndSignDocument(data: FormData): Promise<Document[]> {
        return this.request('POST', this.routes.documents(), data);
    }

    public verifyDocument(data: FormData): Promise<IGpgValidationResult> {
        return this.request('POST', this.routes.docSignature(), data);
    }

    public deleteDocument(documentId: number) {
        return this.request('DELETE', `${this.routes.documents()}?documentId=${documentId}`);
    }

    private request(method: HttpMethod, url: string, body?: any): Promise<any> {
        const requestConfig: AxiosRequestConfig = {
            method,
            url,
            data: body,
        };

        return axios(requestConfig)
            .then((response) => {
                return response.data;
            }).catch((e) => {
                this.logger.error('Api request error: ', e);
                Toaster.error(e.MCMessage);
                return Promise.reject(e);
            });

    }

}
