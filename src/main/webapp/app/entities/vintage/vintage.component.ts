import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs/Rx';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {Vintage} from './vintage.model';
import {VintageService} from './vintage.service';
import {Principal, ResponseWrapper} from '../../shared';

@Component({
    selector: 'jhi-vintage',
    templateUrl: './vintage.component.html'
})
export class VintageComponent implements OnInit, OnDestroy {
vintages: Vintage[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private vintageService: VintageService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.vintageService.query().subscribe(
            (res: ResponseWrapper) => {
                this.vintages = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInVintages();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Vintage) {
        return item.id;
    }
    registerChangeInVintages() {
        this.eventSubscriber = this.eventManager.subscribe('vintageListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
