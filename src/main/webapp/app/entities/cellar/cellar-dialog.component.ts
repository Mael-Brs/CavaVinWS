import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Cellar } from './cellar.model';
import { CellarPopupService } from './cellar-popup.service';
import { CellarService } from './cellar.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-cellar-dialog',
    templateUrl: './cellar-dialog.component.html'
})
export class CellarDialogComponent implements OnInit {

    cellar: Cellar;
    isSaving: boolean;
    currentAccount: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private cellarService: CellarService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        this.cellar.userId = this.currentAccount.id;
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
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Cellar) {
        this.eventManager.broadcast({ name: 'cellarListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
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
