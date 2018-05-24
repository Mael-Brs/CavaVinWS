import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Color } from './color.model';
import { ColorService } from './color.service';

@Component({
    selector: 'jhi-color-detail',
    templateUrl: './color-detail.component.html'
})
export class ColorDetailComponent implements OnInit, OnDestroy {

    color: Color;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private colorService: ColorService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInColors();
    }

    load(id) {
        this.colorService.find(id).subscribe((color) => {
            this.color = color;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInColors() {
        this.eventSubscriber = this.eventManager.subscribe(
            'colorListModification',
            (response) => this.load(this.color.id)
        );
    }
}
