package info.bpace.redditreader;

public interface ThingCallbacks {
	enum Type {
		COMMENT, ACCOUNT, LINK, MESSAGE, SUBREDDIT
	}
	
	public void readURI(String id, Type type);
}
