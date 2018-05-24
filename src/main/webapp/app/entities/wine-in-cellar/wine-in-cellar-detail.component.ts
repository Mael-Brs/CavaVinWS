import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { WineInCellar } from './wine-in-cellar.model';
import { WineInCellarService } from './wine-in-cellar.service';

@Component({
    selector: 'jhi-wine-in-cellar-detail',
    templateUrl: './wine-in-cellar-detail.component.html'
})
export class WineInCellarDetailComponent implements OnInit, OnDestroy {

    wineInCellar: WineInCellar;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private wineInCellarService: WineInCellarService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInWineInCellars();
    }

    load(id) {
        this.wineInCellarService.find(id).subscribe((wineInCellar) => {
            this.wineInCellar = wineInCellar;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInWineInCellars() {
        this.eventSubscriber = this.eventManager.subscribe(
            'wineInCellarListModification',
            (response) => this.load(this.wineInCellar.id)
        );
    }
}
