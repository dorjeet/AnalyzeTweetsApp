/**
 * 
 */
var app = angular.module("myapp", ['ui.bootstrap']);

app.controller(
				"TweetController",
				function($scope, $http, jsonFilter) {


					$scope.getTweets = function(hashtag) {
						$scope.limit = 5;
						$scope.offset = 0;
						$scope.tweets = [];
						$scope.more = false;
						$scope.tweetDetailsSelected = false;
						$scope.selectedTweetId = null;

						$scope.closeAlert();
						$http.get(
										"/tweets"
												+ "?hashtag=" + hashtag
												+ "&limit=" + $scope.limit
												+ "&offset=" + $scope.offset)
								.success(
										function(response) {
											$scope.tweets = $scope.tweets
													.concat(response.rows);
											$scope.total_rows = response.total_rows;
											$scope.offset = response.offset;
											$scope.more = $scope.offset
													+ $scope.limit < $scope.total_rows
										}).error(
												function(data, status, headers, config) {
													alert(data.message);
												});
					}
					

					$scope.hasMore = function() {
						return $scope.more;
					}
					
					$scope.deleteTweet = function() {
						$http.delete("/tweets/"+ $scope.tweetDetails._id + "?hashtag=" + $scope.hashtag + "&rev=" + $scope.tweetDetails._rev)
						.success(
								function(response) {
									var key = $scope.selectedIndex;
									$scope.tweets.splice(key, 1);
									$scope.tweetDetailsSelected = false;
								});
						
						
					}
			
					$scope.loadmore = function() {
						$scope.offset = $scope.offset + $scope.limit;
						$scope.loadMoreTweets($scope.hashtag);
					}
					
					$scope.closeAlert = function() {
						$scope.submitResponseClass = "";
						$scope.submitResponse = "";
					}


					$scope.loadMoreTweets = function(hashtag) {
						$http.get("/tweets"
									+ "?hashtag=" + hashtag
									+ "&limit=" + $scope.limit
									+ "&offset=" + $scope.offset)
								.success(
										function(response) {
											$scope.tweets = $scope.tweets
													.concat(response.rows);
											$scope.total_rows = response.total_rows;
											$scope.offset = response.offset;
											$scope.more = $scope.offset
													+ $scope.limit < $scope.total_rows
										});
					}
					

					
					var logResult = function(str, data, status, headers, config) {
						return str + "\n\n" + "data: " + data + "\n\n" + "status: " + status
								+ "\n\n" + "headers: " + jsonFilter(headers()) + "\n\n"
								+ "config: " + jsonFilter(config);
					};
					
					$scope.submitComment = function() {
						$scope.submitResponseClass = "";
						$scope.submitResponse = "";


						var comment = {
							tweetId: $scope.tweetDetails.id,
							email : $scope.email,
							description : $scope.description
						};
						var req = {
							method : 'POST',
							url : '/comments/',
							headers : {
								'Content-Type' : 'Application/json; charset=utf-8;'
							},
							data : comment
						}

						$http(req).success(function(data, status, headers, config) {
							$scope.email = "";
							$scope.description = "";
							$scope.submitResponseClass = "alert alert-success";
							$scope.submitResponse = "Succeeded to save comment";
						}).error(
								function(data, status, headers, config) {
									$scope.submitResponseClass = "alert alert-danger";
									$scope.submitResponse = "Failed to save comment";
									$scope.postCallResult = logResult("POST ERROR", data,
											status, headers, config);
								});
					};
					

					

					$scope.getTweetDetails = function(tweet, index) {
						$scope.selectedTweetId = tweet.id;
						$scope.selectedIndex = index;
						$http.get("/tweets/"
								+ tweet.id + "?hashtag=" + $scope.hashtag)
								.success(function(response) {
									$scope.closeAlert();
									$scope.tweetDetailsSelected = true;
									$scope.tweetDetails = response;
								}).error(function(data, status, headers, config) {
													alert(data.message);
												});

					}
				});

