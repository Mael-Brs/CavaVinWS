(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .factory('PinnedVintageSearch', PinnedVintageSearch);

    PinnedVintageSearch.$inject = ['$resource'];

    function PinnedVintageSearch($resource) {
        var resourceUrl =  'api/_search/pinned-vintages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
