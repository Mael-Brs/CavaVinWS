(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cellar', {
            parent: 'entity',
            url: '/cellar',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cavavinApp.cellar.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cellar/cellars.html',
                    controller: 'CellarController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cellar');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cellar-detail', {
            parent: 'cellar',
            url: '/cellar/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cavavinApp.cellar.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cellar/cellar-detail.html',
                    controller: 'CellarDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cellar');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Cellar', function($stateParams, Cellar) {
                    return Cellar.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cellar',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cellar-detail.edit', {
            parent: 'cellar-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cellar/cellar-dialog.html',
                    controller: 'CellarDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cellar', function(Cellar) {
                            return Cellar.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cellar.new', {
            parent: 'cellar',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cellar/cellar-dialog.html',
                    controller: 'CellarDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                capacity: null,
                                userId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cellar', null, { reload: 'cellar' });
                }, function() {
                    $state.go('cellar');
                });
            }]
        })
        .state('cellar.edit', {
            parent: 'cellar',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cellar/cellar-dialog.html',
                    controller: 'CellarDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cellar', function(Cellar) {
                            return Cellar.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cellar', null, { reload: 'cellar' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cellar.delete', {
            parent: 'cellar',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cellar/cellar-delete-dialog.html',
                    controller: 'CellarDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cellar', function(Cellar) {
                            return Cellar.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cellar', null, { reload: 'cellar' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
