(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .controller('ColorController', ColorController);

    ColorController.$inject = ['Color', 'ColorSearch'];

    function ColorController(Color, ColorSearch) {

        var vm = this;

        vm.colors = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Color.query(function(result) {
                vm.colors = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ColorSearch.query({query: vm.searchQuery}, function(result) {
                vm.colors = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
