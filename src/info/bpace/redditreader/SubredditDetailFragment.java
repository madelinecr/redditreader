package info.bpace.redditreader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

/**
 * A fragment representing a single Subreddit detail screen. This fragment is
 * either contained in a {@link SubredditListActivity} in two-pane mode (on
 * tablets) or a {@link SubredditDetailActivity} on handsets.
 */
public class SubredditDetailFragment extends ListFragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";
	
	private List<String> objects = null;
	private ArrayAdapter<String> aa = null;
	private Activity a = null;
	private int text = 0;
	private int layout = 0;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public SubredditDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		a = getActivity();
	    layout = android.R.layout.simple_list_item_activated_1;
	    text = android.R.id.text1;
	    objects = new ArrayList<String>();
	    aa = new ArrayAdapter<String>(a, layout, text, objects);
	    
	    String stringUrl = "http://www.reddit.com/hot.json";
	    ConnectivityManager connMgr = 
	    		(ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    if(networkInfo != null && networkInfo.isConnected()) {
	    	new FrontpageTask().execute(stringUrl);
	    }
	}

	public class FrontpageTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... urls) {
			// TODO Auto-generated method stub
			try {
				return downloadUrl(urls[0]);
			} catch (IOException e) {
				return "Unable to retrieve web page. URL may be invalid.";
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject jobject = new JSONObject(result);
				JSONArray jposts = jobject.getJSONObject("data").getJSONArray("children");
				objects = new ArrayList<String>();
				for(int i = 0; i < jposts.length(); i++) {
					objects.add(jposts.getJSONObject(i).getJSONObject("data").getString("title"));
				}
				aa = new ArrayAdapter<String>(a, layout, text, objects);
				//ListView lv = (ListView) a.findViewById(R.id.subreddit_list);
				setListAdapter(aa);
			} catch (JSONException e) {
				//Log.e(DEBUG_TAG, "Error, exception occured: " + e);
			}
		}

		private String downloadUrl(Object newUrl) throws IOException {
			InputStream is = null;
			int len = 40000;

			try {
				URL url = new URL((String) newUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setReadTimeout(10000);
				conn.setConnectTimeout(15000);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);

				conn.connect();
				//int response = conn.getResponseCode();
				is = conn.getInputStream();

				String contentAsString = readIt(is, len);
				return contentAsString;

			} finally {
				if (is != null) {
					is.close();
				}
			}
		}

		private String readIt(InputStream stream, int len)
				throws IOException, UnsupportedEncodingException {

			Reader reader = null;
			reader = new InputStreamReader(stream, "UTF-8");
			char[] buffer = new char[len];
			reader.read(buffer);

			return new String(buffer);
		}
	}
}
