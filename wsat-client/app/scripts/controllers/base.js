'use strict';

angular.module('angClientApp')
  .controller('BaseCtrl', function ($scope, $http, $location) {

  		var accountId = 1;

  		var link = $location.search().l; 

  		$http.get(link)
	  	.success(function(data, status, headers, config) {
			$scope.base = {
				wsName : data.wsName,
				wsProvider : data.wsProvider,
				baseUri: data.baseUri,
				wsDescription: data.wsDescription,
				wsKeywowrds: data.wsKeywords
			};
		});

		$scope.save = function() {
			$http.put(link, $scope.base)
  			.success(function(data, status, headers, config) {
  				$scope.success = true;
  			})
  			.error(function(data, status, headers, config) { 
  				$scope.error = true;
  			});
	  	};

	});
