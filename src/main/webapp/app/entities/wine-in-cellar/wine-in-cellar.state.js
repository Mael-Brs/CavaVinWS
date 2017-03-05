(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('wine-in-cellar', {
            parent: 'entity',
            url: '/wine-in-cellar',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cavavinApp.wineInCellar.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wine-in-cellar/wine-in-cellars.html',
                    controller: 'WineInCellarController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('wineInCellar');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('wine-in-cellar-detail', {
            parent: 'wine-in-cellar',
            url: '/wine-in-cellar/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cavavinApp.wineInCellar.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wine-in-cellar/wine-in-cellar-detail.html',
                    controller: 'WineInCellarDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('wineInCellar');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WineInCellar', function($stateParams, WineInCellar) {
                    return WineInCellar.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'wine-in-cellar',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('wine-in-cellar-detail.edit', {
            parent: 'wine-in-cellar-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wine-in-cellar/wine-in-cellar-dialog.html',
                    controller: 'WineInCellarDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WineInCellar', function(WineInCellar) {
                            return WineInCellar.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wine-in-cellar.new', {
            parent: 'wine-in-cellar',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wine-in-cellar/wine-in-cellar-dialog.html',
                    controller: 'WineInCellarDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                minKeep: null,
                                maxKeep: null,
                                price: null,
                                quantity: null,
                                comments: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('wine-in-cellar', null, { reload: 'wine-in-cellar' });
                }, function() {
                    $state.go('wine-in-cellar');
                });
            }]
        })
        .state('wine-in-cellar.edit', {
            parent: 'wine-in-cellar',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wine-in-cellar/wine-in-cellar-dialog.html',
                    controller: 'WineInCellarDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WineInCellar', function(WineInCellar) {
                            return WineInCellar.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wine-in-cellar', null, { reload: 'wine-in-cellar' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wine-in-cellar.delete', {
            parent: 'wine-in-cellar',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wine-in-cellar/wine-in-cellar-delete-dialog.html',
                    controller: 'WineInCellarDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WineInCellar', function(WineInCellar) {
                            return WineInCellar.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wine-in-cellar', null, { reload: 'wine-in-cellar' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
