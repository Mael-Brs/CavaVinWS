import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { WineAgingData } from './wine-aging-data.model';
import { WineAgingDataPopupService } from './wine-aging-data-popup.service';
import { WineAgingDataService } from './wine-aging-data.service';

@Component({
    selector: 'jhi-wine-aging-data-delete-dialog',
    templateUrl: './wine-aging-data-delete-dialog.component.html'
})
export class WineAgingDataDeleteDialogComponent {

    wineAgingData: WineAgingData;

    constructor(
        private wineAgingDataService: WineAgingDataService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.wineAgingDataService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'wineAgingDataListModification',
                content: 'Deleted an wineAgingData'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-wine-aging-data-delete-popup',
    template: ''
})
export class WineAgingDataDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private wineAgingDataPopupService: WineAgingDataPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.wineAgingDataPopupService
                .open(WineAgingDataDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
