import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { PinnedVintage } from './pinned-vintage.model';
import { PinnedVintageService } from './pinned-vintage.service';

@Component({
    selector: 'jhi-pinned-vintage-detail',
    templateUrl: './pinned-vintage-detail.component.html'
})
export class PinnedVintageDetailComponent implements OnInit, OnDestroy {

    pinnedVintage: PinnedVintage;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private pinnedVintageService: PinnedVintageService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPinnedVintages();
    }

    load(id) {
        this.pinnedVintageService.find(id).subscribe((pinnedVintage) => {
            this.pinnedVintage = pinnedVintage;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPinnedVintages() {
        this.eventSubscriber = this.eventManager.subscribe(
            'pinnedVintageListModification',
            (response) => this.load(this.pinnedVintage.id)
        );
    }
}
