'use strict';

/**
 * @ngdoc overview
 * @name webCronApp
 * @description
 * # webCronApp
 *
 * Main module of the application.
 */
angular
    .module('webCronApp', [
        'ngAnimate',
        'ngAria',
        'ngCookies',
        'ngMessages',
        'ngResource',
        'ngRoute',
        'ngSanitize',
        'ngStomp',
        'ui.bootstrap'
    ])
    .config(function ($routeProvider) {
        $routeProvider
            .when('#!/', {
                templateUrl: 'views/home.html',
                controller: 'HomeCtrl',
                controllerAs: 'home'
            })
            .when('#!/login', {
                templateUrl: 'views/login.html',
                controller: 'LoginCtrl',
                controllerAs: 'login'
            })
            .when('#!/jobs', {
                templateUrl: 'views/jobs.html',
                controller: 'JobsCtrl',
                controllerAs: 'jobs'
            })
            .otherwise({
                redirectTo: '#!/'
            });
    });