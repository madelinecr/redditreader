package info.bpace.redditreader;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class LinkListActivity extends FragmentActivity 
	implements ThingCallbacks {

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
	public void readThing(String id, Type type) {
		// TODO Auto-generated method stub
		
		LinkFragment fragment = new LinkFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.post_detail_container, fragment)
				.commit();
	}
}
