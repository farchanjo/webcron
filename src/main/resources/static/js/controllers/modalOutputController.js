angular.module('webCronApp')
    .controller('ModalOutputCtrl', function ($scope, $log, $uibModalInstance, job) {
        $log.debug("Model Output Controller has been load.");
        $scope.job = job;

        $scope.closeModal = function () {
            $uibModalInstance.close();
        };

    });