package info.bpace.redditreader;

import info.bpace.redditreader.api.Listings;
import info.bpace.redditreader.api.Subreddits;

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
 * A list fragment representing a list of Subreddits. This fragment also
 * supports tablet devices by allowing list items to be given an 'activated'
 * state upon selection. This helps indicate which item is currently being
 * viewed in a {@link PostListFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class SubredditListFragment extends ListFragment {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	
	private static String DEBUG_TAG = "REDDITREADER";
	private static List<MenuItem> categories = new ArrayList<MenuItem>();
	private ArrayAdapter<MenuItem> aa = null;
	private Activity a = null;
	private int text = 0;
	private int layout = 0;
	
	private static class MenuItem {
		public String id;
		public String title;
		
		public MenuItem(String newId, String newTitle) { id = newId; title = newTitle; }
		@Override
		public String toString() { return title; }
	}
	
	static {
		categories.add(new MenuItem(Listings.hotPosts(), "Front Page"));
		categories.add(new MenuItem(Listings.newPosts(), "New"));
		categories.add(new MenuItem(Listings.topPosts(), "Top"));
	}

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private ThingCallbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(String id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static ThingCallbacks sDummyCallbacks = new ThingCallbacks() {
		@Override
		public void readURI(String id, ThingCallbacks.Type type) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public SubredditListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	    a = getActivity();
	    layout = android.R.layout.simple_list_item_activated_1;
	    text = android.R.id.text1;
	    aa = new ArrayAdapter<MenuItem>(a, layout, text, categories);
	    
	    ConnectivityManager connMgr = 
	    		(ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    if(networkInfo != null && networkInfo.isConnected()) {
	    	Log.d(DEBUG_TAG, "Retrieving popular reddits...");
	    	new SubredditTask().execute(Subreddits.popular());
	    }

	    setListAdapter(aa);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
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
		mCallbacks.readURI(categories.get(position).id, ThingCallbacks.Type.SUBREDDIT);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
	
	public class SubredditTask extends AsyncTask<String, String, String> {

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
			
				Log.d(DEBUG_TAG, result);
				for(int i = 0; i < jposts.length(); i++) {
					String category = jposts.getJSONObject(i).getJSONObject("data").getString("display_name");
//					Log.d(DEBUG_TAG, jposts.getJSONObject(i).getJSONObject("data").getString("display_name"));
					MenuItem newCategory = new MenuItem(Subreddits.subreddit(category), category);
					aa.add(newCategory);
				}
				
			} catch (JSONException e) {
				Log.e(DEBUG_TAG, "Error, exception occured: " + e);
			}
		}

		private String downloadUrl(Object newUrl) throws IOException {
			InputStream is = null;
			int len = 500000;

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