package info.bpace.redditreader.api;

public class Listings {
	
	public static final String baseUrl = "http://www.reddit.com/";
	
	public static String hotPosts() {
		return baseUrl + "hot.json";
	}
	
	public static String newPosts() {
		return baseUrl + "new.json";
	}
	
	public static String topPosts() {
		return baseUrl + "top.json";
	}
}
