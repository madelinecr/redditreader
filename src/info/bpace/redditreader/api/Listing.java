package info.bpace.redditreader.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Listing {
	public Thing[] list;

	public Listing(JSONObject jobject) {
		try {
			JSONArray children = jobject.getJSONObject("data").getJSONArray("children");
			list = new Thing[children.length()];
			for(int i = 0; i < children.length(); i++) {
				Thing thing = makeThing(children.getJSONObject(i));
				list[i] = thing;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public Thing[] getList() {
		return list;
	}
	
	private Thing makeThing(JSONObject jobject) {
		Thing newThing = null;
		try {
			String kind = jobject.getString("kind");
			if(kind.equals("t3")) {
				return new Link(jobject);
			}
			else if(kind.equals("t5")) {
				return new Subreddit(jobject);
			} else {
				newThing = new Thing(jobject);
			}
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return newThing;
	}
}