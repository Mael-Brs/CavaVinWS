import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Vintage } from './vintage.model';
import { VintagePopupService } from './vintage-popup.service';
import { VintageService } from './vintage.service';
import { Wine, WineService } from '../wine';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-vintage-dialog',
    templateUrl: './vintage-dialog.component.html'
})
export class VintageDialogComponent implements OnInit {

    vintage: Vintage;
    isSaving: boolean;

    wines: Wine[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private vintageService: VintageService,
        private wineService: WineService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.wineService.query()
            .subscribe((res: ResponseWrapper) => { this.wines = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.vintage.id !== undefined) {
            this.subscribeToSaveResponse(
                this.vintageService.update(this.vintage));
        } else {
            this.subscribeToSaveResponse(
                this.vintageService.create(this.vintage));
        }
    }

    private subscribeToSaveResponse(result: Observable<Vintage>) {
        result.subscribe((res: Vintage) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Vintage) {
        this.eventManager.broadcast({ name: 'vintageListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackWineById(index: number, item: Wine) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-vintage-popup',
    template: ''
})
export class VintagePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private vintagePopupService: VintagePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.vintagePopupService
                    .open(VintageDialogComponent as Component, params['id']);
            } else {
                this.vintagePopupService
                    .open(VintageDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
