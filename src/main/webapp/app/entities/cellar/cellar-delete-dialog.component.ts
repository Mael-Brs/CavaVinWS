import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Cellar } from './cellar.model';
import { CellarPopupService } from './cellar-popup.service';
import { CellarService } from './cellar.service';

@Component({
    selector: 'jhi-cellar-delete-dialog',
    templateUrl: './cellar-delete-dialog.component.html'
})
export class CellarDeleteDialogComponent {

    cellar: Cellar;

    constructor(
        private cellarService: CellarService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.cellarService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'cellarListModification',
                content: 'Deleted an cellar'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cellar-delete-popup',
    template: ''
})
export class CellarDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cellarPopupService: CellarPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.cellarPopupService
                .open(CellarDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
