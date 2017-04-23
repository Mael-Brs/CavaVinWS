(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('wine-aging-data', {
            parent: 'entity',
            url: '/wine-aging-data',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cavavinApp.wineAgingData.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wine-aging-data/wine-aging-data.html',
                    controller: 'WineAgingDataController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('wineAgingData');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('wine-aging-data-detail', {
            parent: 'wine-aging-data',
            url: '/wine-aging-data/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cavavinApp.wineAgingData.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wine-aging-data/wine-aging-data-detail.html',
                    controller: 'WineAgingDataDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('wineAgingData');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WineAgingData', function($stateParams, WineAgingData) {
                    return WineAgingData.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'wine-aging-data',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('wine-aging-data-detail.edit', {
            parent: 'wine-aging-data-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wine-aging-data/wine-aging-data-dialog.html',
                    controller: 'WineAgingDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WineAgingData', function(WineAgingData) {
                            return WineAgingData.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wine-aging-data.new', {
            parent: 'wine-aging-data',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wine-aging-data/wine-aging-data-dialog.html',
                    controller: 'WineAgingDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                minKeep: null,
                                maxKeep: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('wine-aging-data', null, { reload: 'wine-aging-data' });
                }, function() {
                    $state.go('wine-aging-data');
                });
            }]
        })
        .state('wine-aging-data.edit', {
            parent: 'wine-aging-data',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wine-aging-data/wine-aging-data-dialog.html',
                    controller: 'WineAgingDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WineAgingData', function(WineAgingData) {
                            return WineAgingData.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wine-aging-data', null, { reload: 'wine-aging-data' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wine-aging-data.delete', {
            parent: 'wine-aging-data',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wine-aging-data/wine-aging-data-delete-dialog.html',
                    controller: 'WineAgingDataDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WineAgingData', function(WineAgingData) {
                            return WineAgingData.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wine-aging-data', null, { reload: 'wine-aging-data' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
