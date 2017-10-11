import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { PinnedWine } from './pinned-wine.model';
import { PinnedWineService } from './pinned-wine.service';

@Component({
    selector: 'jhi-pinned-wine-detail',
    templateUrl: './pinned-wine-detail.component.html'
})
export class PinnedWineDetailComponent implements OnInit, OnDestroy {

    pinnedWine: PinnedWine;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private pinnedWineService: PinnedWineService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPinnedWines();
    }

    load(id) {
        this.pinnedWineService.find(id).subscribe((pinnedWine) => {
            this.pinnedWine = pinnedWine;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPinnedWines() {
        this.eventSubscriber = this.eventManager.subscribe(
            'pinnedWineListModification',
            (response) => this.load(this.pinnedWine.id)
        );
    }
}
