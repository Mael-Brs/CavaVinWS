import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { WineInCellar } from './wine-in-cellar.model';
import { WineInCellarPopupService } from './wine-in-cellar-popup.service';
import { WineInCellarService } from './wine-in-cellar.service';

@Component({
    selector: 'jhi-wine-in-cellar-delete-dialog',
    templateUrl: './wine-in-cellar-delete-dialog.component.html'
})
export class WineInCellarDeleteDialogComponent {

    wineInCellar: WineInCellar;

    constructor(
        private wineInCellarService: WineInCellarService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.wineInCellarService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'wineInCellarListModification',
                content: 'Deleted an wineInCellar'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-wine-in-cellar-delete-popup',
    template: ''
})
export class WineInCellarDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private wineInCellarPopupService: WineInCellarPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.wineInCellarPopupService
                .open(WineInCellarDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
