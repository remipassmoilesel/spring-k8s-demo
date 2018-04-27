import Vue from 'vue';
import * as _ from 'lodash';
import Toasted from 'vue-toasted';

Vue.use(Toasted);

interface IVueToasted {
    show(MCMessage: string, options: IToastOptions);
}
const VueToasted: IVueToasted = (Vue as any).toasted;

declare type ToastType = 'success' | 'info' | 'error';
interface IToastOptions {
    icon?: string;
    duration?: number;
    type?: ToastType;
}

const defaultOptions = {
    duration: 3000,
};

export class Toaster {

    public static info(MCMessage: string) {
        this.show(MCMessage, {
            type: 'info',
        });
    }

    public static success(MCMessage: string) {
        this.show(MCMessage, {
            type: 'success',
        });
    }

    public static error(MCMessage: string) {
        this.show(MCMessage, {
            type: 'error',
        });
    }

    private static show(MCMessage: string, options){
        const toastOptions = _.defaultsDeep({}, defaultOptions, options);
        VueToasted.show(MCMessage, toastOptions);
    }

}
