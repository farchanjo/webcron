angular.module('webCronApp')
    .controller('MeCtrl', function ($scope, $location, $log, UsersService, LoginService, ShareService) {
        $log.debug("Me Controller has been load.");
        $scope.loadUser = function () {
            UsersService.me()
                .then(function (res) {
                    $scope.me = res.data;
                }, function () {
                    $location.path('/loginPage');
                });
        };
        $scope.loadUser();

        $scope.logout = function () {
            LoginService.doLogout().then(function () {
                $location.path('/loginPage');
                ShareService.prepForBroadcast("logoutdone");
            });
        };

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
    });