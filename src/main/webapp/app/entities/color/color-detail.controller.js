(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('ColorDetailController', ColorDetailController);

    ColorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Color'];

    function ColorDetailController($scope, $rootScope, $stateParams, previousState, entity, Color) {
        var vm = this;

        vm.color = entity;
        vm.previousState = previousState;

        var unsubscribe = $rootScope.$on('cavavinApp:colorUpdate', function(event, result) {
            vm.color = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
