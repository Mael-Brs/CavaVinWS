/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CavavinTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CellarDetailComponent } from '../../../../../../main/webapp/app/entities/cellar/cellar-detail.component';
import { CellarService } from '../../../../../../main/webapp/app/entities/cellar/cellar.service';
import { Cellar } from '../../../../../../main/webapp/app/entities/cellar/cellar.model';

describe('Component Tests', () => {

    describe('Cellar Management Detail Component', () => {
        let comp: CellarDetailComponent;
        let fixture: ComponentFixture<CellarDetailComponent>;
        let service: CellarService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CavavinTestModule],
                declarations: [CellarDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CellarService,
                    JhiEventManager
                ]
            }).overrideTemplate(CellarDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CellarDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CellarService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Cellar(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.cellar).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
