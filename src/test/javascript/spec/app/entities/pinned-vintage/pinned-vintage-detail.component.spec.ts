/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CavavinTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PinnedVintageDetailComponent } from '../../../../../../main/webapp/app/entities/pinned-vintage/pinned-vintage-detail.component';
import { PinnedVintageService } from '../../../../../../main/webapp/app/entities/pinned-vintage/pinned-vintage.service';
import { PinnedVintage } from '../../../../../../main/webapp/app/entities/pinned-vintage/pinned-vintage.model';

describe('Component Tests', () => {

    describe('PinnedVintage Management Detail Component', () => {
        let comp: PinnedVintageDetailComponent;
        let fixture: ComponentFixture<PinnedVintageDetailComponent>;
        let service: PinnedVintageService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CavavinTestModule],
                declarations: [PinnedVintageDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PinnedVintageService,
                    JhiEventManager
                ]
            }).overrideTemplate(PinnedVintageDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PinnedVintageDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PinnedVintageService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new PinnedVintage(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.pinnedVintage).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
