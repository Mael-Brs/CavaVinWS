import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CavavinSharedModule } from '../../shared';
import {
    VintageService,
    VintagePopupService,
    VintageComponent,
    VintageDetailComponent,
    VintageDialogComponent,
    VintagePopupComponent,
    VintageDeletePopupComponent,
    VintageDeleteDialogComponent,
    vintageRoute,
    vintagePopupRoute,
} from './';

const ENTITY_STATES = [
    ...vintageRoute,
    ...vintagePopupRoute,
];

@NgModule({
    imports: [
        CavavinSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        VintageComponent,
        VintageDetailComponent,
        VintageDialogComponent,
        VintageDeleteDialogComponent,
        VintagePopupComponent,
        VintageDeletePopupComponent,
    ],
    entryComponents: [
        VintageComponent,
        VintageDialogComponent,
        VintagePopupComponent,
        VintageDeleteDialogComponent,
        VintageDeletePopupComponent,
    ],
    providers: [
        VintageService,
        VintagePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CavavinVintageModule {}
