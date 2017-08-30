/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CavavinTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { RegionDetailComponent } from '../../../../../../main/webapp/app/entities/region/region-detail.component';
import { RegionService } from '../../../../../../main/webapp/app/entities/region/region.service';
import { Region } from '../../../../../../main/webapp/app/entities/region/region.model';

describe('Component Tests', () => {

    describe('Region Management Detail Component', () => {
        let comp: RegionDetailComponent;
        let fixture: ComponentFixture<RegionDetailComponent>;
        let service: RegionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CavavinTestModule],
                declarations: [RegionDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    RegionService,
                    JhiEventManager
                ]
            }).overrideTemplate(RegionDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RegionDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RegionService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Region(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.region).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
