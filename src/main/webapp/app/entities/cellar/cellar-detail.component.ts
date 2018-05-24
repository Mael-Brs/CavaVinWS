import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Cellar } from './cellar.model';
import { CellarService } from './cellar.service';

@Component({
    selector: 'jhi-cellar-detail',
    templateUrl: './cellar-detail.component.html'
})
export class CellarDetailComponent implements OnInit, OnDestroy {

    cellar: Cellar;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private cellarService: CellarService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCellars();
    }

    load(id) {
        this.cellarService.find(id).subscribe((cellar) => {
            this.cellar = cellar;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCellars() {
        this.eventSubscriber = this.eventManager.subscribe(
            'cellarListModification',
            (response) => this.load(this.cellar.id)
        );
    }
}
