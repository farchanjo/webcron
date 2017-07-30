angular.module('webCronApp')
    .controller('LoginCtrl', function ($scope, $location, $log, LoginService, ShareService) {
        $scope.username = null;
        $scope.password = null;

        $scope.doLogin = function (form) {
            if (form.$valid) {
                $log.debug("Doing login");
                LoginService.doLogin($scope.username, $scope.password)
                    .then(function (res) {
                        if (res) {
                            $log.debug("login done");
                            $location.path("/");
                            ShareService.prepForBroadcast("logindone");
                        }
                    }, function (err) {
                        $log.debug(err);
                    });
            }
        };
    });