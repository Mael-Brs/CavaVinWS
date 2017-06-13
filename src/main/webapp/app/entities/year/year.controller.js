(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('YearController', YearController);

    YearController.$inject = ['Year', 'YearSearch'];

    function YearController(Year, YearSearch) {

        var vm = this;

        vm.years = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Year.query(function(result) {
                vm.years = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            YearSearch.query({query: vm.searchQuery}, function(result) {
                vm.years = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
