(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('PinnedVintageController', PinnedVintageController);

    PinnedVintageController.$inject = ['PinnedVintage', 'PinnedVintageSearch'];

    function PinnedVintageController(PinnedVintage, PinnedVintageSearch) {

        var vm = this;

        vm.pinnedVintages = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            PinnedVintage.query(function(result) {
                vm.pinnedVintages = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PinnedVintageSearch.query({query: vm.searchQuery}, function(result) {
                vm.pinnedVintages = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
