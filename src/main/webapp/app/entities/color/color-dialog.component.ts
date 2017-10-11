import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Color } from './color.model';
import { ColorPopupService } from './color-popup.service';
import { ColorService } from './color.service';

@Component({
    selector: 'jhi-color-dialog',
    templateUrl: './color-dialog.component.html'
})
export class ColorDialogComponent implements OnInit {

    color: Color;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private colorService: ColorService,
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
        if (this.color.id !== undefined) {
            this.subscribeToSaveResponse(
                this.colorService.update(this.color));
        } else {
            this.subscribeToSaveResponse(
                this.colorService.create(this.color));
        }
    }

    private subscribeToSaveResponse(result: Observable<Color>) {
        result.subscribe((res: Color) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Color) {
        this.eventManager.broadcast({ name: 'colorListModification', content: 'OK'});
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
    selector: 'jhi-color-popup',
    template: ''
})
export class ColorPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private colorPopupService: ColorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.colorPopupService
                    .open(ColorDialogComponent as Component, params['id']);
            } else {
                this.colorPopupService
                    .open(ColorDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
