/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CavavinTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { WineInCellarDetailComponent } from '../../../../../../main/webapp/app/entities/wine-in-cellar/wine-in-cellar-detail.component';
import { WineInCellarService } from '../../../../../../main/webapp/app/entities/wine-in-cellar/wine-in-cellar.service';
import { WineInCellar } from '../../../../../../main/webapp/app/entities/wine-in-cellar/wine-in-cellar.model';

describe('Component Tests', () => {

    describe('WineInCellar Management Detail Component', () => {
        let comp: WineInCellarDetailComponent;
        let fixture: ComponentFixture<WineInCellarDetailComponent>;
        let service: WineInCellarService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CavavinTestModule],
                declarations: [WineInCellarDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    WineInCellarService,
                    JhiEventManager
                ]
            }).overrideTemplate(WineInCellarDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WineInCellarDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WineInCellarService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new WineInCellar(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.wineInCellar).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
