angular.module('webCronApp')
    .controller('HomeCtrl', function ($scope, $log, CronService) {
        $scope.payload = null;
        $log.debug("Home Controller has been load.");

        CronService.listJobs(0, 10).then(function (res) {
            $log.debug(res);
        })
    });