import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage, LocalStorageService, SessionStorageService } from 'ngx-webstorage';

import { CavavinSharedModule, UserRouteAccessService } from './shared';
import { CavavinAppRoutingModule} from './app-routing.module';
import { CavavinHomeModule } from './home/home.module';
import { CavavinAdminModule } from './admin/admin.module';
import { CavavinAccountModule } from './account/account.module';
import { CavavinEntityModule } from './entities/entity.module';
import { PaginationConfig } from './blocks/config/uib-pagination.config';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from "./blocks/interceptor/auth.interceptor";
// jhipster-needle-angular-add-module-import JHipster will add new module here

import {
    JhiMainComponent,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ActiveMenuDirective,
    ErrorComponent
} from './layouts';

@NgModule({
    imports: [
        BrowserModule,
        CavavinAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        CavavinSharedModule,
        CavavinHomeModule,
        CavavinAdminModule,
        CavavinAccountModule,
        CavavinEntityModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent
    ],
    providers: [
        ProfileService,
        PaginationConfig,
        UserRouteAccessService,
        {
            provide: HTTP_INTERCEPTORS,
            useFactory: (localStorageService, sessionStorageService) => new AuthInterceptor(localStorageService, sessionStorageService),
            multi: true,
            deps: [
                LocalStorageService,
                SessionStorageService
            ]
        }
    ],
    bootstrap: [ JhiMainComponent ]
})
export class CavavinAppModule {}
