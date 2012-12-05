package info.bpace.redditreader.api;

import org.json.JSONException;
import org.json.JSONObject;

public class Thing {
	private String id;
	private String name;
	private String kind;
	
	public Thing(JSONObject jobject) {
		try {
			JSONObject jdata = jobject.getJSONObject("data");
			id = jdata.getString("id");
			name = jdata.getString("name");
			kind = jdata.getString("kind");
		} catch(JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String toString() {
		return name;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getKind() {
		return kind;
	}
}