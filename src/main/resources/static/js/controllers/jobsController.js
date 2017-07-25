angular.module('webCronApp')
    .controller('JobsCtrl', function ($scope, $log, JobsService) {
        $log.debug("Jobs Controller has been load.");
        $scope.content = [];



        JobsService.listJobs(0, 10).then(function (res) {
            $scope.content = res.data;
        });

        $scope.editJob = function (job) {
            $log.info(job);
        };

        $scope.deleteJob = function (job) {
            $log.info(job);
        };
    });