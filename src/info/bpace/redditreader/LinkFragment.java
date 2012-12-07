package info.bpace.redditreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LinkFragment extends Fragment {
	
	public static final String ARG_LINK_ID = "link_id";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getArguments();
		if(extras != null) {
			Log.d("LINK", extras.getString(ARG_LINK_ID));
		}
	}
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_post, container, false);
	}
}
