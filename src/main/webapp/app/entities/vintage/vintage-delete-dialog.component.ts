import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Vintage } from './vintage.model';
import { VintagePopupService } from './vintage-popup.service';
import { VintageService } from './vintage.service';

@Component({
    selector: 'jhi-vintage-delete-dialog',
    templateUrl: './vintage-delete-dialog.component.html'
})
export class VintageDeleteDialogComponent {

    vintage: Vintage;

    constructor(
        private vintageService: VintageService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.vintageService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'vintageListModification',
                content: 'Deleted an vintage'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-vintage-delete-popup',
    template: ''
})
export class VintageDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private vintagePopupService: VintagePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.vintagePopupService
                .open(VintageDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
