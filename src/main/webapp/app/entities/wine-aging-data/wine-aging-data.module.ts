import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CavavinSharedModule } from '../../shared';
import {
    WineAgingDataService,
    WineAgingDataPopupService,
    WineAgingDataComponent,
    WineAgingDataDetailComponent,
    WineAgingDataDialogComponent,
    WineAgingDataPopupComponent,
    WineAgingDataDeletePopupComponent,
    WineAgingDataDeleteDialogComponent,
    wineAgingDataRoute,
    wineAgingDataPopupRoute,
} from './';

const ENTITY_STATES = [
    ...wineAgingDataRoute,
    ...wineAgingDataPopupRoute,
];

@NgModule({
    imports: [
        CavavinSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        WineAgingDataComponent,
        WineAgingDataDetailComponent,
        WineAgingDataDialogComponent,
        WineAgingDataDeleteDialogComponent,
        WineAgingDataPopupComponent,
        WineAgingDataDeletePopupComponent,
    ],
    entryComponents: [
        WineAgingDataComponent,
        WineAgingDataDialogComponent,
        WineAgingDataPopupComponent,
        WineAgingDataDeleteDialogComponent,
        WineAgingDataDeletePopupComponent,
    ],
    providers: [
        WineAgingDataService,
        WineAgingDataPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CavavinWineAgingDataModule {}
