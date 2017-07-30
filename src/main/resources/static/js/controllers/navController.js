angular.module('webCronApp')
    .controller('NavCtrl', function ($scope, $route, $location, $log, UsersService, ShareService) {
        $scope.me = {name: ''};

        $scope.loadme = function () {
            UsersService.me().then(function (res) {
                $scope.me = res.data;
            }, function () {
                $location.path('/loginPage');
            });
        };
        $scope.loadme();

        $scope.isActive = function (viewLocation) {
            return viewLocation === $location.path();
        };

        $scope.$on('handleBroadcast', function () {
            if (ShareService.message !== undefined) {
                switch (ShareService.message) {
                    case 'logoutdone':
                        $scope.loadme();
                        $scope.me = {};
                        break;
                    case 'logindone':
                        $scope.loadme();
                        break;
                }
            }
        });
    });