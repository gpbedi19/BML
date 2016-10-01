//---------------------------------------------------------------------------
// This file contains bookings related AngularJS controllers used in this
// application. Make sure that all javascript files are listed in
// suitable order in the closure compiler's compression command in ant task.
//---------------------------------------------------------------------------

/**
 * SPM lab booking search controller
 */
myApp.controller('BookingSearchCtrl', [
		'$scope',
		'$http',
		'$routeParams',
		'Shared',
		'$filter',
		'$location',
		function($scope, $http, $routeParams, Shared, $filter,$location) {
			C.log("BookingSearchCtrl: " + JSON.stringify($routeParams));
			$scope.Shared = Shared;
			$scope.P = {results:[]};

			/* Date picker popups */
			$scope.popup1 = {opened: false};
			$scope.popup2 = {opened: false};
			
			$scope.open1 = function() {
				$scope.popup1.opened = true;
			};
			$scope.open2 = function() {
				$scope.popup2.opened = true;
			};
			/* Sorting of table */
			$scope.predicate = 'bookingDate';
			$scope.reverse = true;
			$scope.order = function(predicate) {
				$scope.reverse = ($scope.predicate === predicate) ? !$scope.reverse
						: false;
				$scope.predicate = predicate;
			};
			  
			$scope.showBooking = function(bid) {
				$location.path("/spm/"+bid+"/true");
			};

			$scope.search = function() {
				C.log("Searching: "+JSON.stringify($scope.P));
				$scope.P.bookingDateFrom = $filter('date')($scope.P.bookingDateFrom, 'mediumDate');
				$scope.P.bookingDateTo = $filter('date')($scope.P.bookingDateTo, 'mediumDate');

				$http.post("./Bookings/searchBooking.a", $scope.P).success(
						function(data, status, headers, config) {
							if (data.Status === "OK") {
								$scope.P.results = data.Payload;
							} else {
								$scope.setStatus(data.Payload);
							}
						}).error(function(data, status, headers, config) {
					$scope.setStatus(data.Payload);
				});
			};
			  
		} ]);

/**
 * Lab booking controller. This is common controller for all types of labs.
 * The $routeParams["lab"] value determines the type of lab to work on.
 */
myApp.controller('LabBookingCtrl', [
		'$scope',
		'$http',
		'$routeParams',
		'Shared',
		'$filter',
		'$location',
		function($scope, $http, $routeParams, Shared, $filter,$location) {
			C.log("LabBookingCtrl: " + JSON.stringify($routeParams));
			$scope.Shared = Shared;
			/* Prepare default booking object */
			$scope.P = {
				booking : {
					bookingDate : $filter('date')(new Date(), 'mediumDate'),
					lab : $routeParams["lab"],
					analysisModes : [],
					status : "PENDING"
				}
			};

			var url = "./Bookings/getBooking.a?lab="+$routeParams["lab"];
			
			var isEdit = $routeParams["bid"] !== undefined;
			if (isEdit) {
				url = url+"&bid="+$routeParams["bid"];
				// Load the booking record
				$http.get(url).success(function(data, status, headers, config) {
					if (data.Status === "OK") {
						$scope.P = data.Payload;
						$scope.P.booking.instrumentDate = new Date($scope.P.booking.instrumentDate);
						$scope.calcTotalCharges();
						// Tell the child controller about loaded booking
						$scope.$broadcast("bookingLoaded", $scope.P.booking);
					} else {
						$scope.setStatus(data.Payload);
					}
				}).error(function(data, status, headers, config) {
					$scope.setStatus(data.Payload);
				});
			}

			/**
			 * Saves the lab booking record.
			 */
			$scope.saveBooking = function() {
				if ($scope.P.inError) {
					$scope.setStatus("Something seems wrong with details you entered! Please check.");
					return;
				}
				$scope.P.booking.instrumentDate = $filter('date')($scope.P.booking.instrumentDate, 'mediumDate');
				$http.post("./Bookings/saveBooking.a", $scope.P).success(
						function(data, status, headers, config) {
							if (data.Status === "OK") {
								$scope.P = data.Payload;
								$scope.P.booking.instrumentDate = new Date($scope.P.booking.instrumentDate);
								$scope.setStatus("Saved booking request!");
								$location.path("/booking/"+$routeParams["lab"]+"/"+$scope.P.booking.bookingId).replace();
							} else {
								$scope.setStatus(data.Payload);
							}
						}).error(function(data, status, headers, config) {
					$scope.setStatus(data.Payload);
				});
			};
			
			/**
			 * Calculate charges for measurement
			 */
			$scope.calcCharges = function(am) {
				if (am.resourceType === undefined) {
					$scope.setStatus("Please select the resource type!");
					return;
				}
				var rp = Shared.RP.find(function(p) {
					return p.resourceType.code === am.resourceType.code; 
				});
				if (rp !== undefined) {
					am.charges = am.samples*rp.perSamplePrice + am.results*rp.perResultPrice;
				} else {
					$scope.setStatus("Pricing information could not be found for "+am.resourceType.code);
				}
				$scope.calcTotalCharges();
				C.log(">>>>>>> "+JSON.stringify(am));
			};

			$scope.calcTotalCharges = function() {
				var x = $scope.P.booking.analysisModes;
				var total=0;
				for(var i=0; i<x.length;i++) {
					total += x[i].charges;
				}
				$scope.totalCharges = total;
			};

			$scope.removeAM = function(n) {
				var i = $scope.P.booking.analysisModes.indexOf(n);
				$scope.P.booking.analysisModes.splice(i,1);
			};
			
			$scope.isSaveDisabled = function() {
				var disabled = false;
				if ($scope.P.inError) {
					disabled = true;
				} else if ($scope.P.booking.bookingId !== undefined) {
					if (Shared.hasRole('USER') && $scope.P.booking.status !== 'PENDING') {
						disabled = true;
					}
				}
				return disabled;
			};

		} ]);

