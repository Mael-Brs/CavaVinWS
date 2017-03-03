(function() {
    'use strict';

    angular
        .module('cavaVinApp')
        .controller('WineInCellarDetailController', WineInCellarDetailController);

    WineInCellarDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WineInCellar', 'Cellar', 'Vintage'];

    function WineInCellarDetailController($scope, $rootScope, $stateParams, previousState, entity, WineInCellar, Cellar, Vintage) {
        var vm = this;

        vm.wineInCellar = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cavaVinApp:wineInCellarUpdate', function(event, result) {
            vm.wineInCellar = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
