import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CavavinSharedModule } from '../../shared';
import {
    ColorService,
    ColorPopupService,
    ColorComponent,
    ColorDetailComponent,
    ColorDialogComponent,
    ColorPopupComponent,
    ColorDeletePopupComponent,
    ColorDeleteDialogComponent,
    colorRoute,
    colorPopupRoute,
} from './';

const ENTITY_STATES = [
    ...colorRoute,
    ...colorPopupRoute,
];

@NgModule({
    imports: [
        CavavinSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ColorComponent,
        ColorDetailComponent,
        ColorDialogComponent,
        ColorDeleteDialogComponent,
        ColorPopupComponent,
        ColorDeletePopupComponent,
    ],
    entryComponents: [
        ColorComponent,
        ColorDialogComponent,
        ColorPopupComponent,
        ColorDeleteDialogComponent,
        ColorDeletePopupComponent,
    ],
    providers: [
        ColorService,
        ColorPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CavavinColorModule {}
