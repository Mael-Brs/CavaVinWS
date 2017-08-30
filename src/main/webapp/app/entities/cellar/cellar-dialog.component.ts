import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Cellar } from './cellar.model';
import { CellarPopupService } from './cellar-popup.service';
import { CellarService } from './cellar.service';

@Component({
    selector: 'jhi-cellar-dialog',
    templateUrl: './cellar-dialog.component.html'
})
export class CellarDialogComponent implements OnInit {

    cellar: Cellar;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private cellarService: CellarService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.cellar.id !== undefined) {
            this.subscribeToSaveResponse(
                this.cellarService.update(this.cellar));
        } else {
            this.subscribeToSaveResponse(
                this.cellarService.create(this.cellar));
        }
    }

    private subscribeToSaveResponse(result: Observable<Cellar>) {
        result.subscribe((res: Cellar) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Cellar) {
        this.eventManager.broadcast({ name: 'cellarListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-cellar-popup',
    template: ''
})
export class CellarPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cellarPopupService: CellarPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.cellarPopupService
                    .open(CellarDialogComponent as Component, params['id']);
            } else {
                this.cellarPopupService
                    .open(CellarDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
