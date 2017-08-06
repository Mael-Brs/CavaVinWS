(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('CellarDetailController', CellarDetailController);

    CellarDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cellar'];

    function CellarDetailController($scope, $rootScope, $stateParams, previousState, entity, Cellar) {
        var vm = this;

        vm.cellar = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cavavinApp:cellarUpdate', function(event, result) {
            vm.cellar = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
