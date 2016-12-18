(function() {
    'use strict';

    angular
        .module('cavaVinApp')
        .factory('WineSearch', WineSearch);

    WineSearch.$inject = ['$resource'];

    function WineSearch($resource) {
        var resourceUrl =  'api/_search/wines/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
