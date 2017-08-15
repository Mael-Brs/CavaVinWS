(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('VintageDialogController', VintageDialogController);

    VintageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Vintage', 'Wine'];

    function VintageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Vintage, Wine) {
        var vm = this;

        vm.vintage = entity;
        vm.clear = clear;
        vm.save = save;
        vm.wines = Wine.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.vintage.id !== null) {
                Vintage.update(vm.vintage, onSaveSuccess, onSaveError);
            } else {
                Vintage.save(vm.vintage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cavavinApp:vintageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
