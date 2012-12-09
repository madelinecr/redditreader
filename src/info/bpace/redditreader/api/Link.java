package info.bpace.redditreader.api;

import org.json.JSONException;
import org.json.JSONObject;

public class Link extends Thing {
	private String title;
	private String selftext;
	private String subreddit;

	public Link(JSONObject jobject) {
		super(jobject);
		try {
			JSONObject jdata = jobject.getJSONObject("data");
			title = jdata.getString("title");
			selftext = jdata.getString("selftext");
			subreddit = jdata.getString("subreddit");
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
	
	public String getSelftext() {
		return selftext;
	}
	
	public String getSubreddit() {
		return subreddit;
	}
}