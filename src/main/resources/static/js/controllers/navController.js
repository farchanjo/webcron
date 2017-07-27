angular.module('webCronApp')
    .controller('NavCtrl', function ($scope, $route, $location, $log, UsersService, LoginService) {
        $scope.me = {name: ''};
        UsersService.me().then(function (res) {
            $scope.me = res.data;
        }, function () {
            $location.path('/login');
        });

        $scope.isActive = function (viewLocation) {
            return viewLocation === $location.path();
        };

        $scope.logout = function () {
            LoginService.doLogout().then(function () {
                $location.path('/login');
            });
        };

        $scope.$on('$routeChangeSuccess', function () {
        });
    });