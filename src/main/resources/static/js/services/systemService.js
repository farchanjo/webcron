'use strict';

angular.module('webCronApp')
    .service('SystemService', function ($http) {
        /**
         * @returns Promisse
         */
        this.listUsers = function () {
            var req = {
                method: 'GET',
                url: '/system/users',
                headers: {
                    'Accept': 'application/json'
                }
            };
            return $http(req);
        };

    });