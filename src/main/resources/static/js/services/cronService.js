'use strict';

angular.module('webCronApp')
    .service('CronService', function ($http, $cookies) {
        /**
         * @returns Promisse
         */
        this.listJobs = function (page, limit) {
            var req = {
                method: 'GET',
                url: '/cron/jobs',
                params: {page: page, limit: limit},
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'X-XSRF-TOKEN': $cookies.get('XSRF-TOKEN')
                }
            };
            return $http(req);
        };
    });