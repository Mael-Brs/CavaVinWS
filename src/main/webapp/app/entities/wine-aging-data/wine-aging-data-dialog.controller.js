(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('WineAgingDataDialogController', WineAgingDataDialogController);

    WineAgingDataDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WineAgingData', 'Color', 'Region'];

    function WineAgingDataDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WineAgingData, Color, Region) {
        var vm = this;

        vm.wineAgingData = entity;
        vm.clear = clear;
        vm.save = save;
        vm.colors = Color.query();
        vm.regions = Region.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.wineAgingData.id !== null) {
                WineAgingData.update(vm.wineAgingData, onSaveSuccess, onSaveError);
            } else {
                WineAgingData.save(vm.wineAgingData, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cavavinApp:wineAgingDataUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
