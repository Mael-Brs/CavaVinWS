(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .factory('ColorSearch', ColorSearch);

    ColorSearch.$inject = ['$resource'];

    function ColorSearch($resource) {
        var resourceUrl =  'api/_search/colors/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
