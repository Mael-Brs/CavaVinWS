(function() {
    'use strict';

    angular
        .module('cavaVinApp')
        .controller('YearDeleteController',YearDeleteController);

    YearDeleteController.$inject = ['$uibModalInstance', 'entity', 'Year'];

    function YearDeleteController($uibModalInstance, entity, Year) {
        var vm = this;

        vm.year = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Year.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
