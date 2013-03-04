package info.bpace.redditreader;

import info.bpace.redditreader.api.Link;
import info.bpace.redditreader.api.WebUtils;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
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
	
	private LruCache<String, Bitmap> bitmapCache;

	public LinkAdapter(Activity a, Link[] d) {
		activity = a;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		data = d;
		
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;
		
		bitmapCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount() / 1024;
			}
		};
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
		ImageView thumbnail = (ImageView) vi.findViewById(R.id.thumbnail);

		Log.v("REDDIT", "making new view from " + data[position].getUrl());
		new ThumbTask(vi).execute(data[position].getUrl());

		title.setText(data[position].getTitle());
		subtitle.setText("by " + data[position].getAuthor());
		commentcount.setText(data[position].getCommentCount() + " comments");
		score.setText(data[position].getScore() + " points");
		thumbnail.setImageBitmap(null);
		return vi;
	}

	private class ThumbTask extends AsyncTask<String, Void, Bitmap> {

		private View view;

		public ThumbTask(View vi) {
			view = vi;
		}

		@Override
		protected Bitmap doInBackground(String... url) {
			String imageName = url[0];
			Log.v("REDDIT", "String: " + imageName);
			Bitmap bmp = bitmapCache.get(imageName);
			
			// if the cache didn't return anything
			if(bmp == null) {
				bmp = WebUtils.grabImage(url[0], true);
				// if we got a valid bitmap from the net
				if(bmp != null) {
					bitmapCache.put(imageName, bmp);
				}
			}
			return bmp;
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
