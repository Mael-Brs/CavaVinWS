(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('wine', {
            parent: 'entity',
            url: '/wine',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cavavinApp.wine.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wine/wines.html',
                    controller: 'WineController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('wine');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('wine-detail', {
            parent: 'wine',
            url: '/wine/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cavavinApp.wine.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wine/wine-detail.html',
                    controller: 'WineDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('wine');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Wine', function($stateParams, Wine) {
                    return Wine.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'wine',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('wine-detail.edit', {
            parent: 'wine-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wine/wine-dialog.html',
                    controller: 'WineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Wine', function(Wine) {
                            return Wine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wine.new', {
            parent: 'wine',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wine/wine-dialog.html',
                    controller: 'WineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                appellation: null,
                                producer: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('wine', null, { reload: 'wine' });
                }, function() {
                    $state.go('wine');
                });
            }]
        })
        .state('wine.edit', {
            parent: 'wine',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wine/wine-dialog.html',
                    controller: 'WineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Wine', function(Wine) {
                            return Wine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wine', null, { reload: 'wine' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wine.delete', {
            parent: 'wine',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wine/wine-delete-dialog.html',
                    controller: 'WineDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Wine', function(Wine) {
                            return Wine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wine', null, { reload: 'wine' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
