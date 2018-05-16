angular.module('webCronApp')
    .controller('ModalUserCtrl', function ($scope, $log, $uibModalInstance, user, UsersService, SystemService) {
        $log.debug("Model User Controller has been load.");
        if (user !== undefined) {
            $scope.user = user;
            $scope.roles = user.roles;
            $scope.status = user.status;
            $scope.user.password = null;
        } else {
            $scope.roles = 'ADMIN';
            $scope.status = 'ENABLE';
            $scope.user = {password: null};
        }
        $scope.statusSelect = [{
            label: 'Enable',
            value: 'ENABLE'
        }, {
            label: 'Disable',
            value: 'DISABLE'
        }];

        $scope.rolesSelect = [{
            label: 'User',
            value: 'USER'
        }, {
            label: 'Admin',
            value: 'ADMIN'
        }];

        $scope.saveUser = function (form) {
            if (form.$valid) {
                var user = {};
                user.id = $scope.user.id;
                if ($scope.user.name !== undefined &&
                    $scope.user.name.length > 0) {
                    user.name = $scope.user.name;
                }
                if ($scope.user.username !== undefined &&
                    $scope.user.username.length > 0) {
                    user.username = $scope.user.username;
                }
                if ($scope.user.email !== undefined &&
                    $scope.user.email.length > 0) {
                    user.email = $scope.user.email;
                }
                if ($scope.user.password !== undefined &&
                    $scope.user.password.length > 0) {
                    user.password = $scope.user.password;
                }
                if ($scope.roles !== undefined &&
                    $scope.roles.length > 0) {
                    user.roles = $scope.roles;
                }
                if ($scope.status !== undefined &&
                    $scope.status.length > 0) {
                    user.status = $scope.status;
                }
                UsersService.save(user)
                    .then(function () {
                        $uibModalInstance.dismiss();
                    }, function (er) {
                        $log.debug(er);
                    })
            }
        };

        $scope.closeModal = function () {
            $uibModalInstance.dismiss();
        };

    });