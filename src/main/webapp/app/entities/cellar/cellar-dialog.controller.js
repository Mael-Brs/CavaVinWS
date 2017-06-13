(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('CellarDialogController', CellarDialogController);

    CellarDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Cellar', 'User'];

    function CellarDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Cellar, User) {
        var vm = this;

        vm.cellar = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cellar.id !== null) {
                Cellar.update(vm.cellar, onSaveSuccess, onSaveError);
            } else {
                Cellar.save(vm.cellar, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cavavinApp:cellarUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
