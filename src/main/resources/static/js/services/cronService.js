'use strict';

angular.module('webCronApp')
    .service('CronService', function ($http) {
        /**
         * @returns Promisse
         */
        this.listJobs = function (page, limit) {
            var req = {
                method: 'GET',
                url: '/api/cron',
                params: {page: page, limit: limit},
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            };
            return $http(req);
        };
    });