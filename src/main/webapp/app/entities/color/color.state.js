(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('color', {
            parent: 'entity',
            url: '/color',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'cavavinApp.color.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/color/colors.html',
                    controller: 'ColorController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('color');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('color-detail', {
            parent: 'color',
            url: '/color/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cavavinApp.color.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/color/color-detail.html',
                    controller: 'ColorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('color');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Color', function($stateParams, Color) {
                    return Color.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'color',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('color-detail.edit', {
            parent: 'color-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/color/color-dialog.html',
                    controller: 'ColorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Color', function(Color) {
                            return Color.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('color.new', {
            parent: 'color',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/color/color-dialog.html',
                    controller: 'ColorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                colorName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('color', null, { reload: 'color' });
                }, function() {
                    $state.go('color');
                });
            }]
        })
        .state('color.edit', {
            parent: 'color',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/color/color-dialog.html',
                    controller: 'ColorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Color', function(Color) {
                            return Color.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('color', null, { reload: 'color' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('color.delete', {
            parent: 'color',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/color/color-delete-dialog.html',
                    controller: 'ColorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Color', function(Color) {
                            return Color.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('color', null, { reload: 'color' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
