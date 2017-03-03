(function() {
    'use strict';

    angular
        .module('cavaVinApp')
        .controller('CellarController', CellarController);

    CellarController.$inject = ['$scope', '$state', 'Cellar', 'CellarSearch'];

    function CellarController ($scope, $state, Cellar, CellarSearch) {
        var vm = this;

        vm.cellars = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Cellar.query(function(result) {
                vm.cellars = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CellarSearch.query({query: vm.searchQuery}, function(result) {
                vm.cellars = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
