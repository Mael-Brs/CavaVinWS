(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('WineInCellarDeleteController',WineInCellarDeleteController);

    WineInCellarDeleteController.$inject = ['$uibModalInstance', 'entity', 'WineInCellar'];

    function WineInCellarDeleteController($uibModalInstance, entity, WineInCellar) {
        var vm = this;

        vm.wineInCellar = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WineInCellar.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
