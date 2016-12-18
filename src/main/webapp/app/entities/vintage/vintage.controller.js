(function() {
    'use strict';

    angular
        .module('cavaVinApp')
        .controller('VintageController', VintageController);

    VintageController.$inject = ['$scope', '$state', 'Vintage', 'VintageSearch'];

    function VintageController ($scope, $state, Vintage, VintageSearch) {
        var vm = this;

        vm.vintages = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Vintage.query(function(result) {
                vm.vintages = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            VintageSearch.query({query: vm.searchQuery}, function(result) {
                vm.vintages = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
