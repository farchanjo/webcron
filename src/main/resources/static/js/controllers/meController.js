angular.module('webCronApp')
    .controller('MeCtrl', function ($scope, $location, $log, $uibModal, UsersService, LoginService, ShareService) {
        $log.debug("Me Controller has been load.");
        $scope.pageSize = 5;
        $scope.currentPage = 1;
        $scope.reloadTime = 5;

        $scope.saveUser = function (form) {
            if (form.$valid) {
                var user = {};
                user.id = $scope.me.id;
                if ($scope.me.name !== undefined &&
                    $scope.me.name.length > 0) {
                    user.name = $scope.me.name;
                }
                if ($scope.me.username !== undefined &&
                    $scope.me.username.length > 0) {
                    user.username = $scope.me.username;
                }
                if ($scope.me.email !== undefined &&
                    $scope.me.email.length > 0) {
                    user.email = $scope.me.email;
                }
                if ($scope.me.password !== undefined &&
                    $scope.me.password.length > 0) {
                    user.password = $scope.me.password;
                }

                UsersService.save(user)
                    .then(function (res) {
                        $scope.me = res.data;
                        ShareService.prepForBroadcast("userSaved");
                    }, function () {
                        $location.path('/loginPage');
                    });
            }
        };
        $scope.listUsers = function (page, limit) {
            UsersService.listUsers(limit, page)
                .then(function (res) {
                    $scope.users = res.data.content;
                    $scope.totalItems = res.data.totalElements;
                    $scope.smallnumPages = res.data.totalPages;
                }, function () {
                    $log.debug("You dont have access.");
                })
        };
        $scope.loadUser = function () {
            UsersService.me()
                .then(function (res) {
                    $scope.me = res.data;
                }, function () {
                    $location.path('/loginPage');
                });
        };
        $scope.loadUser();

        $scope.listUsers($scope.currentPage, $scope.pageSize);

        $scope.logout = function () {
            LoginService.doLogout().then(function () {
                $location.path('/loginPage');
                ShareService.prepForBroadcast("logoutdone");
            });
        };

        $scope.openModal = function (user, size, parentSelector) {
            var parentElem = parentSelector ?
                angular.element($document[0].querySelector('.modal-demo ' + parentSelector)) : undefined;
            var instance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: 'modalUser.html',
                controller: 'ModalUserCtrl',
                controllerAs: 'modalUser',
                size: size,
                appendTo: parentElem,
                resolve: {
                    user: function () {
                        return user;
                    }
                }
            });
            instance.result.then(function () {
            }, function () {
                $log.debug('Modal closed');
                $scope.listUsers($scope.currentPage, $scope.pageSize);
            });
        };

        $scope.pageChanged = function () {
            $scope.listUsers($scope.currentPage, $scope.pageSize);
        }

    });