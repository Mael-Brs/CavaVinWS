import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { WineAgingData } from './wine-aging-data.model';
import { WineAgingDataPopupService } from './wine-aging-data-popup.service';
import { WineAgingDataService } from './wine-aging-data.service';
import { Color, ColorService } from '../color';
import { Region, RegionService } from '../region';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-wine-aging-data-dialog',
    templateUrl: './wine-aging-data-dialog.component.html'
})
export class WineAgingDataDialogComponent implements OnInit {

    wineAgingData: WineAgingData;
    isSaving: boolean;

    colors: Color[];

    regions: Region[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private wineAgingDataService: WineAgingDataService,
        private colorService: ColorService,
        private regionService: RegionService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.colorService.query()
            .subscribe((res: ResponseWrapper) => { this.colors = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.regionService.query()
            .subscribe((res: ResponseWrapper) => { this.regions = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.wineAgingData.id !== undefined) {
            this.subscribeToSaveResponse(
                this.wineAgingDataService.update(this.wineAgingData));
        } else {
            this.subscribeToSaveResponse(
                this.wineAgingDataService.create(this.wineAgingData));
        }
    }

    private subscribeToSaveResponse(result: Observable<WineAgingData>) {
        result.subscribe((res: WineAgingData) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: WineAgingData) {
        this.eventManager.broadcast({ name: 'wineAgingDataListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackColorById(index: number, item: Color) {
        return item.id;
    }

    trackRegionById(index: number, item: Region) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-wine-aging-data-popup',
    template: ''
})
export class WineAgingDataPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private wineAgingDataPopupService: WineAgingDataPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.wineAgingDataPopupService
                    .open(WineAgingDataDialogComponent as Component, params['id']);
            } else {
                this.wineAgingDataPopupService
                    .open(WineAgingDataDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
