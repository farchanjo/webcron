angular.module('webCronApp')
    .controller('ModalJobCtrl', function ($scope, $log, $uibModalInstance, job, JobsService, SystemService) {
        $log.debug("Model Jobs Controller has been load.");
        $scope.systemUsers = [];
        $scope.systemUser = 'root';
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
            label: 'Enable',
            value: 'ENABLE'
        }, {
            label: 'Disable',
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
            $scope.command = job.command;
            $scope.directory = job.directory;
            if (job.environments !== undefined &&
                job.environments.length > 0) {
                $scope.environments = [];
                for (var i = 0; i < job.environments.length; i++) {
                    var env = job.environments[i];
                    $scope.environments.push(env['key'] + '=' + env['value']);
                }
            }
        }

        $scope.okModal = function (form) {
            if (form.$valid) {
                var job = {
                    id: $scope.id,
                    name: $scope.name,
                    async: $scope.type,
                    fixedRate: $scope.rate,
                    status: $scope.status,
                    unit: $scope.unit,
                    system: {
                        user: $scope.systemUser
                    },
                    cron: $scope.cron,
                    command: $scope.command,
                    directory: $scope.directory
                };
                if ($scope.environments !== undefined) {
                    if ($scope.environments.length > 0) {
                        for (var i = 0; i < $scope.environments.length; i++) {
                            var env = $scope.environments[i];
                            var envArr = env['text'].split('=');
                            if (job.environments === undefined) {
                                job.environments = [{key: envArr[0], value: envArr[1]}]
                            }
                            else {
                                job.environments.push({key: envArr[0], value: envArr[1]});
                            }
                        }
                    }
                }
                JobsService.save(job).then(function () {
                    $log.debug('Saved jobs: ' + $scope.name);
                    $uibModalInstance.dismiss();
                });
            }

        };

        $scope.cancelModal = function () {
            $uibModalInstance.dismiss();
        };

        SystemService.listUsers()
            .then(function (res) {
                $scope.systemUsers = res.data;
            });
    });