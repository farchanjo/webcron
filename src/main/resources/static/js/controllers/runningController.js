angular.module('webCronApp')
    .controller('RunningCtrl', function ($interval, $scope, $log, JobsService) {
        $log.debug("Running Controller has been load.");
        $scope.jobsRunning = [];
        $scope.reloadTime = 5;
        var autoReloadPromise;

        $scope.feedTable = function () {
            JobsService.listRunning()
                .then(function (res) {
                    $scope.jobsRunning = res.data;
                });
        };

        $scope.stopJob = function (job) {
            JobsService.stopTigger(job)
                .then(function (res) {
                    $log.debug(res.data);
                })
        };


        $scope.startAuto = function (checked) {
            if (checked) {
                autoReloadPromise = $interval(function () {
                    $scope.feedTable();
                }, $scope.reloadTime * 1000);
            }
            else {
                $interval.cancel(autoReloadPromise);
            }
        };

        $scope.feedTable();
        $scope.$on("$destroy", function () {
            $interval.cancel(autoReloadPromise);
        });
    });