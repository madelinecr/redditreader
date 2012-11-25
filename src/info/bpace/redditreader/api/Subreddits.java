package info.bpace.redditreader.api;

public class Subreddits {
	
	public static final String baseUrl = "http://www.reddit.com/";
	
	public static String popular() {
		return baseUrl + "reddits/popular.json";
	}
	
	public static String subreddit(String subreddit) {
		return baseUrl + "r/" + subreddit + ".json";
	}
}
