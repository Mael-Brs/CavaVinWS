(function() {
    'use strict';

    angular
        .module('cavaVinApp')
        .factory('VintageSearch', VintageSearch);

    VintageSearch.$inject = ['$resource'];

    function VintageSearch($resource) {
        var resourceUrl =  'api/_search/vintages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
