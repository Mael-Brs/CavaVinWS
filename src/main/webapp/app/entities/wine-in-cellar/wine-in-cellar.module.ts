import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {CavavinSharedModule} from '../../shared';
import {
    WineInCellarComponent,
    WineInCellarDeleteDialogComponent,
    WineInCellarDeletePopupComponent,
    WineInCellarDetailComponent,
    WineInCellarDialogComponent,
    WineInCellarPopupComponent,
    wineInCellarPopupRoute,
    WineInCellarPopupService,
    wineInCellarRoute,
    WineInCellarService,
} from './';

const ENTITY_STATES = [
    ...wineInCellarRoute,
    ...wineInCellarPopupRoute,
];

@NgModule({
    imports: [
        CavavinSharedModule,
        RouterModule.forChild(ENTITY_STATES)
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
