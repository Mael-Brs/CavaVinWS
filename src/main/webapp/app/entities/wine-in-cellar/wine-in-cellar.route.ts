import {Routes} from '@angular/router';

import {UserRouteAccessService} from '../../shared';
import {WineInCellarComponent} from './wine-in-cellar.component';
import {WineInCellarDetailComponent} from './wine-in-cellar-detail.component';
import {WineInCellarPopupComponent} from './wine-in-cellar-dialog.component';
import {WineInCellarDeletePopupComponent} from './wine-in-cellar-delete-dialog.component';

export const wineInCellarRoute: Routes = [
    {
        path: 'wine-in-cellar',
        component: WineInCellarComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.wineInCellar.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'wine-in-cellar/:id',
        component: WineInCellarDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.wineInCellar.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const wineInCellarPopupRoute: Routes = [
    {
        path: 'wine-in-cellar-new',
        component: WineInCellarPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.wineInCellar.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'wine-in-cellar/:id/edit',
        component: WineInCellarPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.wineInCellar.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'wine-in-cellar/:id/delete',
        component: WineInCellarDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.wineInCellar.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
