import Vue from 'vue';
import {ApiClient} from '../lib/api/ApiClient';

export abstract class AbstractUiComponent extends Vue {

    public abstract componentName: string;
    public abstract componentDescription: string;
    public abstract componentTagName: string;

    protected apiClient: ApiClient;

    constructor(data?: any) {
        super(data);
    }
}
