/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CavavinTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { WineAgingDataDetailComponent } from '../../../../../../main/webapp/app/entities/wine-aging-data/wine-aging-data-detail.component';
import { WineAgingDataService } from '../../../../../../main/webapp/app/entities/wine-aging-data/wine-aging-data.service';
import { WineAgingData } from '../../../../../../main/webapp/app/entities/wine-aging-data/wine-aging-data.model';

describe('Component Tests', () => {

    describe('WineAgingData Management Detail Component', () => {
        let comp: WineAgingDataDetailComponent;
        let fixture: ComponentFixture<WineAgingDataDetailComponent>;
        let service: WineAgingDataService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CavavinTestModule],
                declarations: [WineAgingDataDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    WineAgingDataService,
                    JhiEventManager
                ]
            }).overrideTemplate(WineAgingDataDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WineAgingDataDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WineAgingDataService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new WineAgingData(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.wineAgingData).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
