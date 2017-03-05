(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('WineInCellarController', WineInCellarController);

    WineInCellarController.$inject = ['WineInCellar', 'WineInCellarSearch'];

    function WineInCellarController(WineInCellar, WineInCellarSearch) {

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
