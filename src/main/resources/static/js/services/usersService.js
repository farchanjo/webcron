'use strict';

angular.module('webCronApp')
    .service('UsersService', function ($http, $cookies) {
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

        this.save = function (user) {
            var req = {
                method: 'POST',
                url: '/users',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'X-XSRF-TOKEN': $cookies.get('XSRF-TOKEN')
                },
                data: user
            };
            return $http(req);
        }
    });