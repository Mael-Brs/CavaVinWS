import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { WineAgingData } from './wine-aging-data.model';
import { WineAgingDataService } from './wine-aging-data.service';

@Component({
    selector: 'jhi-wine-aging-data-detail',
    templateUrl: './wine-aging-data-detail.component.html'
})
export class WineAgingDataDetailComponent implements OnInit, OnDestroy {

    wineAgingData: WineAgingData;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private wineAgingDataService: WineAgingDataService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInWineAgingData();
    }

    load(id) {
        this.wineAgingDataService.find(id).subscribe((wineAgingData) => {
            this.wineAgingData = wineAgingData;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInWineAgingData() {
        this.eventSubscriber = this.eventManager.subscribe(
            'wineAgingDataListModification',
            (response) => this.load(this.wineAgingData.id)
        );
    }
}
