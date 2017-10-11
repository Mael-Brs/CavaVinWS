import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CavavinSharedModule } from '../../shared';
import {
    PinnedWineService,
    PinnedWinePopupService,
    PinnedWineComponent,
    PinnedWineDetailComponent,
    PinnedWineDialogComponent,
    PinnedWinePopupComponent,
    PinnedWineDeletePopupComponent,
    PinnedWineDeleteDialogComponent,
    pinnedWineRoute,
    pinnedWinePopupRoute,
} from './';

const ENTITY_STATES = [
    ...pinnedWineRoute,
    ...pinnedWinePopupRoute,
];

@NgModule({
    imports: [
        CavavinSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PinnedWineComponent,
        PinnedWineDetailComponent,
        PinnedWineDialogComponent,
        PinnedWineDeleteDialogComponent,
        PinnedWinePopupComponent,
        PinnedWineDeletePopupComponent,
    ],
    entryComponents: [
        PinnedWineComponent,
        PinnedWineDialogComponent,
        PinnedWinePopupComponent,
        PinnedWineDeleteDialogComponent,
        PinnedWineDeletePopupComponent,
    ],
    providers: [
        PinnedWineService,
        PinnedWinePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CavavinPinnedWineModule {}
