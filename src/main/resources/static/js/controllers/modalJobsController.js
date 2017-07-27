angular.module('webCronApp')
    .controller('ModalJobCtrl', function ($scope, $log, $uibModalInstance, job, JobsService) {
        $log.debug("Model Jobs Controller has been load.");

        $scope.type = 'CRON';
        $scope.status = 'ENABLE';
        $scope.unit = 'MINUTES';
        $scope.asyncType = [{
            label: 'Periodic',
            value: 'PERIODIC'
        }, {
            label: 'Cron',
            value: 'CRON'
        }];

        $scope.statusSelect = [{
            label: 'Enabled',
            value: 'ENABLE'
        }, {
            label: 'Disabled',
            value: 'DISABLE'
        }];

        $scope.unitSelect = [{
            label: 'Days',
            value: 'DAYS'
        }, {
            label: 'Hours',
            value: 'HOURS'
        }, {
            label: 'Seconds',
            value: 'SECONDS'
        }, {
            label: 'Milliseconds',
            value: 'MILLISECONDS'
        }, {
            label: 'Minute',
            value: 'MINUTES'
        }];

        if (job) {
            $scope.id = job.id;
            $scope.name = job.name;
            $scope.type = job.async;
            $scope.rate = job.fixedRate;
            $scope.status = job.status;
            $scope.unit = job.unit;
            $scope.cron = job.cron;
        }

        $scope.okModal = function (form) {
            if (form.$valid) {
                JobsService.save({
                    id: $scope.id,
                    name: $scope.name,
                    async: $scope.type,
                    fixedRate: $scope.rate,
                    status: $scope.status,
                    unit: job.unit,
                    cron: $scope.cron
                }).then(function () {
                    $log.debug('Saved jobs: ' + $scope.name)
                    $uibModalInstance.dismiss();
                });
            }

        };

        $scope.cancelModal = function () {
            $uibModalInstance.dismiss();
        };
    });