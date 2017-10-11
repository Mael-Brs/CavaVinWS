import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { PinnedWine } from './pinned-wine.model';
import { PinnedWinePopupService } from './pinned-wine-popup.service';
import { PinnedWineService } from './pinned-wine.service';
import { Wine, WineService } from '../wine';
import { ResponseWrapper, Principal } from '../../shared';

@Component({
    selector: 'jhi-pinned-wine-dialog',
    templateUrl: './pinned-wine-dialog.component.html'
})
export class PinnedWineDialogComponent implements OnInit {

    pinnedWine: PinnedWine;
    isSaving: boolean;
    currentAccount: any;

    wines: Wine[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private pinnedWineService: PinnedWineService,
        private wineService: WineService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.wineService.query()
            .subscribe((res: ResponseWrapper) => { this.wines = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        this.pinnedWine.userId = this.currentAccount.id;
        if (this.pinnedWine.id !== undefined) {
            this.subscribeToSaveResponse(
                this.pinnedWineService.update(this.pinnedWine));
        } else {
            this.subscribeToSaveResponse(
                this.pinnedWineService.create(this.pinnedWine));
        }
    }

    private subscribeToSaveResponse(result: Observable<PinnedWine>) {
        result.subscribe((res: PinnedWine) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: PinnedWine) {
        this.eventManager.broadcast({ name: 'pinnedWineListModification', content: 'OK'});
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
    selector: 'jhi-pinned-wine-popup',
    template: ''
})
export class PinnedWinePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pinnedWinePopupService: PinnedWinePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.pinnedWinePopupService
                    .open(PinnedWineDialogComponent as Component, params['id']);
            } else {
                this.pinnedWinePopupService
                    .open(PinnedWineDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
