package info.bpace.redditreader;

import info.bpace.redditreader.api.Reddit;
import info.bpace.redditreader.api.Subreddit;

import java.util.ArrayList;
import java.util.List;

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
	private static List<Subreddit> categories = new ArrayList<Subreddit>();
	private ArrayAdapter<Subreddit> aa = null;
	private Activity a = null;
	private int text = 0;
	private int layout = 0;
	
	static {
		//categories.add(new MenuItem(Listings.hotPosts(), "Front Page"));
		//categories.add(new MenuItem(Listings.newPosts(), "New"));
		//categories.add(new MenuItem(Listings.topPosts(), "Top"));
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
	    aa = new ArrayAdapter<Subreddit>(a, layout, text, categories);
	    
	    ConnectivityManager connMgr = 
	    		(ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    if(networkInfo != null && networkInfo.isConnected()) {
	    	Log.d(DEBUG_TAG, "Retrieving popular reddits...");
	    	new SubredditTask().execute();
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
		mCallbacks.readURI(categories.get(position).uri, ThingCallbacks.Type.SUBREDDIT);
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
	
	public class SubredditTask extends AsyncTask<Void, String, Subreddit[]> {

		@Override
		protected Subreddit[] doInBackground(Void... arg0) {
			return Reddit.Subreddits.popular();
		}
		
		@Override
		protected void onPostExecute(Subreddit[] result) {
			for(int i = 0; i < result.length; i++) {
				aa.add(result[i]);
			}
		}
	}
}