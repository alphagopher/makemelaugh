package com.aporter.makemelaugh;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.aporter.makemelaugh.R;
import com.google.android.glass.app.Card;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

public class MainActivity extends Activity {

	//private static final String TAG = "MakeMeLaugh";
	private static final String URL = "http://api.icndb.com/jokes/random";
	private Card card;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {

			card = new Card(this);
			card.setText(R.string.text_default_joke);
			card.setFootnote(R.string.text_footer);
			setContentView(card.toView());
			
			GetJokeTask task = new GetJokeTask();
			task.execute(new String[] { URL });

		} catch (Exception e) {

			e.printStackTrace();
			System.exit(1);
		}

	}

	private class GetJokeTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			for (String url : urls) {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				try {
					HttpResponse execute = client.execute(httpGet);
					InputStream content = execute.getEntity().getContent();

					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(content));
					String s = "";
					while ((s = buffer.readLine()) != null) {
						response += s;
					}
					response = new JSONObject(response).getJSONObject("value").getString("joke");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return response;
		}

		// @Override
		protected void onPostExecute(String result) {
			card.setText(result);
			setContentView(card.toView());
		}
	}
}
