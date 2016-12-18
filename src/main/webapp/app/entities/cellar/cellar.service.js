(function() {
    'use strict';
    angular
        .module('cavaVinApp')
        .factory('Cellar', Cellar);

    Cellar.$inject = ['$resource'];

    function Cellar ($resource) {
        var resourceUrl =  'api/cellars/:id';

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
