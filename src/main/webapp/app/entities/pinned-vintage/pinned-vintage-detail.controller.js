(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('PinnedVintageDetailController', PinnedVintageDetailController);

    PinnedVintageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PinnedVintage', 'Vintage'];

    function PinnedVintageDetailController($scope, $rootScope, $stateParams, previousState, entity, PinnedVintage, Vintage) {
        var vm = this;

        vm.pinnedVintage = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cavavinApp:pinnedVintageUpdate', function(event, result) {
            vm.pinnedVintage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
