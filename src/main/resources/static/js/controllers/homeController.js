angular.module('webCronApp')
    .controller('HomeCtrl', function ($scope, $route, $location, $log, $stomp) {
        $stomp.setDebug(function (args) {
            $log.debug(args)
        });

        $stomp.connect('/cronws', null)
        // frame = CONNECTED headers
            .then(function (frame) {
                $stomp.subscribe('/topic/greetings', function (payload, headers, res) {
                    $scope.payload = payload
                }, {
                    'headers': 'are awesome'
                });

                // Unsubscribe
                // subscription.unsubscribe();

                // Send message
                $stomp.send('/app/hello', {
                    message: 'body'
                }, {
                    priority: 9,
                    custom: 42 // Custom Headers
                });

                // Disconnect
                $stomp.disconnect().then(function () {
                    $log.info('disconnected')
                })
            });
    });