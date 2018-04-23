import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { WineComponent } from './wine.component';
import { WineDetailComponent } from './wine-detail.component';
import { WinePopupComponent } from './wine-dialog.component';
import { WineDeletePopupComponent } from './wine-delete-dialog.component';

export const wineRoute: Routes = [
    {
        path: 'wine',
        component: WineComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'cavavinApp.wine.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'wine/:id',
        component: WineDetailComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'cavavinApp.wine.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const winePopupRoute: Routes = [
    {
        path: 'wine-new',
        component: WinePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'cavavinApp.wine.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'wine/:id/edit',
        component: WinePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'cavavinApp.wine.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'wine/:id/delete',
        component: WineDeletePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'cavavinApp.wine.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
