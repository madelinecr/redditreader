package info.bpace.redditreader.api;

import org.json.JSONArray;
import org.json.JSONException;
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
		
		public static Link[] subreddit(String subreddit) {
			// TODO: Update to use new Listing class
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