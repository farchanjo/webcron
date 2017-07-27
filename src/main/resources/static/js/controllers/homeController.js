angular.module('webCronApp')
    .controller('HomeCtrl', function ($scope, $location, $log, UsersService) {
        $log.debug("Home Controller has been load.");
        UsersService.me().then(function (res) {
            $scope.me = res.data;
        }, function () {
            $location.path('/login');
        });
    });