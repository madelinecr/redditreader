package info.bpace.redditreader.api;

import org.json.JSONObject;

import android.util.Log;

public class Reddit {

	public static final String baseUrl = "http://www.reddit.com/";

	public static class Subreddits {
		public static Thing[] popular() {
			Log.d("REDDITREADER", "Returning popular subreddits...");
			JSONObject response = Query.downloadUrl(baseUrl + "reddits/popular.json");
			Listing list = new Listing(response);
			return list.getList();
		}
		
		public static Thing[] subreddit(String subreddit) {
			// TODO: Update to use new Listing class
			JSONObject response = Query.downloadUrl(baseUrl + "r/" + subreddit + ".json");
			Listing list = new Listing(response);
			return list.getList();
		}
	}
}