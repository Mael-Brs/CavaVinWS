(function() {
    'use strict';

    angular
        .module('cavaVinApp')
        .controller('WineInCellarController', WineInCellarController);

    WineInCellarController.$inject = ['$scope', '$state', 'WineInCellar', 'WineInCellarSearch'];

    function WineInCellarController ($scope, $state, WineInCellar, WineInCellarSearch) {
        var vm = this;

        vm.wineInCellars = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            WineInCellar.query(function(result) {
                vm.wineInCellars = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            WineInCellarSearch.query({query: vm.searchQuery}, function(result) {
                vm.wineInCellars = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
