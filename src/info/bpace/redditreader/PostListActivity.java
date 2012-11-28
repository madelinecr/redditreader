package info.bpace.redditreader;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class PostListActivity extends FragmentActivity 
	implements ThingCallbacks {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_twopane);
		
		PostListFragment fragment = new PostListFragment();
		fragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.post_list, fragment)
				.commit();
	}

	@Override
	public void readURI(String id, Type type) {
		// TODO Auto-generated method stub
		
		PostFragment fragment = new PostFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.post_detail_container, fragment)
				.commit();
	}
}
