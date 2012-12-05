package info.bpace.redditreader.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class Query {
	
	private static final int QUERY_RESPONSE_LENGTH = 500000;

	public static JSONObject downloadUrl(String newUrl) {
		InputStream is = null;
		JSONObject content = null;
		int len = QUERY_RESPONSE_LENGTH;

		try {
			URL url = new URL(newUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);

			conn.connect();
			//int response = conn.getResponseCode();
			is = conn.getInputStream();

			content = new JSONObject(readIt(is, len));
		} catch(IOException e) {
			e.printStackTrace();
		} catch(JSONException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return content;
	}

	private static String readIt(InputStream stream, int len)
			throws IOException, UnsupportedEncodingException {

		Reader reader = null;
		reader = new InputStreamReader(stream, "UTF-8");
		char[] buffer = new char[len];
		reader.read(buffer);

		return new String(buffer);
	}
}