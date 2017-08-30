/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CavavinTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { WineDetailComponent } from '../../../../../../main/webapp/app/entities/wine/wine-detail.component';
import { WineService } from '../../../../../../main/webapp/app/entities/wine/wine.service';
import { Wine } from '../../../../../../main/webapp/app/entities/wine/wine.model';

describe('Component Tests', () => {

    describe('Wine Management Detail Component', () => {
        let comp: WineDetailComponent;
        let fixture: ComponentFixture<WineDetailComponent>;
        let service: WineService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CavavinTestModule],
                declarations: [WineDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    WineService,
                    JhiEventManager
                ]
            }).overrideTemplate(WineDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WineDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WineService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Wine(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.wine).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
