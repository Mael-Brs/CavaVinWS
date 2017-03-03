(function() {
    'use strict';

    angular
        .module('cavaVinApp')
        .controller('CellarDeleteController',CellarDeleteController);

    CellarDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cellar'];

    function CellarDeleteController($uibModalInstance, entity, Cellar) {
        var vm = this;

        vm.cellar = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Cellar.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
