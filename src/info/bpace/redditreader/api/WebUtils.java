package info.bpace.redditreader.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class WebUtils {

	public static Bitmap grabImage(String url, boolean thumbnail) {
		Bitmap bmp = null;
		if (url.matches(".*imgur\\.com\\/.{5}")) {
			// an imgur link missing its extension
			bmp = download(url + ".jpg");
		} else if (url.matches(".*imgur\\.com\\/.{5}\\.(jpg|png)")) {
			// a nicely formatted imgur link
			bmp = download(url);
		} else if (url.matches(".*(jpg|jpeg|png)")) {
			// a normal image on the web
			bmp = download(url);
		}
		if(bmp != null && thumbnail == true) {
			int width = bmp.getWidth();
			int height = bmp.getHeight();
			int newWidth = 256;
			int newHeight = (newWidth * height) / width;
			return Bitmap.createScaledBitmap(bmp, newWidth, newHeight, false);
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
