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
                var user = {
                    name: $scope.me.name,
                    id: $scope.me.id,
                    username: $scope.me.username,
                    email: $scope.me.email,
                    password: $scope.me.password
                };

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