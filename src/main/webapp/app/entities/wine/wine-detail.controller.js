(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('WineDetailController', WineDetailController);

    WineDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Wine', 'Region', 'Color'];

    function WineDetailController($scope, $rootScope, $stateParams, previousState, entity, Wine, Region, Color) {
        var vm = this;

        vm.wine = entity;
        vm.previousState = previousState;

        var unsubscribe = $rootScope.$on('cavavinApp:wineUpdate', function(event, result) {
            vm.wine = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
