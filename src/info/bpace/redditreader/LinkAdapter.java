package info.bpace.redditreader;

import info.bpace.redditreader.api.WebUtils;
import info.bpace.redditreader.api.Link;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LinkAdapter extends BaseAdapter {

	private Activity activity;
	private LayoutInflater inflater;
	private Link[] data;

	public LinkAdapter(Activity a, Link[] d) {
		activity = a;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		data = d;
	}

	@Override
	public int getCount() {
		return data.length;
	}

	@Override
	public Object getItem(int position) {
		return data[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.link_row, null);

		TextView title = (TextView) vi.findViewById(R.id.title);
		TextView subtitle = (TextView) vi.findViewById(R.id.author);
		TextView commentcount = (TextView) vi.findViewById(R.id.commentcount);
		TextView score = (TextView) vi.findViewById(R.id.score);

		new ThumbTask(vi).execute(data[position].getUrl());

		title.setText(data[position].getTitle());
		subtitle.setText("by " + data[position].getAuthor());
		commentcount.setText(data[position].getCommentCount() + " comments");
		score.setText(data[position].getScore() + " points");
		return vi;
	}

	private class ThumbTask extends AsyncTask<String, Void, Bitmap> {

		private View view;

		public ThumbTask(View vi) {
			view = vi;
		}

		@Override
		protected Bitmap doInBackground(String... url) {
			return WebUtils.grabImage(url[0], true);
		}

		@Override
		protected void onPostExecute(Bitmap bmp) {
			ImageView iv = (ImageView) view.findViewById(R.id.thumbnail);
			if(bmp != null) {
				iv.setImageBitmap(bmp);
			}
		}
	}
}
