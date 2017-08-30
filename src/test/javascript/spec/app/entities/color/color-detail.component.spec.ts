/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CavavinTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ColorDetailComponent } from '../../../../../../main/webapp/app/entities/color/color-detail.component';
import { ColorService } from '../../../../../../main/webapp/app/entities/color/color.service';
import { Color } from '../../../../../../main/webapp/app/entities/color/color.model';

describe('Component Tests', () => {

    describe('Color Management Detail Component', () => {
        let comp: ColorDetailComponent;
        let fixture: ComponentFixture<ColorDetailComponent>;
        let service: ColorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CavavinTestModule],
                declarations: [ColorDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ColorService,
                    JhiEventManager
                ]
            }).overrideTemplate(ColorDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ColorDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ColorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Color(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.color).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
