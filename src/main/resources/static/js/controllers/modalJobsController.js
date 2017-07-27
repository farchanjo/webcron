angular.module('webCronApp')
    .controller('ModalJobCtrl', function ($scope, $log, $uibModalInstance, job) {
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
            $scope.name = job.name;
            $scope.type = job.async;
            $scope.rate = job.fixedRate;
            $scope.status = job.status;
            $scope.unit = job.unit;
            $scope.cron = job.cron;
        }

        $scope.okModal = function (form) {
            if (form.$valid) {
            }
            $uibModalInstance.dismiss();
        };

        $scope.cancelModal = function () {
            $uibModalInstance.dismiss();
        };
    });