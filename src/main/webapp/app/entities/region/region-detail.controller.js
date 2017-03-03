(function() {
    'use strict';

    angular
        .module('cavaVinApp')
        .controller('RegionDetailController', RegionDetailController);

    RegionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Region'];

    function RegionDetailController($scope, $rootScope, $stateParams, previousState, entity, Region) {
        var vm = this;

        vm.region = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cavaVinApp:regionUpdate', function(event, result) {
            vm.region = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
