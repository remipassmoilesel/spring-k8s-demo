import {HomeView} from '../views/home';
import {IRouteConfigAugmented} from './IRouteConfigAugmented';
import {DocumentsView} from '../views/documents';
import {NewDocumentView} from '../views/new-document';
import {CheckDocumentView} from '../views/check-document';

export function getAllRoutes(): IRouteConfigAugmented[] {
    return [
        {
            path: '/',
            component: HomeView,
            text: 'Home',
            description: '',
        },
        {
            path: '/documents',
            component: DocumentsView,
            text: 'Documents',
            description: '',
        },
        {
            path: '/new',
            component: NewDocumentView,
            text: 'New',
            description: '',
        },
        {
            path: '/check',
            component: CheckDocumentView,
            text: 'Check',
            description: '',
        },
    ];
}
