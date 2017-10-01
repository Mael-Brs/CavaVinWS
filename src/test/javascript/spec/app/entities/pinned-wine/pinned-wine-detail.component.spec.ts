/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CavavinTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PinnedWineDetailComponent } from '../../../../../../main/webapp/app/entities/pinned-wine/pinned-wine-detail.component';
import { PinnedWineService } from '../../../../../../main/webapp/app/entities/pinned-wine/pinned-wine.service';
import { PinnedWine } from '../../../../../../main/webapp/app/entities/pinned-wine/pinned-wine.model';

describe('Component Tests', () => {

    describe('PinnedWine Management Detail Component', () => {
        let comp: PinnedWineDetailComponent;
        let fixture: ComponentFixture<PinnedWineDetailComponent>;
        let service: PinnedWineService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CavavinTestModule],
                declarations: [PinnedWineDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PinnedWineService,
                    JhiEventManager
                ]
            }).overrideTemplate(PinnedWineDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PinnedWineDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PinnedWineService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new PinnedWine(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.pinnedWine).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
