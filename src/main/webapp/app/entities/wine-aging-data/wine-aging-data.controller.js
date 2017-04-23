(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('WineAgingDataController', WineAgingDataController);

    WineAgingDataController.$inject = ['WineAgingData', 'WineAgingDataSearch'];

    function WineAgingDataController(WineAgingData, WineAgingDataSearch) {

        var vm = this;

        vm.wineAgingData = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            WineAgingData.query(function(result) {
                vm.wineAgingData = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            WineAgingDataSearch.query({query: vm.searchQuery}, function(result) {
                vm.wineAgingData = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
