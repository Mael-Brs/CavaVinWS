(function() {
    'use strict';

    angular
        .module('cavaVinApp')
        .controller('VintageDetailController', VintageDetailController);

    VintageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Vintage', 'Year', 'Wine'];

    function VintageDetailController($scope, $rootScope, $stateParams, previousState, entity, Vintage, Year, Wine) {
        var vm = this;

        vm.vintage = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cavaVinApp:vintageUpdate', function(event, result) {
            vm.vintage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
