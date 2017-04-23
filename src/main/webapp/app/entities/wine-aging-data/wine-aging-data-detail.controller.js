(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('WineAgingDataDetailController', WineAgingDataDetailController);

    WineAgingDataDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WineAgingData', 'Color', 'Region'];

    function WineAgingDataDetailController($scope, $rootScope, $stateParams, previousState, entity, WineAgingData, Color, Region) {
        var vm = this;

        vm.wineAgingData = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cavavinApp:wineAgingDataUpdate', function(event, result) {
            vm.wineAgingData = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
