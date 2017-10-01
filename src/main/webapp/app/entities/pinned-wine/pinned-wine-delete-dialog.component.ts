import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { PinnedWine } from './pinned-wine.model';
import { PinnedWinePopupService } from './pinned-wine-popup.service';
import { PinnedWineService } from './pinned-wine.service';

@Component({
    selector: 'jhi-pinned-wine-delete-dialog',
    templateUrl: './pinned-wine-delete-dialog.component.html'
})
export class PinnedWineDeleteDialogComponent {

    pinnedWine: PinnedWine;

    constructor(
        private pinnedWineService: PinnedWineService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.pinnedWineService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'pinnedWineListModification',
                content: 'Deleted an pinnedWine'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-pinned-wine-delete-popup',
    template: ''
})
export class PinnedWineDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pinnedWinePopupService: PinnedWinePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.pinnedWinePopupService
                .open(PinnedWineDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
