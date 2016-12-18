(function() {
    'use strict';

    angular
        .module('cavaVinApp')
        .controller('YearDetailController', YearDetailController);

    YearDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Year'];

    function YearDetailController($scope, $rootScope, $stateParams, previousState, entity, Year) {
        var vm = this;

        vm.year = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cavaVinApp:yearUpdate', function(event, result) {
            vm.year = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
