package com.rcon4games.arktools.tools;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ModUpdateChecker {

	private static final String API_URL = "https://api.steampowered.com/ISteamRemoteStorage/GetPublishedFileDetails/v1/";

	private String[] modIds = {"566885854", "693416678", "670764308",  "793605978", "621154190"};

	public ModUpdateChecker() {

	}


	public void check() throws IOException {

		HttpClientBuilder builder = HttpClientBuilder.create();
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(2000).build();


		for (String modId : modIds) {
			HttpClient client = builder.setDefaultRequestConfig(requestConfig).build();

			HttpPost post = new HttpPost(API_URL);
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("itemcount", "1"));
			parameters.add(new BasicNameValuePair("publishedfileids[0]", modId));
			post.setEntity(new UrlEncodedFormEntity(parameters));

			HttpResponse response = client.execute(post);

			if (response.getStatusLine().getStatusCode() == 200) {
				String content = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
				JSONObject jsonObject = new JSONObject(content);
				JSONObject details = jsonObject.getJSONObject("response").getJSONArray("publishedfiledetails").getJSONObject(0);
				System.out.println(details.getString("title"));
				Date date = new Date(details.getLong("time_updated") * 1000);
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY HH:mm");
				dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
				System.out.println(dateFormat.format(date));
			}
		}
	}
}
