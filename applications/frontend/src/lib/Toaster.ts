import Vue from 'vue';
import * as _ from 'lodash';
import Toasted from 'vue-toasted';

Vue.use(Toasted);

interface IVueToasted {
    show(message: string, options: IToastOptions);
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

    public static info(message: string) {
        this.show(message, {
            type: 'info',
        });
    }

    public static success(message: string) {
        this.show(message, {
            type: 'success',
        });
    }

    public static error(message: string) {
        this.show(message, {
            type: 'error',
        });
    }

    private static show(message: string, options){
        const toastOptions = _.defaultsDeep({}, defaultOptions, options);
        VueToasted.show(message, toastOptions);
    }

}
