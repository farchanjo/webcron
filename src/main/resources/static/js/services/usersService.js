'use strict';

angular.module('webCronApp')
    .service('UsersService', function ($http) {
        /**
         * @returns Promisse
         */
        this.me = function () {
            var req = {
                method: 'GET',
                url: '/users/me',
                headers: {
                    'Accept': 'application/json'
                }
            };
            return $http(req);
        };
    });