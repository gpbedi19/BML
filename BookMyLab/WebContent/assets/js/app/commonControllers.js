//---------------------------------------------------------------------------
// This file contains the AngularJS controllers used in this application.
// If needed you may split this file into multiple files to organize the
// controllers as needed. Make sure that all javascript files are listed in
// suitable order in the closure compiler's compression command in ant task.
//---------------------------------------------------------------------------
/**
 * Login page controller.
 */
myApp.controller('LoginCtrl', [
		'$scope',
		'$http',
		'$location',
		'Shared',
		function LoginCtrl($scope, $http, $location, Shared) {

			$scope.Shared = Shared;
			$scope.LF = {};
			C.log("LoginCtrl loaded.");
			$scope.login = function() {
				if ($scope.LF.email === undefined) {
					$scope.setStatus("Please supply a valid email to login!");
					return;
				}
				$http.post('./App/login.a', $scope.LF).success(
						function(data, status, headers, config) {
							if (data.Status === "OK") {
								Shared.init();
								$location.path("/Non-exitent").replace();
							} else {
								$scope.setStatus(data.Payload);
							}
						}).error(function(data, status, headers, config) {
					$scope.setStatus(data.Payload);
				});
			};

		} ]);

// User cntroller
myApp.controller('UserCtrl', [
		'$scope',
		'$http',
		'$location',
		'$routeParams',
		'$route',
		'Shared',
		function($scope, $http, $location, $routeParams, $route, Shared) {
			$scope.Shared = Shared;
			$scope.Reg = {};
			C.log("UserCtrl ...");
			var isEdit = $routeParams["pid"] !== undefined;
			var url = isEdit ? "./App/saveUser.a" : "./App/register.a";
			if (isEdit) {
				C.log("Editing user");
				$scope.Reg = Shared.User;
			} else {
				C.log("Creating new user.");
			}
			$scope.saveUser = function() {
				$http.post(url, $scope.Reg).success(
						function(data, status, headers, config) {
							if (data.Status === "OK") {
								if (isEdit) {
									C.log("Updated user.");
									$scope.setStatus("Updated user details!");
								} else {
									Shared.init();
									$location.path("/Non-exitent").replace();
								}
							} else {
								$scope.setStatus(data.Payload);
							}
						}).error(function(data, status, headers, config) {
					$scope.setStatus(data.Payload);
				});
			};

		} ]);

/**
 * Controller for the root view. All other controllers are children of this
 * controller, and hence can access functions and data defined on this one. We
 * load commonly used entities such as countries list etc. here.
 */
myApp
		.controller(
				'IndexCtrl',
				[
						'$scope',
						'$http',
						'$location',
						'$q',
						'Shared',
						function($scope, $http, $location, $q, Shared) {

							C.log("IndexCtrl...");
							$scope.RootModel = {
								Data : {}
							};
							$scope.Shared = Shared;

							$scope.setStatus = function(msg) {
								$scope.StatusMessage = msg;
								$("#statusmsg").fadeIn(400);
							};
							$scope.hideStatus = function() {
								$scope.StatusMessage = undefined;
								$("#statusmsg").fadeOut(400);
							};

							$scope.logout = function() {
								$http.get('./App/logout.a').success(
										function(data) {
											C.log("Logged out: "
													+ JSON.stringify(data));
											Shared.User = {loggedIn: false};
											$location.path("/Non-exitent")
													.replace();
										});
							};

							// Emitted in HTTP interceptor in case of errors
							$scope
									.$on(
											"Errors",
											function(event, args) {
												$scope.Errors = args;
												$scope.Invalid = args.length > 0;
												C.log("Got errors: "
														+ JSON.stringify(args)
														+ ". Invalid="
														+ $scope.Invalid);
												$scope
														.setStatus("Your request could not be processed. Error occurred.");
											});
							$scope.$on("ClearMessages", function(event, args) {
								$scope.Errors = [];
								$scope.Invalid = false;
								$scope.hideStatus();
							});

							Shared.init(); // Gets the session details if any
							C.log("IndexCtrl: Loaded.");
							
							$scope.printableProfileDetails = function(u) {
								var d = "";
								if (u.profile !== undefined) {
									var p = u.profile;
									d = d + p.designation + ", Dept. " + p.dept + ", ";
									d = d + p.org + " (" + p.orgType + "), " + p.address +" ";
									d = d + p.city + " " + p.state + " " + p.postalCode;
									d = d + ". Ph. " + p.officePhone;
								} else {
									d = "(Profile details missing.)";
								}
								return d;
							};

						} ]);

/**
 * Home controller
 */
myApp.controller('HomeCtrl', [
		'$scope',
		'$http',
		'$routeParams',
		'Shared',
		function($scope, $http, $routeParams, Shared) {
			C.log("HomeCtrl: " + JSON.stringify($routeParams));

			if (Shared.User.loggedIn) {
				$http.get("./App/getDashboard.a").success(
						function(data, status, headers, config) {
							if (data.Status === "OK") {
								$scope.P = data.Payload;
							} else {
								$scope.setStatus(data.Payload);
							}
						}).error(function(data, status, headers, config) {
					$scope.setStatus(data.Payload);
				});
			}
			
			$scope.markUserVerified = function(u) {
				$http.get("./App/markUserVerified.a?email="+u.email).success(
						function(data, status, headers, config) {
							if (data.Status === "OK") {
								u.verified = true;
							} else {
								$scope.setStatus(data.Payload);
							}
						}).error(function(data, status, headers, config) {
					$scope.setStatus(data.Payload);
				});
			};

		} ]);

/**
 * User profile controller
 */
myApp.controller('ProfileCtrl', [
		'$scope',
		'$http',
		'$routeParams',
		'Shared',
		function($scope, $http, $routeParams, Shared) {
			C.log("ProfileCtrl: " + JSON.stringify($routeParams));
			$scope.Shared = Shared;
			$scope.P = {};
			var url = "./App/getProfile.a";
			$http.get(url).success(function(data, status, headers, config) {
				if (data.Status === "OK") {
					$scope.P = data.Payload;
				} else {
					$scope.setStatus(data.Payload);
				}
			}).error(function(data, status, headers, config) {
				$scope.setStatus(data.Payload);
			});

			$scope.saveProfile = function() {
				$http.post("./App/saveProfile.a", $scope.P).success(
						function(data, status, headers, config) {
							if (data.Status === "OK") {
								$scope.P = data.Payload;
								$scope.setStatus("Saved user profile!");
							} else {
								$scope.setStatus(data.Payload);
							}
						}).error(function(data, status, headers, config) {
					$scope.setStatus(data.Payload);
				});
			};
		} ]);

/**
 * Admin tasks controller
 */
myApp.controller('AdminCtrl', [
		'$scope',
		'$http',
		'$routeParams',
		'Shared',
		function($scope, $http, $routeParams, Shared) {
			C.log("AdminCtrl: " + JSON.stringify($routeParams));
			$scope.Shared = Shared;
			$http.get("./App/getDashboard.a").success(
					function(data, status, headers, config) {
						if (data.Status === "OK") {
							$scope.P = data.Payload;
						} else {
							$scope.setStatus(data.Payload);
						}
					}).error(function(data, status, headers, config) {
				$scope.setStatus(data.Payload);
			});
		} ]);
