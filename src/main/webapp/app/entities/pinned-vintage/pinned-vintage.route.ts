import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PinnedVintageComponent } from './pinned-vintage.component';
import { PinnedVintageDetailComponent } from './pinned-vintage-detail.component';
import { PinnedVintagePopupComponent } from './pinned-vintage-dialog.component';
import { PinnedVintageDeletePopupComponent } from './pinned-vintage-delete-dialog.component';

export const pinnedVintageRoute: Routes = [
    {
        path: 'pinned-vintage',
        component: PinnedVintageComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.pinnedVintage.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'pinned-vintage/:id',
        component: PinnedVintageDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.pinnedVintage.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pinnedVintagePopupRoute: Routes = [
    {
        path: 'pinned-vintage-new',
        component: PinnedVintagePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.pinnedVintage.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pinned-vintage/:id/edit',
        component: PinnedVintagePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.pinnedVintage.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pinned-vintage/:id/delete',
        component: PinnedVintageDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.pinnedVintage.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
