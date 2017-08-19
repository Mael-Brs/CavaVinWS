(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .directive('isWineCreator', isWineCreator);

    isWineCreator.$inject = ['Principal'];

    function isWineCreator(Principal) {
        var directive = {
            restrict: 'A',
            link: linkFunc
        };

        return directive;

        function linkFunc(scope, element, attrs) {
            var wine = attrs.isWineCreator;

            var setVisible = function () {
                    element.removeClass('hidden');
                },
                setHidden = function () {
                    element.addClass('hidden');
                },
                defineVisibility = function (reset) {

                    if (reset) {
                        setVisible();
                    }

                    Principal.identity()
                        .then(function (account) {
                            if (account.id === wine.creatorId) {
                                setVisible();
                            } else {
                                setHidden();
                            }
                        });
                };

            if (wine) {
                defineVisibility(true);

                scope.$watch(function() {
                    return Principal.isAuthenticated();
                }, function() {
                    defineVisibility(true);
                });
            }
        }
    }
})();
