import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { Cellar } from './cellar.model';
import { CellarService } from './cellar.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-cellar',
    templateUrl: './cellar.component.html'
})
export class CellarComponent implements OnInit, OnDestroy {
cellars: Cellar[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private cellarService: CellarService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.cellarService.query().subscribe(
            (res: ResponseWrapper) => {
                this.cellars = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInCellars();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Cellar) {
        return item.id;
    }
    registerChangeInCellars() {
        this.eventSubscriber = this.eventManager.subscribe('cellarListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
