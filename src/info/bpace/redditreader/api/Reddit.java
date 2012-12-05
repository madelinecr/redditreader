package info.bpace.redditreader.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Reddit {

	public static final String baseUrl = "http://www.reddit.com/";

	public static class Subreddits {
		public static Subreddit[] popular() {
			Log.d("REDDITREADER", "Returning popular subreddits...");
			JSONObject response = Query.downloadUrl(baseUrl + "reddits/popular.json");
			Subreddit[] subreddits = null;
			try {
				JSONArray children = response.getJSONObject("data").getJSONArray("children");
				subreddits = new Subreddit[children.length()];
				//Log.d(DEBUG_TAG, result);
				for(int i = 0; i < children.length(); i++) {
					Subreddit subreddit = new Subreddit(children.getJSONObject(i));
					subreddits[i] = subreddit;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				//Log.e(DEBUG_TAG, "Error, exception occured: " + e);
			}
			return subreddits;
		}
		
		public static Link[] subreddit(String subreddit) {
			JSONObject response = Query.downloadUrl(baseUrl + "r/" + subreddit + ".json");
			Link[] links = null;
			try {
				JSONArray children = response.getJSONObject("data").getJSONArray("children");
				links = new Link[children.length()];
				for(int i = 0; i < children.length(); i++) {
					Link link = new Link(children.getJSONObject(i));
					links[i] = link;
				}
			} catch(JSONException e) {
				e.printStackTrace();
			}
			return links;
		}
	}
}