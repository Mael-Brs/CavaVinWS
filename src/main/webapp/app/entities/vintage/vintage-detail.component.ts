import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Vintage } from './vintage.model';
import { VintageService } from './vintage.service';

@Component({
    selector: 'jhi-vintage-detail',
    templateUrl: './vintage-detail.component.html'
})
export class VintageDetailComponent implements OnInit, OnDestroy {

    vintage: Vintage;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private vintageService: VintageService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInVintages();
    }

    load(id) {
        this.vintageService.find(id).subscribe((vintage) => {
            this.vintage = vintage;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInVintages() {
        this.eventSubscriber = this.eventManager.subscribe(
            'vintageListModification',
            (response) => this.load(this.vintage.id)
        );
    }
}
