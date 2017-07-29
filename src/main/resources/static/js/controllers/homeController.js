angular.module('webCronApp')
    .controller('HomeCtrl', function ($scope, $location, $log, $uibModal, $interval, UsersService, JobsService) {
        $log.debug("Home Controller has been load.");
        $scope.pageSize = 15;
        $scope.currentPage = 1;
        $scope.reloadTime = 5;
        var autoReloadPromise;

        UsersService.me().then(function (res) {
            $scope.me = res.data;
        }, function () {
            $location.path('/login');
        });

        $scope.feedTable = function (page, limit, jobName) {
            JobsService.listJobsResult(page, limit, jobName).then(function (res) {
                $scope.content = res.data;
                $scope.totalItems = res.data.totalElements;
                $scope.smallnumPages = res.data.totalPages;
            });
        };

        $scope.feedTable($scope.currentPage, $scope.pageSize);

        $scope.pageChanged = function () {
            if ($scope.searched !== undefined && $scope.searched.length > 0) {
                $scope.feedTable($scope.currentPage, $scope.pageSize, $scope.searched);
            }
            else {
                $scope.feedTable($scope.currentPage, $scope.pageSize);
            }
        };

        $scope.openModal = function (job, size, parentSelector) {
            var parentElem = parentSelector ?
                angular.element($document[0].querySelector('.modal-demo ' + parentSelector)) : undefined;
            $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: 'modalOutput.html',
                controller: 'ModalOutputCtrl',
                controllerAs: 'modalOutput',
                size: size,
                appendTo: parentElem,
                resolve: {
                    job: function () {
                        return job;
                    }
                }
            });
        };

        $scope.search = function () {
            if ($scope.searched !== undefined && $scope.searched.length > 0) {
                $scope.feedTable($scope.currentPage, $scope.pageSize, $scope.searched);
            }
            else {
                $scope.feedTable($scope.currentPage, $scope.pageSize);
            }
        };

        $scope.isDanger = function (result) {
            if (result.errors || result.exitCode > 0) {
                return 'danger';
            }
            else {
                return 'success';
            }
        };

        $scope.startAuto = function (checked) {
            if (checked) {
                autoReloadPromise = $interval(function () {
                    if ($scope.searched !== undefined && $scope.searched.length > 0) {
                        $scope.feedTable($scope.currentPage, $scope.pageSize, $scope.searched);
                    }
                    else {
                        $scope.feedTable($scope.currentPage, $scope.pageSize);
                    }
                }, $scope.reloadTime * 1000);
            }
            else {
                $interval.cancel(autoReloadPromise);
            }
        };

        $scope.$on("$destroy", function () {
            $interval.cancel(autoReloadPromise);
        });
    });