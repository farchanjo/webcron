angular.module('webCronApp')
    .controller('HomeCtrl', function ($scope, $location, $log, $uibModal, UsersService, JobsService) {
        $log.debug("Home Controller has been load.");
        $scope.pageSize = 10;
        $scope.currentPage = 1;

        UsersService.me().then(function (res) {
            $scope.me = res.data;
        }, function () {
            $location.path('/login');
        });

        $scope.feedTable = function (page, limit) {
            JobsService.listJobsResult(page, limit).then(function (res) {
                $scope.content = res.data;
                $scope.totalItems = res.data.totalElements;
                $scope.smallnumPages = res.data.totalPages;
            });
        };

        $scope.feedTable($scope.currentPage, $scope.pageSize);

        $scope.pageChanged = function () {
            $scope.feedTable($scope.currentPage, $scope.pageSize);
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
        }
    });