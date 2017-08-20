import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { CavavinCellarModule } from './cellar/cellar.module';
import { CavavinColorModule } from './color/color.module';
import { CavavinPinnedVintageModule } from './pinned-vintage/pinned-vintage.module';
import { CavavinRegionModule } from './region/region.module';
import { CavavinVintageModule } from './vintage/vintage.module';
import { CavavinWineModule } from './wine/wine.module';
import { CavavinWineAgingDataModule } from './wine-aging-data/wine-aging-data.module';
import { CavavinWineInCellarModule } from './wine-in-cellar/wine-in-cellar.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        CavavinCellarModule,
        CavavinColorModule,
        CavavinPinnedVintageModule,
        CavavinRegionModule,
        CavavinVintageModule,
        CavavinWineModule,
        CavavinWineAgingDataModule,
        CavavinWineInCellarModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CavavinEntityModule {}
