import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { PinnedVintage } from './pinned-vintage.model';
import { PinnedVintagePopupService } from './pinned-vintage-popup.service';
import { PinnedVintageService } from './pinned-vintage.service';

@Component({
    selector: 'jhi-pinned-vintage-delete-dialog',
    templateUrl: './pinned-vintage-delete-dialog.component.html'
})
export class PinnedVintageDeleteDialogComponent {

    pinnedVintage: PinnedVintage;

    constructor(
        private pinnedVintageService: PinnedVintageService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.pinnedVintageService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'pinnedVintageListModification',
                content: 'Deleted an pinnedVintage'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-pinned-vintage-delete-popup',
    template: ''
})
export class PinnedVintageDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pinnedVintagePopupService: PinnedVintagePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.pinnedVintagePopupService
                .open(PinnedVintageDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
