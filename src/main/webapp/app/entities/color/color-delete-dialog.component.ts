import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Color } from './color.model';
import { ColorPopupService } from './color-popup.service';
import { ColorService } from './color.service';

@Component({
    selector: 'jhi-color-delete-dialog',
    templateUrl: './color-delete-dialog.component.html'
})
export class ColorDeleteDialogComponent {

    color: Color;

    constructor(
        private colorService: ColorService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.colorService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'colorListModification',
                content: 'Deleted an color'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-color-delete-popup',
    template: ''
})
export class ColorDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private colorPopupService: ColorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.colorPopupService
                .open(ColorDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
