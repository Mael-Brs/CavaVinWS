import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Wine } from './wine.model';
import { WinePopupService } from './wine-popup.service';
import { WineService } from './wine.service';

@Component({
    selector: 'jhi-wine-delete-dialog',
    templateUrl: './wine-delete-dialog.component.html'
})
export class WineDeleteDialogComponent {

    wine: Wine;

    constructor(
        private wineService: WineService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.wineService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'wineListModification',
                content: 'Deleted an wine'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-wine-delete-popup',
    template: ''
})
export class WineDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private winePopupService: WinePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.winePopupService
                .open(WineDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
