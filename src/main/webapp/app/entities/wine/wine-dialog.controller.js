(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('WineDialogController', WineDialogController);

    WineDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Wine', 'Region', 'Color', 'User'];

    function WineDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Wine, Region, Color, User) {
        var vm = this;

        vm.wine = entity;
        vm.clear = clear;
        vm.save = save;
        vm.regions = Region.query();
        vm.colors = Color.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.wine.id !== null) {
                Wine.update(vm.wine, onSaveSuccess, onSaveError);
            } else {
                Wine.save(vm.wine, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cavavinApp:wineUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
