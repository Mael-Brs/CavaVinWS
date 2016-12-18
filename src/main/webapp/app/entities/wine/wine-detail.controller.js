(function() {
    'use strict';

    angular
        .module('cavaVinApp')
        .controller('WineDetailController', WineDetailController);

    WineDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Wine', 'Region', 'Color'];

    function WineDetailController($scope, $rootScope, $stateParams, previousState, entity, Wine, Region, Color) {
        var vm = this;

        vm.wine = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cavaVinApp:wineUpdate', function(event, result) {
            vm.wine = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
