(function() {
    'use strict';

    angular
        .module('cavaVinApp')
        .controller('CellarDetailController', CellarDetailController);

    CellarDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cellar', 'User'];

    function CellarDetailController($scope, $rootScope, $stateParams, previousState, entity, Cellar, User) {
        var vm = this;

        vm.cellar = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cavaVinApp:cellarUpdate', function(event, result) {
            vm.cellar = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