/**
 * Booking controller. Parent of this controller must have a property of
 * this structure: "booking":{bookingDate:"A Date object", startTime:"HH:MM", endTime:"HH:MM"}
 * available under property $scope.P of the parent controller. 
 */
myApp.controller('BookingCtrl', [
		'$scope',
		'$http',
		'$routeParams',
		'Shared',
		'$filter',
		function($scope, $http, $routeParams, Shared, $filter) {
			C.log("BookingCtrl: " + JSON.stringify($routeParams));
			$scope.Shared = Shared;
			/* Date popup */
			$scope.dtPopup = {opened: false};
			$scope.openDt = function() {
				$scope.dtPopup.opened = true;
			};

			// Update booking date when notified by parent controller.
			$scope.$on("bookingLoaded", function(event, b) {
				$scope.bookingDate = new Date($filter('date')(b.bookingDate, 'mediumDate'));
			});
			/**
			 * Watch for changes to bookingDate. If user selected a new date then fetch
			 * blocked slots for new date. Also set the bookingDate property of parent.
			 */
			$scope.$watch("bookingDate", function(newValue, oldValue) {
				C.log("new: "+newValue+" :: old: "+oldValue);
				if (newValue !== oldValue) {
					$scope.P.booking.bookingDate = $filter('date')(newValue, 'mediumDate');
					$scope.getSlots($scope.P.booking.bookingDate);
				}
			});
			$scope.slotNoToTime = function(n) {
				var m = (n%2 === 0) ? "00":"30";
				var h = ("00"+Math.floor(0.5*n)).slice(-2);				
				return h+":"+m;
			};

			$scope.timeToSlotNo = function(t) {
				var s = t.replace(/:30/i, ':50').replace(/:/i, '.');
				return 2*parseFloat(s);
			};
			$scope.checkOverlap = function(st, et) {
				var overlaps = false;
				if ($scope.Slots === undefined) return false;
				for (var i=0; i<$scope.Slots.length; i++) {
					var s = $scope.Slots[i];
					var t1 = $scope.timeToSlotNo(s.startTime);
					var t2 = $scope.timeToSlotNo(s.endTime);
					overlaps = !(et <= t1 || st >= t2);
					if (overlaps) {
						C.log("checkOverlap: t1="+t1+". t2="+t2);
						break;
					}
				}
				C.log("checkOverlap: st="+st+". et="+et+". overlaps="+overlaps);
				$scope.P.inError = overlaps;
				return overlaps;
			};
			/**
			 * Updates start/end time properties of booking object in parent controller.
			 */
			$scope.handleSliderChange = function(sliderId, modelValue, highValue) {
				C.log("Slider value: "+modelValue+". HighValue: "+highValue);
				$scope.overlaps = $scope.checkOverlap(modelValue, highValue);
				$scope.P.booking.startTime = $scope.slotNoToTime(modelValue);
				$scope.P.booking.endTime = $scope.slotNoToTime(highValue);
				C.log("booking:: "+JSON.stringify($scope.P.booking));
			};
			
			/**
			 * Time slot selection slider object setup.
			 */
			$scope.slider = {
				minValue : 16, /* 16th slot --> 08:00HRS. */
				maxValue : 16,
				options : {
					showTicks: true,
					floor : 16,
					ceil : 36, /* 36th slot --> 18:00HRS. */
					step : 1,
					onChange: $scope.handleSliderChange,
					translate: function(value, sliderId, label) {
						return $scope.slotNoToTime(value);
					}
				}
			};
			
			$scope.getSlots = function(d) {
				if (d === undefined)
					return;
				C.log("Fetching slots for " + d);
				$http.get("./Bookings/getSlotsForDate.a?date=" + d).success(
						function(data, status, headers, config) {
							if (data.Status === "OK") {
								$scope.Slots = data.Payload;
								if ($scope.Slots === undefined) $scope.Slots = [];
							} else {
								$scope.setStatus(data.Payload);
							}
						}).error(function(data, status, headers, config) {
					$scope.setStatus(data.Payload);
				});
			};

		} ]);

// Invoice controller
myApp.controller('InvoiceCtrl', [
		'$scope',
		'$http',
		'$location',
		'$routeParams',
		'Shared',
		function($scope, $http, $location, $routeParams, Shared) {
			$scope.Shared = Shared;
			C.log("InvoiceCtrl ...");
			$scope.I = {};
			var id = $routeParams["bid"];
			$http.get("./Bookings/getInvoice.a?bid=" + id).success(
					function(data, status, headers, config) {
						if (data.Status === "OK") {
							$scope.I = data.Payload;
						} else {
							$scope.setStatus(data.Payload);
						}
					}).error(function(data, status, headers, config) {
				$scope.setStatus(data.Payload);
			});
			
		} ]);

