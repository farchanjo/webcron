angular.module('webCronApp')
    .controller('NavCtrl', function ($scope, $route, $location, $log) {
        $scope.me = {name: "fabricio"};

        $scope.isActive = function (viewLocation) {
            return viewLocation === $location.path();
        };

        $scope.logout = function () {
        };

        $scope.$on('$routeChangeSuccess', function () {
        });
    });