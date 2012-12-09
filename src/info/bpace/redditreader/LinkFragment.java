package info.bpace.redditreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LinkFragment extends Fragment {
	
	public static final String ARG_LINK_ID = "link_id";
	public static final String ARG_LINK_TITLE = "link_title";
	public static final String ARG_LINK_SELFTEXT = "link_selftext";
	private static final String TAG = "linkfragment";
	
	private String linkId;
	private String linkTitle;
	private String linkSelftext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getArguments();
		if(extras != null) {
			Log.d(TAG, extras.getString(ARG_LINK_ID));
			linkId = extras.getString(ARG_LINK_ID);
			linkTitle = extras.getString(ARG_LINK_TITLE);
			linkSelftext = extras.getString(ARG_LINK_SELFTEXT);
//			TextView tvTitle = (TextView) findViewById(R.id.post_title);
//			tvTitle.setText(extras.getString(ARG_LINK_TITLE));
		}
	}
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_post, container, false);
		((TextView) view.findViewById(R.id.post_title)).setText(linkTitle);
		((TextView) view.findViewById(R.id.post_content)).setText(linkSelftext);
		return view;
	}
}
