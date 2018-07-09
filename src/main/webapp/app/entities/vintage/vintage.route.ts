import {Routes} from '@angular/router';

import {UserRouteAccessService} from '../../shared';

import {VintageComponent} from './vintage.component';
import {VintageDetailComponent} from './vintage-detail.component';
import {VintagePopupComponent} from './vintage-dialog.component';
import {VintageDeletePopupComponent} from './vintage-delete-dialog.component';

export const vintageRoute: Routes = [
    {
        path: 'vintage',
        component: VintageComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'cavavinApp.vintage.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'vintage/:id',
        component: VintageDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.vintage.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const vintagePopupRoute: Routes = [
    {
        path: 'vintage-new',
        component: VintagePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.vintage.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'vintage/:id/edit',
        component: VintagePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.vintage.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'vintage/:id/delete',
        component: VintageDeletePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'cavavinApp.vintage.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
