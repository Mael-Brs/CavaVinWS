import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PinnedWineComponent } from './pinned-wine.component';
import { PinnedWineDetailComponent } from './pinned-wine-detail.component';
import { PinnedWinePopupComponent } from './pinned-wine-dialog.component';
import { PinnedWineDeletePopupComponent } from './pinned-wine-delete-dialog.component';

export const pinnedWineRoute: Routes = [
    {
        path: 'pinned-wine',
        component: PinnedWineComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.pinnedWine.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'pinned-wine/:id',
        component: PinnedWineDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.pinnedWine.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pinnedWinePopupRoute: Routes = [
    {
        path: 'pinned-wine-new',
        component: PinnedWinePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.pinnedWine.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pinned-wine/:id/edit',
        component: PinnedWinePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.pinnedWine.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pinned-wine/:id/delete',
        component: PinnedWineDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cavavinApp.pinnedWine.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
