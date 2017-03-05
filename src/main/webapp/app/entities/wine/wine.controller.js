(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('WineController', WineController);

    WineController.$inject = ['Wine', 'WineSearch'];

    function WineController(Wine, WineSearch) {

        var vm = this;

        vm.wines = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Wine.query(function(result) {
                vm.wines = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            WineSearch.query({query: vm.searchQuery}, function(result) {
                vm.wines = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
