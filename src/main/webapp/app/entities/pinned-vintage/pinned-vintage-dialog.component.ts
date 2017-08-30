import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { PinnedVintage } from './pinned-vintage.model';
import { PinnedVintagePopupService } from './pinned-vintage-popup.service';
import { PinnedVintageService } from './pinned-vintage.service';
import { Vintage, VintageService } from '../vintage';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-pinned-vintage-dialog',
    templateUrl: './pinned-vintage-dialog.component.html'
})
export class PinnedVintageDialogComponent implements OnInit {

    pinnedVintage: PinnedVintage;
    isSaving: boolean;

    vintages: Vintage[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private pinnedVintageService: PinnedVintageService,
        private vintageService: VintageService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.vintageService.query()
            .subscribe((res: ResponseWrapper) => { this.vintages = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.pinnedVintage.id !== undefined) {
            this.subscribeToSaveResponse(
                this.pinnedVintageService.update(this.pinnedVintage));
        } else {
            this.subscribeToSaveResponse(
                this.pinnedVintageService.create(this.pinnedVintage));
        }
    }

    private subscribeToSaveResponse(result: Observable<PinnedVintage>) {
        result.subscribe((res: PinnedVintage) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: PinnedVintage) {
        this.eventManager.broadcast({ name: 'pinnedVintageListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackVintageById(index: number, item: Vintage) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-pinned-vintage-popup',
    template: ''
})
export class PinnedVintagePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pinnedVintagePopupService: PinnedVintagePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.pinnedVintagePopupService
                    .open(PinnedVintageDialogComponent as Component, params['id']);
            } else {
                this.pinnedVintagePopupService
                    .open(PinnedVintageDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
