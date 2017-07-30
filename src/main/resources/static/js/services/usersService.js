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

        /**
         * @returns Promisse
         */
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
        };

        /**
         * @returns Promisse
         */
        this.listUsers = function (limit, page) {
            var req = {
                method: 'GET',
                url: '/users',
                params: {page: (page - 1), limit: limit},
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'X-XSRF-TOKEN': $cookies.get('XSRF-TOKEN')
                }
            };
            return $http(req);
        };
    });