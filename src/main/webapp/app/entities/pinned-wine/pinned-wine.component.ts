import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { PinnedWine } from './pinned-wine.model';
import { PinnedWineService } from './pinned-wine.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-pinned-wine',
    templateUrl: './pinned-wine.component.html'
})
export class PinnedWineComponent implements OnInit, OnDestroy {
pinnedWines: PinnedWine[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private pinnedWineService: PinnedWineService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.pinnedWineService.query().subscribe(
            (res: ResponseWrapper) => {
                this.pinnedWines = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
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
        this.jhiAlertService.error(error.message, null, null);
    }
}
