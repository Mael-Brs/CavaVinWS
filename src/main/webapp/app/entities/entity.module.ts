import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { CavavinCellarModule } from './cellar/cellar.module';
import { CavavinColorModule } from './color/color.module';
import { CavavinRegionModule } from './region/region.module';
import { CavavinWineAgingDataModule } from './wine-aging-data/wine-aging-data.module';
import { CavavinPinnedWineModule } from './pinned-wine/pinned-wine.module';
import { CavavinWineModule } from './wine/wine.module';
import { CavavinVintageModule } from './vintage/vintage.module';
import { CavavinWineInCellarModule } from './wine-in-cellar/wine-in-cellar.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        CavavinCellarModule,
        CavavinColorModule,
        CavavinRegionModule,
        CavavinWineAgingDataModule,
        CavavinPinnedWineModule,
        CavavinWineModule,
        CavavinVintageModule,
        CavavinWineInCellarModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CavavinEntityModule {}
