angular.module('webCronApp')
    .controller('JobsCtrl', function ($scope, $log, JobsService) {
        $log.debug("Jobs Controller has been load.");
        $scope.pageSize = 10;
        $scope.currentPage = 1;
        $scope.feedTable = function (page, limit) {
            JobsService.listJobs(page, limit).then(function (res) {
                $scope.content = res.data;
                $scope.totalItems = res.data.totalElements;
                $scope.smallnumPages = res.data.totalPages;
            })
        };


        $scope.feedTable($scope.currentPage, $scope.pageSize);

        $scope.editJob = function (job) {
            $log.info(job);
        };

        $scope.deleteJob = function (job) {
            $log.info(job);
        };

        $scope.pageChanged = function () {
            $scope.feedTable($scope.currentPage, $scope.pageSize);
        };
    });