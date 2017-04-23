(function() {
    'use strict';
    angular
        .module('cavavinApp')
        .factory('WineAgingData', WineAgingData);

    WineAgingData.$inject = ['$resource'];

    function WineAgingData ($resource) {
        var resourceUrl =  'api/wine-aging-data/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
