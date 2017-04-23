(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .factory('WineAgingDataSearch', WineAgingDataSearch);

    WineAgingDataSearch.$inject = ['$resource'];

    function WineAgingDataSearch($resource) {
        var resourceUrl =  'api/_search/wine-aging-data/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
