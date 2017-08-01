(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('WineInCellarDetailController', WineInCellarDetailController);

    WineInCellarDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WineInCellar', 'Vintage'];

    function WineInCellarDetailController($scope, $rootScope, $stateParams, previousState, entity, WineInCellar, Vintage) {
        var vm = this;

        vm.wineInCellar = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cavavinApp:wineInCellarUpdate', function(event, result) {
            vm.wineInCellar = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
