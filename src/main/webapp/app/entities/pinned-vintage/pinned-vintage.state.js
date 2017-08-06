(function() {
    'use strict';

    angular
        .module('cavavinApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pinned-vintage', {
            parent: 'entity',
            url: '/pinned-vintage',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cavavinApp.pinnedVintage.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pinned-vintage/pinned-vintages.html',
                    controller: 'PinnedVintageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pinnedVintage');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('pinned-vintage-detail', {
            parent: 'pinned-vintage',
            url: '/pinned-vintage/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cavavinApp.pinnedVintage.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pinned-vintage/pinned-vintage-detail.html',
                    controller: 'PinnedVintageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pinnedVintage');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PinnedVintage', function($stateParams, PinnedVintage) {
                    return PinnedVintage.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'pinned-vintage',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('pinned-vintage-detail.edit', {
            parent: 'pinned-vintage-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pinned-vintage/pinned-vintage-dialog.html',
                    controller: 'PinnedVintageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PinnedVintage', function(PinnedVintage) {
                            return PinnedVintage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pinned-vintage.new', {
            parent: 'pinned-vintage',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pinned-vintage/pinned-vintage-dialog.html',
                    controller: 'PinnedVintageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                userId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('pinned-vintage', null, { reload: 'pinned-vintage' });
                }, function() {
                    $state.go('pinned-vintage');
                });
            }]
        })
        .state('pinned-vintage.edit', {
            parent: 'pinned-vintage',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pinned-vintage/pinned-vintage-dialog.html',
                    controller: 'PinnedVintageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PinnedVintage', function(PinnedVintage) {
                            return PinnedVintage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pinned-vintage', null, { reload: 'pinned-vintage' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pinned-vintage.delete', {
            parent: 'pinned-vintage',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pinned-vintage/pinned-vintage-delete-dialog.html',
                    controller: 'PinnedVintageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PinnedVintage', function(PinnedVintage) {
                            return PinnedVintage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pinned-vintage', null, { reload: 'pinned-vintage' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
