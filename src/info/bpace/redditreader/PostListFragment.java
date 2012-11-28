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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A fragment representing a single Subreddit detail screen. This fragment is
 * either contained in a {@link SubredditListActivity} in two-pane mode (on
 * tablets) or a {@link SubredditDetailActivity} on handsets.
 */
public class PostListFragment extends ListFragment {
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
	private String stringUrl;
	
	private ThingCallbacks mCallbacks = sDummyCallbacks;
	
	private static ThingCallbacks sDummyCallbacks = new ThingCallbacks() {
		public void readURI(String id, ThingCallbacks.Type type) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public PostListFragment() {
	}
	
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(String id, boolean article);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		a = getActivity();
	    layout = android.R.layout.simple_list_item_activated_1;
	    text = android.R.id.text1;
	    objects = new ArrayList<String>();
	    aa = new ArrayAdapter<String>(a, layout, text, objects);
	    
	    Bundle extras = getArguments();
	    if(extras != null) {
	    	Log.d("TEST", "Extras!");
	    	Log.d("TEST", "My extra is: " + extras.getString(ARG_ITEM_ID));
	    	stringUrl = getArguments().getString(ARG_ITEM_ID);
	    } else {
	    	Log.d("TEST", "No extras :(");
	    	stringUrl = "http://www.reddit.com/hot";
	    }
	    
	    Log.d("TEST", stringUrl);
	    
	    ConnectivityManager connMgr = 
	    		(ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    if(networkInfo != null && networkInfo.isConnected()) {
	    	new FrontpageTask().execute(stringUrl);
	    }
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof ThingCallbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (ThingCallbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.readURI(stringUrl, ThingCallbacks.Type.LINK);
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
				aa = new ArrayAdapter<String>(a, layout, text, objects);
				setListAdapter(aa);
				
				for(int i = 0; i < jposts.length(); i++) {
					aa.add(jposts.getJSONObject(i).getJSONObject("data").getString("title"));
				}
				
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
