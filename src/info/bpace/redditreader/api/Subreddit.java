package info.bpace.redditreader.api;

import org.json.JSONException;
import org.json.JSONObject;

public class Subreddit {
	private String displayName;
	public String uri;
	
	public Subreddit(JSONObject jobject) {
		try {
		displayName = jobject.getJSONObject("data").getString("display_name");
		} catch(JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String toString() {
		return displayName;
	}
}
