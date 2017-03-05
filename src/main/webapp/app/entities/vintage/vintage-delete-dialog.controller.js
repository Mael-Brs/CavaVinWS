(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('VintageDeleteController',VintageDeleteController);

    VintageDeleteController.$inject = ['$uibModalInstance', 'entity', 'Vintage'];

    function VintageDeleteController($uibModalInstance, entity, Vintage) {
        var vm = this;

        vm.vintage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Vintage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
