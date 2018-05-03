import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { WineInCellar } from './wine-in-cellar.model';
import { WineInCellarPopupService } from './wine-in-cellar-popup.service';
import { WineInCellarService } from './wine-in-cellar.service';
import { Vintage, VintageService } from '../vintage';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-wine-in-cellar-dialog',
    templateUrl: './wine-in-cellar-dialog.component.html'
})
export class WineInCellarDialogComponent implements OnInit {

    wineInCellar: WineInCellar;
    isSaving: boolean;

    vintages: Vintage[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private wineInCellarService: WineInCellarService,
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
        if (this.wineInCellar.id !== undefined) {
            this.subscribeToSaveResponse(
                this.wineInCellarService.update(this.wineInCellar));
        } else {
            this.subscribeToSaveResponse(
                this.wineInCellarService.create(this.wineInCellar));
        }
    }

    private subscribeToSaveResponse(result: Observable<WineInCellar>) {
        result.subscribe((res: WineInCellar) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: WineInCellar) {
        this.eventManager.broadcast({ name: 'wineInCellarListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackVintageById(index: number, item: Vintage) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-wine-in-cellar-popup',
    template: ''
})
export class WineInCellarPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private wineInCellarPopupService: WineInCellarPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.wineInCellarPopupService
                    .open(WineInCellarDialogComponent as Component, params['id']);
            } else {
                this.wineInCellarPopupService
                    .open(WineInCellarDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
