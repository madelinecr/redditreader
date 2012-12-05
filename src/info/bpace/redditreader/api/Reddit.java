package info.bpace.redditreader.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.view.MenuItem;

public class Reddit {

	public static final String baseUrl = "http://www.reddit.com/";

	public static class Subreddits {
		public static Subreddit[] popular() {
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
	}
}