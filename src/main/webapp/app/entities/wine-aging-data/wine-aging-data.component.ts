import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { WineAgingData } from './wine-aging-data.model';
import { WineAgingDataService } from './wine-aging-data.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-wine-aging-data',
    templateUrl: './wine-aging-data.component.html'
})
export class WineAgingDataComponent implements OnInit, OnDestroy {
wineAgingData: WineAgingData[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private wineAgingDataService: WineAgingDataService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.wineAgingDataService.query().subscribe(
            (res: ResponseWrapper) => {
                this.wineAgingData = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInWineAgingData();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: WineAgingData) {
        return item.id;
    }
    registerChangeInWineAgingData() {
        this.eventSubscriber = this.eventManager.subscribe('wineAgingDataListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
