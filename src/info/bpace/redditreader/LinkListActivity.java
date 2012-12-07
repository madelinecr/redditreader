package info.bpace.redditreader;

import info.bpace.redditreader.api.Link;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class LinkListActivity extends FragmentActivity 
	implements LinkListFragment.Callbacks {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_twopane);
		
		LinkListFragment fragment = new LinkListFragment();
		fragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.post_list, fragment)
				.commit();
	}

	@Override
	public void readLink(Link link) {
		Bundle arguments = new Bundle();
		arguments.putString(LinkFragment.ARG_LINK_ID, link.getId());
		LinkFragment fragment = new LinkFragment();
		fragment.setArguments(arguments);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.post_detail_container, fragment)
				.commit();
	}
}
