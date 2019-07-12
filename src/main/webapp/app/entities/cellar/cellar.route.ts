import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CellarComponent } from './cellar.component';
import { CellarDetailComponent } from './cellar-detail.component';
import { CellarPopupComponent } from './cellar-dialog.component';
import { CellarDeletePopupComponent } from './cellar-delete-dialog.component';

export const cellarRoute: Routes = [
    {
        path: 'cellar',
        component: CellarComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.cellar.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'cellar/:id',
        component: CellarDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.cellar.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cellarPopupRoute: Routes = [
    {
        path: 'cellar-new',
        component: CellarPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.cellar.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cellar/:id/edit',
        component: CellarPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.cellar.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cellar/:id/delete',
        component: CellarDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.cellar.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
