(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('PinnedVintageDialogController', PinnedVintageDialogController);

    PinnedVintageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PinnedVintage', 'Vintage', 'User'];

    function PinnedVintageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PinnedVintage, Vintage, User) {
        var vm = this;

        vm.pinnedVintage = entity;
        vm.clear = clear;
        vm.save = save;
        vm.vintages = Vintage.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.pinnedVintage.id !== null) {
                PinnedVintage.update(vm.pinnedVintage, onSaveSuccess, onSaveError);
            } else {
                PinnedVintage.save(vm.pinnedVintage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cavavinApp:pinnedVintageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
