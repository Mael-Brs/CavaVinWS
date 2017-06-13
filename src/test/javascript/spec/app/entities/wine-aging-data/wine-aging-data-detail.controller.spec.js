'use strict';

describe('Controller Tests', function() {

    describe('WineAgingData Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockWineAgingData, MockColor, MockRegion;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockWineAgingData = jasmine.createSpy('MockWineAgingData');
            MockColor = jasmine.createSpy('MockColor');
            MockRegion = jasmine.createSpy('MockRegion');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'WineAgingData': MockWineAgingData,
                'Color': MockColor,
                'Region': MockRegion
            };
            createController = function() {
                $injector.get('$controller')("WineAgingDataDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'cavavinApp:wineAgingDataUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
