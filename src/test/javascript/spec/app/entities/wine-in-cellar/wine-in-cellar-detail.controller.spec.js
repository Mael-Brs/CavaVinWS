'use strict';

describe('Controller Tests', function() {

    describe('WineInCellar Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockWineInCellar, MockCellar, MockVintage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockWineInCellar = jasmine.createSpy('MockWineInCellar');
            MockCellar = jasmine.createSpy('MockCellar');
            MockVintage = jasmine.createSpy('MockVintage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'WineInCellar': MockWineInCellar,
                'Cellar': MockCellar,
                'Vintage': MockVintage
            };
            createController = function() {
                $injector.get('$controller')("WineInCellarDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'cavavinApp:wineInCellarUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
