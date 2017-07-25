'use strict';

angular.module('webCronApp')
    .service('LoginService', function ($http) {
        /**
         * @returns Promisse
         */
        this.doLogin = function (username, password) {
            var req = {
                method: 'POST',
                url: '/api/login',
                data: $.param({emailorusername: username, password: password}),
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            };
            return $http(req);
        };

        /**
         * @returns Promisse
         */
        this.doLogout = function () {
            var req = {
                method: 'GET',
                url: '/api/logout',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            };
            return $http(req);
        };
    });