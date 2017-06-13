(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('WineInCellarDialogController', WineInCellarDialogController);

    WineInCellarDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WineInCellar', 'Cellar', 'Vintage'];

    function WineInCellarDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WineInCellar, Cellar, Vintage) {
        var vm = this;

        vm.wineInCellar = entity;
        vm.clear = clear;
        vm.save = save;
        vm.cellars = Cellar.query();
        vm.vintages = Vintage.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.wineInCellar.id !== null) {
                WineInCellar.update(vm.wineInCellar, onSaveSuccess, onSaveError);
            } else {
                WineInCellar.save(vm.wineInCellar, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cavavinApp:wineInCellarUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
