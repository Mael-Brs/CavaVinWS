(function() {
    'use strict';

    angular
        .module('cavaVinApp')
        .controller('ColorDetailController', ColorDetailController);

    ColorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Color'];

    function ColorDetailController($scope, $rootScope, $stateParams, previousState, entity, Color) {
        var vm = this;

        vm.color = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cavaVinApp:colorUpdate', function(event, result) {
            vm.color = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
