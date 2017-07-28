'use strict';

angular.module('webCronApp')
    .service('JobsService', function ($http, $cookies) {
        /**
         * @returns Promisse
         */
        this.listJobs = function (page, limit) {
            var req = {
                method: 'GET',
                url: '/jobs/all',
                params: {page: (page - 1), limit: limit},
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'X-XSRF-TOKEN': $cookies.get('XSRF-TOKEN')
                }
            };
            return $http(req);
        };

        /**
         * @returns Promisse
         */
        this.listJobsResult = function (page, limit) {
            var req = {
                method: 'GET',
                url: '/jobs/results',
                params: {page: (page - 1), limit: limit},
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'X-XSRF-TOKEN': $cookies.get('XSRF-TOKEN')
                }
            };
            return $http(req);
        };

        /**
         * @returns Promisse
         */
        this.readLog = function (jobId) {
            var req = {
                method: 'GET',
                url: '/jobs/log',
                params: {jobId: jobId},
                transformResponse: [function (data) {
                    return data;
                }],
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'X-XSRF-TOKEN': $cookies.get('XSRF-TOKEN')
                }
            };
            return $http(req);
        };

        /**
         * @returns Promisse
         */
        this.save = function (job) {
            var req = {
                method: 'POST',
                url: '/jobs/create',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'X-XSRF-TOKEN': $cookies.get('XSRF-TOKEN')
                },
                data: job
            };
            return $http(req);
        };

        /**
         * @returns Promisse
         */
        this.delete = function (job) {
            var req = {
                method: 'DELETE',
                url: '/jobs/delete/' + job.id,
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'X-XSRF-TOKEN': $cookies.get('XSRF-TOKEN')
                }
            };
            return $http(req);
        };
    });