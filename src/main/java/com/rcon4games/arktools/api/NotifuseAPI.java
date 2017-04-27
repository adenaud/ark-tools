package com.rcon4games.arktools.api;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by Anthony Denaud on 27/04/17.
 * Copyright Personalized-Software Ltd
 */
@Component
@Scope("singleton")
public class NotifuseAPI {

	@Value("${notifuse.url}")
	private String API_URL;
	@Value("${notifuse.key}")
	private String API_KEY;

	@Value("${notifuse.channel}")
	private String CHANNEL;

	@Value("${notifuse.notification}")
	private String NOTIFICATION;

	@Value("${notifuse.from.name}")
	private String FROM_NAME;

	@Value("${notifuse.from.generic}")
	private String EMAIL_FROM_GENERIC;

	public void sendMessage(String recipient, String[] cc, String[] bcc, String template, JSONObject data) throws IOException, NotifuseException {

		if(data == null){
			data = new JSONObject();
		}

		JSONObject root = new JSONObject();
		JSONArray messages = new JSONArray();
		JSONObject message = new JSONObject();

		message.put("notification", NOTIFICATION);
		message.put("channel", CHANNEL);
		message.put("template", template);
		message.put("templateData", data);



		message.put("contact", String.valueOf(1));
		JSONObject contact = new JSONObject();
		contact.put("firstName", recipient);
		contact.put("lastName", "--");
		contact.put("email", recipient);


		message.put("contactProfile", new JSONObject().put("$set", contact));

		if ((cc != null && cc.length > 0) || (bcc != null && bcc.length > 0)) {
			JSONObject channelOptions = new JSONObject();
			if (cc != null && cc.length > 0) {
				channelOptions.put("cc", cc);
			}
			if (bcc != null && bcc.length > 0) {
				channelOptions.put("bcc", bcc);
			}
			channelOptions.put("from", EMAIL_FROM_GENERIC);
			channelOptions.put("name", FROM_NAME);
			message.put("channelOptions", channelOptions);
		}

		messages.put(message);
		root.put("messages", messages);

		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		HttpClient client = httpClientBuilder.build();

		HttpPost request = new HttpPost(API_URL + "messages.send");
		request.addHeader("Authorization", "Bearer " + API_KEY);
		request.addHeader("Content-Type", "application/json");
		request.setEntity(new StringEntity(root.toString(), "UTF-8"));

		HttpResponse response = client.execute(request);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new NotifuseException(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase(), "");
		}

		String content = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
		JSONObject jsonResponse = new JSONObject(content);

		if (!jsonResponse.has("success") && jsonResponse.has("error")) {
			System.out.println(content);
			throw new NotifuseException(jsonResponse.getInt("statusCode"),
					jsonResponse.getString("error"),
					jsonResponse.getString("message"));
		}

		if (jsonResponse.has("success") && !jsonResponse.getBoolean("success")) {
			System.out.println(content);
			String error = ((JSONObject) jsonResponse.getJSONArray("failed").get(0)).getString("error");
			throw new NotifuseException(jsonResponse.getInt("statusCode"), error, "");
		}

	}

}
