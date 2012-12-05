package info.bpace.redditreader.api;

import org.json.JSONException;
import org.json.JSONObject;

public class Link extends Thing {
	private String title;

	public Link(JSONObject jobject) {
		super(jobject);
		try {
			title = jobject.getJSONObject("data").getString("title");
		} catch(JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String toString() {
		return title;
	}
	
	public String getTitle() {
		return title;
	}
}