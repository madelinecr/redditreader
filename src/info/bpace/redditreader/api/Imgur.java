package info.bpace.redditreader.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Imgur {

	public static Bitmap grabImage(String url) {
		Bitmap bmp = null;
		if (url.matches(".*imgur\\.com\\/.{5}")) {
			// an image link missing its extension
			bmp = download(url + ".jpg");
		} else if (url.matches(".*imgur\\.com\\/.{5}\\.(jpg|png)")) {
			// a nicely formatted image link
			bmp = download(url);
		}
		return bmp;
	}

	private static Bitmap download(String url) {
		Bitmap bmp = null;
		URL urlObject = null;
		try {
			urlObject = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		try {
			bmp = BitmapFactory.decodeStream(urlObject.openConnection()
					.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bmp;
	}
}
