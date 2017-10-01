import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { PinnedWine } from './pinned-wine.model';
import { PinnedWineService } from './pinned-wine.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-pinned-wine',
    templateUrl: './pinned-wine.component.html'
})
export class PinnedWineComponent implements OnInit, OnDestroy {
pinnedWines: PinnedWine[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private pinnedWineService: PinnedWineService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.pinnedWineService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.pinnedWines = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.pinnedWineService.query().subscribe(
            (res: ResponseWrapper) => {
                this.pinnedWines = res.json;
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
        this.registerChangeInPinnedWines();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: PinnedWine) {
        return item.id;
    }
    registerChangeInPinnedWines() {
        this.eventSubscriber = this.eventManager.subscribe('pinnedWineListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
