angular.module('webCronApp')
    .controller('JobsCtrl', function ($uibModal, $scope, $log, JobsService) {
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

        $scope.openModel = function (job, size, parentSelector) {
            var parentElem = parentSelector ?
                angular.element($document[0].querySelector('.modal-demo ' + parentSelector)) : undefined;
            var instance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: 'modelJobs.html',
                controller: 'ModalJobCtrl',
                controllerAs: 'modalJobs',
                size: size,
                appendTo: parentElem,
                resolve: {
                    job: function () {
                        return job;
                    }
                }
            });

            instance.result.then(function () {
            }, function () {
                $log.debug('Model closed');
                $scope.feedTable($scope.currentPage, $scope.pageSize);
            });
        };

        $scope.feedTable($scope.currentPage, $scope.pageSize);

        $scope.pageChanged = function () {
            $scope.feedTable($scope.currentPage, $scope.pageSize);
        };

    });