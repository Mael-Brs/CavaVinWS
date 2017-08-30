import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CavavinSharedModule } from '../../shared';
import {
    WineService,
    WinePopupService,
    WineComponent,
    WineDetailComponent,
    WineDialogComponent,
    WinePopupComponent,
    WineDeletePopupComponent,
    WineDeleteDialogComponent,
    wineRoute,
    winePopupRoute,
} from './';

const ENTITY_STATES = [
    ...wineRoute,
    ...winePopupRoute,
];

@NgModule({
    imports: [
        CavavinSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        WineComponent,
        WineDetailComponent,
        WineDialogComponent,
        WineDeleteDialogComponent,
        WinePopupComponent,
        WineDeletePopupComponent,
    ],
    entryComponents: [
        WineComponent,
        WineDialogComponent,
        WinePopupComponent,
        WineDeleteDialogComponent,
        WineDeletePopupComponent,
    ],
    providers: [
        WineService,
        WinePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CavavinWineModule {}
