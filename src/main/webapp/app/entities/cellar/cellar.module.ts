import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CavavinSharedModule } from '../../shared';
import {
    CellarService,
    CellarPopupService,
    CellarComponent,
    CellarDetailComponent,
    CellarDialogComponent,
    CellarPopupComponent,
    CellarDeletePopupComponent,
    CellarDeleteDialogComponent,
    cellarRoute,
    cellarPopupRoute,
} from './';

const ENTITY_STATES = [
    ...cellarRoute,
    ...cellarPopupRoute,
];

@NgModule({
    imports: [
        CavavinSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CellarComponent,
        CellarDetailComponent,
        CellarDialogComponent,
        CellarDeleteDialogComponent,
        CellarPopupComponent,
        CellarDeletePopupComponent,
    ],
    entryComponents: [
        CellarComponent,
        CellarDialogComponent,
        CellarPopupComponent,
        CellarDeleteDialogComponent,
        CellarDeletePopupComponent,
    ],
    providers: [
        CellarService,
        CellarPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CavavinCellarModule {}
