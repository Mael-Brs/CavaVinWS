import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { WineAgingDataComponent } from './wine-aging-data.component';
import { WineAgingDataDetailComponent } from './wine-aging-data-detail.component';
import { WineAgingDataPopupComponent } from './wine-aging-data-dialog.component';
import { WineAgingDataDeletePopupComponent } from './wine-aging-data-delete-dialog.component';

export const wineAgingDataRoute: Routes = [
    {
        path: 'wine-aging-data',
        component: WineAgingDataComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.wineAgingData.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'wine-aging-data/:id',
        component: WineAgingDataDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.wineAgingData.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const wineAgingDataPopupRoute: Routes = [
    {
        path: 'wine-aging-data-new',
        component: WineAgingDataPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.wineAgingData.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'wine-aging-data/:id/edit',
        component: WineAgingDataPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.wineAgingData.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'wine-aging-data/:id/delete',
        component: WineAgingDataDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.wineAgingData.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
