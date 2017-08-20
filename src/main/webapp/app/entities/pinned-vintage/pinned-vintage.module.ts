import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CavavinSharedModule } from '../../shared';
import {
    PinnedVintageService,
    PinnedVintagePopupService,
    PinnedVintageComponent,
    PinnedVintageDetailComponent,
    PinnedVintageDialogComponent,
    PinnedVintagePopupComponent,
    PinnedVintageDeletePopupComponent,
    PinnedVintageDeleteDialogComponent,
    pinnedVintageRoute,
    pinnedVintagePopupRoute,
} from './';

const ENTITY_STATES = [
    ...pinnedVintageRoute,
    ...pinnedVintagePopupRoute,
];

@NgModule({
    imports: [
        CavavinSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PinnedVintageComponent,
        PinnedVintageDetailComponent,
        PinnedVintageDialogComponent,
        PinnedVintageDeleteDialogComponent,
        PinnedVintagePopupComponent,
        PinnedVintageDeletePopupComponent,
    ],
    entryComponents: [
        PinnedVintageComponent,
        PinnedVintageDialogComponent,
        PinnedVintagePopupComponent,
        PinnedVintageDeleteDialogComponent,
        PinnedVintageDeletePopupComponent,
    ],
    providers: [
        PinnedVintageService,
        PinnedVintagePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CavavinPinnedVintageModule {}
