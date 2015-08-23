package com.dorjee.rest.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import com.dorjee.model.TweetBody;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomJsonDateDeserializerTest {

	@Test 
	public void parseDateTest() throws IOException {
		String path = "src/test/resources/testTweetBody.json";
		String tweetInJson = getTestJson(path);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		try {
			TweetBody tweet = mapper.readValue(tweetInJson, TweetBody.class);
			assertNotNull(tweet.getCreatedAt());
		} catch (JsonParseException e) {
			fail("Parse failed");
		} catch (JsonMappingException e) {
			fail("Mapping failed");
		} 
	}
	
	@Test(expected = JsonMappingException.class)   
	public void mappingFailTest() throws IOException {
		String path = "src/test/resources/MapFailTweet.json";
		String tweetInJson = getTestJson(path);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,	false);
		try {
			TweetBody tweet = mapper.readValue(tweetInJson, TweetBody.class);
		} catch (JsonParseException e) {
			fail("Parse failed");
		} 
	}

	@Test(expected = JsonParseException.class)   
	public void parseFailTest() throws IOException {
		String path = "src/test/resources/ParseFailTweet.json";
		String tweetInJson = getTestJson(path);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,	false);
		TweetBody tweet = mapper.readValue(tweetInJson, TweetBody.class);
	}

	
	private String getTestJson(String path) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String json = reader.readLine();
		if (reader != null)
			reader.close();
		return json;
	}
}
