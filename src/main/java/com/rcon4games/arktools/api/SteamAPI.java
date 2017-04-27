package com.rcon4games.arktools.api;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Anthony Denaud on 27/04/17.
 * Copyright Personalized-Software Ltd
 */
@Component
@Scope("singleton")
public class SteamAPI {

	private static final String API_URL = "https://api.steampowered.com/ISteamRemoteStorage/GetPublishedFileDetails/v1/";


	public SteamAPI() {

	}

	public DateTime getModLastUpdate(long fileId) throws IOException{


		HttpPost post = new HttpPost(API_URL);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("itemcount", "1"));
		parameters.add(new BasicNameValuePair("publishedfileids[0]", String.valueOf(fileId)));
		post.setEntity(new UrlEncodedFormEntity(parameters));

		HttpClientBuilder builder = HttpClientBuilder.create();
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(2000).build();
		HttpClient client = builder.setDefaultRequestConfig(requestConfig).build();
		HttpResponse response = client.execute(post);

		if (response.getStatusLine().getStatusCode() == 200) {
			String content = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

			JSONObject jsonObject = new JSONObject(content);
			JSONObject details = jsonObject.getJSONObject("response").getJSONArray("publishedfiledetails").getJSONObject(0);
			Date date = new Date(details.getLong("time_updated") * 1000);

			return new DateTime(date).withZone(DateTimeZone.UTC);
		}
		else{
			throw new IOException();
		}
	}
}
