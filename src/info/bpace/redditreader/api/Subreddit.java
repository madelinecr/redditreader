package info.bpace.redditreader.api;

import org.json.JSONException;
import org.json.JSONObject;

public class Subreddit extends Thing {
	private String displayname;
	
	public Subreddit(JSONObject jobject) {
		super(jobject);
		try {
			displayname = jobject.getJSONObject("data").getString("display_name");
		} catch(JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String toString() {
		return displayname;
	}
	
	public String getDisplayName() {
		return displayname;
	}
}
