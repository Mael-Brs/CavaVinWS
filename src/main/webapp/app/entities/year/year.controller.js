(function() {
    'use strict';

    angular
        .module('cavaVinApp')
        .controller('YearController', YearController);

    YearController.$inject = ['$scope', '$state', 'Year', 'YearSearch', '$q'];

    function YearController ($scope, $state, Year, YearSearch, $q) {
        var vm = this;

        vm.years = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function populateYear(nb){
            Year.save({number:nb}, function(){
                if(nb < 2016){
                    populateYear(nb+1);
                }
            });
        }

        function loadAll() {
            Year.query(function(result) {
                vm.years = result;
                vm.searchQuery = null;
                if(!vm.years.length > 0){
                    populateYear(1885);
                }
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
