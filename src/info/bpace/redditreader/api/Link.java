package info.bpace.redditreader.api;

import org.json.JSONException;
import org.json.JSONObject;

public class Link extends Thing {
	private String title;
	private String selftext;
	private String url;
	private String score;
	private String subreddit;

	public Link(JSONObject jobject) {
		super(jobject);
		try {
			JSONObject jdata = jobject.getJSONObject("data");
			title = jdata.getString("title");
			selftext = jdata.getString("selftext");
			url = jdata.getString("url");
			score = jdata.getString("score");
			
			subreddit = jdata.getString("subreddit");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return title;
	}

	public String getTitle() {
		return title;
	}

	public String getSelftext() {
		return selftext;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getScore() {
		return score;
	}

	public String getSubreddit() {
		return subreddit;
	}
}