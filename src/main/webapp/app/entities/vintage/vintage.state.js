(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('vintage', {
            parent: 'entity',
            url: '/vintage',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cavavinApp.vintage.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vintage/vintages.html',
                    controller: 'VintageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('vintage');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('vintage-detail', {
            parent: 'vintage',
            url: '/vintage/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cavavinApp.vintage.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vintage/vintage-detail.html',
                    controller: 'VintageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('vintage');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Vintage', function($stateParams, Vintage) {
                    return Vintage.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'vintage',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('vintage-detail.edit', {
            parent: 'vintage-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vintage/vintage-dialog.html',
                    controller: 'VintageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Vintage', function(Vintage) {
                            return Vintage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vintage.new', {
            parent: 'vintage',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vintage/vintage-dialog.html',
                    controller: 'VintageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                bareCode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('vintage', null, { reload: 'vintage' });
                }, function() {
                    $state.go('vintage');
                });
            }]
        })
        .state('vintage.edit', {
            parent: 'vintage',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vintage/vintage-dialog.html',
                    controller: 'VintageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Vintage', function(Vintage) {
                            return Vintage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vintage', null, { reload: 'vintage' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vintage.delete', {
            parent: 'vintage',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vintage/vintage-delete-dialog.html',
                    controller: 'VintageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Vintage', function(Vintage) {
                            return Vintage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vintage', null, { reload: 'vintage' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
