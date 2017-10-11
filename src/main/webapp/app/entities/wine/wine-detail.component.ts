import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Wine } from './wine.model';
import { WineService } from './wine.service';

@Component({
    selector: 'jhi-wine-detail',
    templateUrl: './wine-detail.component.html'
})
export class WineDetailComponent implements OnInit, OnDestroy {

    wine: Wine;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private wineService: WineService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInWines();
    }

    load(id) {
        this.wineService.find(id).subscribe((wine) => {
            this.wine = wine;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInWines() {
        this.eventSubscriber = this.eventManager.subscribe(
            'wineListModification',
            (response) => this.load(this.wine.id)
        );
    }
}
