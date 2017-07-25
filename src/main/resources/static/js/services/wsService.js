'use strict';

angular.module('webCronApp')
    .service('WsService', function ($stomp, $cookies, $log) {

        $stomp.setDebug(function (args) {
            $log.debug(args)
        });

        /**
         * @returns Promisse
         */
        this.getConnection = function () {
            return $stomp.connect('/cronws', {"X-XSRF-TOKEN": $cookies.get("XSRF-TOKEN")});
        };

        /**
         * @returns {$stomp|*}
         */
        this.getStomp = function () {
            return $stomp;
        }
    });