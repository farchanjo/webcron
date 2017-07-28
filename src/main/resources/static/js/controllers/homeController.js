angular.module('webCronApp')
    .controller('HomeCtrl', function ($scope, $location, $log, UsersService, JobsService) {
        $log.debug("Home Controller has been load.");
        $scope.pageSize = 10;
        $scope.currentPage = 1;

        UsersService.me().then(function (res) {
            $scope.me = res.data;
        }, function () {
            $location.path('/login');
        });

        $scope.feedTable = function (page, limit) {
            JobsService.listJobsResult(page, limit).then(function (res) {
                $scope.content = res.data;
                $scope.totalItems = res.data.totalElements;
                $scope.smallnumPages = res.data.totalPages;
            });
        };

        $scope.feedTable($scope.currentPage, $scope.pageSize);

        $scope.pageChanged = function () {
            $scope.feedTable($scope.currentPage, $scope.pageSize);
        };
    });