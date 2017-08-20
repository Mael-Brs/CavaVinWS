import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Wine } from './wine.model';
import { WinePopupService } from './wine-popup.service';
import { WineService } from './wine.service';
import { Region, RegionService } from '../region';
import { Color, ColorService } from '../color';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-wine-dialog',
    templateUrl: './wine-dialog.component.html'
})
export class WineDialogComponent implements OnInit {

    wine: Wine;
    isSaving: boolean;

    regions: Region[];

    colors: Color[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private wineService: WineService,
        private regionService: RegionService,
        private colorService: ColorService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.regionService.query()
            .subscribe((res: ResponseWrapper) => { this.regions = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.colorService.query()
            .subscribe((res: ResponseWrapper) => { this.colors = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.wine.id !== undefined) {
            this.subscribeToSaveResponse(
                this.wineService.update(this.wine));
        } else {
            this.subscribeToSaveResponse(
                this.wineService.create(this.wine));
        }
    }

    private subscribeToSaveResponse(result: Observable<Wine>) {
        result.subscribe((res: Wine) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Wine) {
        this.eventManager.broadcast({ name: 'wineListModification', content: 'OK'});
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

    trackRegionById(index: number, item: Region) {
        return item.id;
    }

    trackColorById(index: number, item: Color) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-wine-popup',
    template: ''
})
export class WinePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private winePopupService: WinePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.winePopupService
                    .open(WineDialogComponent as Component, params['id']);
            } else {
                this.winePopupService
                    .open(WineDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}