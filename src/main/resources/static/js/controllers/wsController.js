angular.module('webCronApp')
    .controller('WsCtrl', function ($scope, $location, $log, WsService) {
        $scope.payload = null;

        /**
         * OnLoad
         */
        $scope.$on('$viewContentLoaded', function () {
            WsService.getConnection()
                .then(function () {
                    WsService.getStomp().subscribe('/topic/listjobs', function (payload, headers, res) {
                        $scope.payload = payload;
                        $scope.$apply();
                    });
                });
        });

        /**
         * On destroy
         */
        $scope.$on('$destroy', function () {
            WsService.getStomp().disconnect().then(function () {
                $log.info("Sockect disconnect");
            })
        })
    });