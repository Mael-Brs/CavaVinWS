(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('VintageDetailController', VintageDetailController);

    VintageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Vintage', 'Wine'];

    function VintageDetailController($scope, $rootScope, $stateParams, previousState, entity, Vintage, Wine) {
        var vm = this;

        vm.vintage = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cavavinApp:vintageUpdate', function(event, result) {
            vm.vintage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
