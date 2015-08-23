package com.dorjee.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.io.IOException;

import org.apache.commons.codec.Charsets;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;

import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorjee.exceptions.DatabaseConnectionException;
import com.dorjee.exceptions.DatabaseNotFoundException;
import com.dorjee.exceptions.TweetParseException;
import com.dorjee.model.TweetBody;
import com.dorjee.model.Tweets;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/tweets")
public class TweetsController {

	private static String DB_URL = "http://localhost:5984/";

	@RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public Resource<Tweets> getTweets(
			@RequestParam(value = "hashtag", defaultValue = "capitalone") String hashtag,
			@RequestParam(value = "limit") int limit,
			@RequestParam(value = "offset") int skip) {

		String dbName = hashtag + "_db";
		databaseExists(dbName);
		StringBuilder sb = new StringBuilder();
		sb.append(DB_URL);
		sb.append(dbName);
		sb.append("/_all_docs?include_docs=true");
		sb.append("&limit=").append(limit);
		sb.append("&skip=").append(skip);
		String endpoint = sb.toString();

		HttpGet request = new HttpGet(endpoint);
		request.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = null;
		try {
			response = client.execute(request);
			String tweetsInJson = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			Tweets tweets = mapper.readValue(tweetsInJson, Tweets.class);
			
			Resource<Tweets> rs = new Resource<>(tweets);
			rs.add(linkTo(methodOn(TweetsController.class).getTweets(hashtag, limit, skip + limit)).withRel("next"));
			return rs;
		} catch (IOException e) {
			throw new TweetParseException(e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{tweetId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Resource<TweetBody> getTweet(@PathVariable(value = "tweetId") String tweetId,
			@RequestParam(value = "hashtag") String hashtag) {

		String dbName = hashtag + "_db";
		databaseExists(dbName);
		StringBuilder sb = new StringBuilder();
		sb.append(DB_URL);
		sb.append(dbName).append("/");
		sb.append(tweetId);
		String endpoint = sb.toString();

		HttpGet request = new HttpGet(endpoint);
		request.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpClients.custom().setServiceUnavailableRetryStrategy(new ServiceUnavailableRetryStrategy() {
            @Override
            public long getRetryInterval() {
                return 0;
            }

			@Override
			public boolean retryRequest(HttpResponse response, int executionCount,
					HttpContext context) {
                int statusCode = response.getStatusLine().getStatusCode();
                return statusCode == 403 && executionCount < 5;
			}
        }).build();
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = null;
		try {
			response = client.execute(request);
			String tweetInJson = EntityUtils.toString(response.getEntity());
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//			if (1 == 1) throw new TweetParseException("Tweet parse failed");

			TweetBody tweet = mapper.readValue(tweetInJson, TweetBody.class);
			Resource<TweetBody> rs = new Resource<>(tweet);
			rs.add(linkTo(methodOn(TweetsController.class).getTweet(tweetId, hashtag)).withSelfRel());
			return rs;
		} catch (IOException e) {
			throw new TweetParseException(e.getMessage());
		}
	}
	
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{tweetId}")
	public void deleteTweet(@PathVariable(value = "tweetId") String tweetId,
			@RequestParam(value = "hashtag") String hashtag,
			@RequestParam(value = "rev") String rev) {

		String dbName = hashtag + "_db";
		databaseExists(dbName);
		StringBuilder sb = new StringBuilder();
		sb.append(DB_URL);
		sb.append(dbName).append("/");
		sb.append(tweetId);
		sb.append("?rev=");
		sb.append(rev);
		String endpoint = sb.toString();

		HttpDelete request = new HttpDelete(endpoint);
		request.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpClient client = HttpClientBuilder.create().build();
		try {
			client.execute(request);
		} catch (IOException e) {
			throw new TweetParseException(e.getMessage());
		}
	}


	public static void databaseExists(String dbName)
			throws DatabaseNotFoundException {
		StringBuilder sb = new StringBuilder();
		sb.append(DB_URL);
		sb.append(dbName);
		String endpoint = sb.toString();
//		if(1 == 1) throw new DatabaseConnectionException("Cannot connect to database");

		HttpHead request = new HttpHead(endpoint);
		HttpResponse response = null;
		HttpClient client = HttpClientBuilder.create().build();
		try {
			response = client.execute(request);
		} catch (IOException e) {
			throw new DatabaseConnectionException("Failed connecting to "
					+ dbName);
		}
//		if (1 == 1) throw new DatabaseNotFoundException("Database not found");
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			throw new DatabaseNotFoundException(dbName);
		}
	}

}
