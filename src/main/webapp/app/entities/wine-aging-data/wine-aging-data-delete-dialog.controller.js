(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('WineAgingDataDeleteController',WineAgingDataDeleteController);

    WineAgingDataDeleteController.$inject = ['$uibModalInstance', 'entity', 'WineAgingData'];

    function WineAgingDataDeleteController($uibModalInstance, entity, WineAgingData) {
        var vm = this;

        vm.wineAgingData = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WineAgingData.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
