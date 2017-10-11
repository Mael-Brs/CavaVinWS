/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CavavinTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { VintageDetailComponent } from '../../../../../../main/webapp/app/entities/vintage/vintage-detail.component';
import { VintageService } from '../../../../../../main/webapp/app/entities/vintage/vintage.service';
import { Vintage } from '../../../../../../main/webapp/app/entities/vintage/vintage.model';

describe('Component Tests', () => {

    describe('Vintage Management Detail Component', () => {
        let comp: VintageDetailComponent;
        let fixture: ComponentFixture<VintageDetailComponent>;
        let service: VintageService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CavavinTestModule],
                declarations: [VintageDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    VintageService,
                    JhiEventManager
                ]
            }).overrideTemplate(VintageDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VintageDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VintageService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Vintage(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.vintage).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
