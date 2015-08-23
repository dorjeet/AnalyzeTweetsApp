package com.dorjee.rest;

import java.io.IOException;
import java.net.URI;




import org.apache.commons.codec.Charsets;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;

import com.dorjee.exceptions.TweetParseException;
import com.dorjee.model.Comment;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/comments")
public class CommentsController {
	private static String COMMENTS_DB = "comments_db";
	private static String DB_URL = "http://localhost:5984/";

	
	@RequestMapping(method = RequestMethod.POST, value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> submitComment(@RequestBody Comment comment) {
		StringBuilder sb =  new StringBuilder();
		sb.append(DB_URL);
		sb.append(COMMENTS_DB);
		String endpoint = sb.toString();
		
		HttpPost request = new HttpPost(endpoint);
		request.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		ObjectMapper mapper = new ObjectMapper();
		String output = null;
		try {
			output = mapper.writeValueAsString(comment);
		} catch (IOException e) {
			throw new TweetParseException(e.getMessage());
		}
		request.setEntity(new StringEntity(output, Charsets.UTF_8));
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = null;
		try {
			response = client.execute(request);
			HttpHeaders headers = new HttpHeaders();
			if (response.getStatusLine().getStatusCode() == HttpStatus.CREATED.value()) {
				headers.setLocation(URI.create(response.getHeaders(HttpHeaders.LOCATION)[0].getValue()));
			}
			return new ResponseEntity<>(headers, HttpStatus.CREATED);
		} catch(IOException e) {
			throw new TweetParseException(e.getMessage());
			
		} 
	}

}
