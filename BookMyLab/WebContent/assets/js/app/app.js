//-----------------------------------------------------------------------------
// This file contains the code for application setup tasks of angular.
// Mainly we setup the client-side routing here and services dependencies etc.
//-----------------------------------------------------------------------------
/* Declare app level module and its dependencies on filters, and services if any */
var myApp = angular.module('myApp', ['ngRoute', 'ngAnimate',
    'myApp.SharedServices', 'ui.bootstrap','rzModule'])
        .config(['$routeProvider', '$locationProvider',
            function ($routeProvider, $locationProvider) {

                /* Add the routes for views */
                $routeProvider.when('/index', {
                    /* Points to the server-side controller path */
                    templateUrl: './Partials/home.a'
                    /* Name of the angular controller. Commented because it
                     * should be either specified here or in the template.
                     */
                    /* controller: 'IndexCtrl' */
                });
                $routeProvider.when('/login', {
                    templateUrl: './Partials/login.a'
                });
                $routeProvider.when('/register', {
                    templateUrl: './Partials/register.a'
                });
                $routeProvider.when('/user/:pid', {
                    templateUrl: './Partials/register.a'
                });
                $routeProvider.when('/invoice/:bid', {
                    templateUrl: './Partials/invoice.a'
                });

                /* User profile */
                $routeProvider.when('/profile', {
                    templateUrl: './Partials/profile.a'
                });
                $routeProvider.when('/profile/:id', {
                    templateUrl: './Partials/profile.a'
                });

                /* Lab booking */
                $routeProvider.when('/booking/:lab/:bid', {
                    templateUrl: function(rp) {
                    	return "./Partials/"+rp["lab"]+".a";
					}
                });
                $routeProvider.when('/booking/:lab', {
                    templateUrl: function(rp) {
                    	return "./Partials/"+rp["lab"]+".a";
					}
                });
                $routeProvider.when('/searchBooking', {
                    templateUrl: function(rp) {
                    	return "./Partials/searchBooking.a";
					}
                });

                $routeProvider.when('/logout', {
                    templateUrl: './App/logout.a',
                    controller: 'DummyCtrl'
                });
                /* Default route */
                $routeProvider.otherwise({redirectTo: '/index'});

                /* Specify HTML5 mode (using the History APIs) or HashBang syntax. */
                $locationProvider.html5Mode(false);
            }]);

myApp.controller('DummyCtrl', ['$scope', function ($scope) {
    }]);

/**
 * Global namespace containing functions for common tasks.
 */
var C = {
    log: function (msg) {
    	if (window.location.hostname === "localhost")
    		console.debug(msg);
    }
};
