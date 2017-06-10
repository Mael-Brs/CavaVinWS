(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('PinnedVintageDeleteController',PinnedVintageDeleteController);

    PinnedVintageDeleteController.$inject = ['$uibModalInstance', 'entity', 'PinnedVintage'];

    function PinnedVintageDeleteController($uibModalInstance, entity, PinnedVintage) {
        var vm = this;

        vm.pinnedVintage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PinnedVintage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
