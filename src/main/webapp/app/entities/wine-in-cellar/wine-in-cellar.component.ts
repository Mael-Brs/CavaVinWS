import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { WineInCellar } from './wine-in-cellar.model';
import { WineInCellarService } from './wine-in-cellar.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-wine-in-cellar',
    templateUrl: './wine-in-cellar.component.html'
})
export class WineInCellarComponent implements OnInit, OnDestroy {
wineInCellars: WineInCellar[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private wineInCellarService: WineInCellarService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.wineInCellarService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.wineInCellars = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.wineInCellarService.query().subscribe(
            (res: ResponseWrapper) => {
                this.wineInCellars = res.json;
                this.currentSearch = '';
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInWineInCellars();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: WineInCellar) {
        return item.id;
    }
    registerChangeInWineInCellars() {
        this.eventSubscriber = this.eventManager.subscribe('wineInCellarListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
