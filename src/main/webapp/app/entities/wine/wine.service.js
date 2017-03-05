(function() {
    'use strict';
    angular
        .module('cavavinApp')
        .factory('Wine', Wine);

    Wine.$inject = ['$resource'];

    function Wine ($resource) {
        var resourceUrl =  'api/wines/:id';

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
