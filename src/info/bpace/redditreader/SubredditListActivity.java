package info.bpace.redditreader;

import info.bpace.redditreader.api.Link;
import info.bpace.redditreader.api.Subreddit;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * An activity representing a list of Subreddits. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link SubredditDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link SubredditListFragment} and the item details (if present) is a
 * {@link LinkListFragment}.
 * <p>
 * This activity also implements the required
 * {@link SubredditListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class SubredditListActivity extends FragmentActivity implements
		LinkListFragment.Callbacks, SubredditListFragment.Callbacks {
	
	private static final String TAG = "subredditlistactivity";

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subreddit_list);

		if (findViewById(R.id.subreddit_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((SubredditListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.subreddit_list))
					.setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public void readSubreddit(Subreddit subreddit) {
		Bundle arguments = new Bundle();
		arguments.putString(LinkListFragment.ARG_ITEM_ID, subreddit.getDisplayName());
		LinkListFragment fragment = new LinkListFragment();
		fragment.setArguments(arguments);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.subreddit_detail_container, fragment)
				.commit();
	}

	@Override
	public void readLink(Link link) {
		Intent postIntent = new Intent(this, LinkListActivity.class);
		postIntent.putExtra(LinkListFragment.ARG_ITEM_ID, link.getSubreddit());
		startActivity(postIntent);
		Log.d(TAG, "This is a link, intent fired.");		
	}
}
