import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ColorComponent } from './color.component';
import { ColorDetailComponent } from './color-detail.component';
import { ColorPopupComponent } from './color-dialog.component';
import { ColorDeletePopupComponent } from './color-delete-dialog.component';

export const colorRoute: Routes = [
    {
        path: 'color',
        component: ColorComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.color.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'color/:id',
        component: ColorDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.color.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const colorPopupRoute: Routes = [
    {
        path: 'color-new',
        component: ColorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.color.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'color/:id/edit',
        component: ColorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.color.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'color/:id/delete',
        component: ColorDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.color.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
