import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CavavinSharedModule } from '../../shared';
import {
    WineInCellarService,
    WineInCellarPopupService,
    WineInCellarComponent,
    WineInCellarDetailComponent,
    WineInCellarDialogComponent,
    WineInCellarPopupComponent,
    WineInCellarDeletePopupComponent,
    WineInCellarDeleteDialogComponent,
    wineInCellarRoute,
    wineInCellarPopupRoute,
} from './';

const ENTITY_STATES = [
    ...wineInCellarRoute,
    ...wineInCellarPopupRoute,
];

@NgModule({
    imports: [
        CavavinSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        WineInCellarComponent,
        WineInCellarDetailComponent,
        WineInCellarDialogComponent,
        WineInCellarDeleteDialogComponent,
        WineInCellarPopupComponent,
        WineInCellarDeletePopupComponent,
    ],
    entryComponents: [
        WineInCellarComponent,
        WineInCellarDialogComponent,
        WineInCellarPopupComponent,
        WineInCellarDeleteDialogComponent,
        WineInCellarDeletePopupComponent,
    ],
    providers: [
        WineInCellarService,
        WineInCellarPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CavavinWineInCellarModule {}
