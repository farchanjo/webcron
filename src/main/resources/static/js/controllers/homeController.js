angular.module('webCronApp')
    .controller('HomeCtrl', function ($scope, $route, $location, $log, $stomp, $cookies) {
        $stomp.setDebug(function (args) {
            $log.debug(args)
        });

        $stomp.connect('/cronws', {"X-XSRF-TOKEN": $cookies.get("XSRF-TOKEN")})
        // frame = CONNECTED headers
            .then(function (frame) {
                $stomp.subscribe('/topic/greetings', function (payload, headers, res) {
                    $scope.payload = payload
                });
            });
    });