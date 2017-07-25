angular.module('webCronApp')
    .controller('JobsCtrl', function ($scope, $log, CronService) {
        $log.debug("Jobs Controller has been load.");

        CronService.listJobs(0, 10).then(function (res) {
            $log.debug(res);
        })
    });