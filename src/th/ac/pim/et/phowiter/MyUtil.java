package th.ac.pim.et.phowiter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class MyUtil {

	// Activity request codes
	public static final int MEDIA_TYPE_IMAGE = 1;

	// directory name to store captured images and videos
	private static final String IMAGE_DIRECTORY_NAME = "PhoWiter";

	public static boolean isConnected(Context context){
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo() != null;
	}

	public static boolean isCameraInstalled(Context context) {
		return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}

	public static boolean isGPS(Context context) {
		LocationManager manager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	private static File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(
				Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
						+ IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
			Log.i("Images Paht", mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}

		return mediaFile;
	}

	public static Bitmap drawTextToBitmap(Context context,  Uri fileUri, String location) {
		
		try {
			Resources resources = context.getResources();
			float scale = resources.getDisplayMetrics().density;

			BitmapFactory.Options options = new BitmapFactory.Options();
			Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);

			android.graphics.Bitmap.Config bitmapConfig =   bitmap.getConfig();
			// set default bitmap config if none
			if(bitmapConfig == null) {
				bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
			}
			// resource bitmaps are imutable,
			// so we need to convert it to mutable one
			bitmap = bitmap.copy(bitmapConfig, true);

			Canvas canvas = new Canvas(bitmap);
			// new antialised Paint
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			// text color - #3D3D3D
			paint.setColor(Color.rgb(110,110, 110));
			// text size in pixels
			paint.setTextSize((int) (120 * scale));
			// text shadow
			paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);

			// draw text to the Canvas center
			Rect bounds = new Rect();
			paint.getTextBounds(location, 0, location.length(), bounds);
			int x = (bitmap.getWidth() - bounds.width())/6;
			int y = (bitmap.getHeight() + bounds.height())/5;

			canvas.drawText(location, x * scale, y * scale, paint);

			return bitmap;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public static String getLocation(Context context) {
		LocationManager lcm = (LocationManager) context.getSystemService(
				Context.LOCATION_SERVICE);
		
		String provider = LocationManager.NETWORK_PROVIDER;
		Location loc = lcm.getLastKnownLocation(provider);
		String addressText = "";	
		Geocoder geocoder = new Geocoder(context, Locale.getDefault());
		List<Address> addresses = null;
		try {
			addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (addresses != null && addresses.size() > 0) {
			Address address = addresses.get(0);

//			addressText = String.format("%s %s",
//					address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
//							address.getLocality()); // 
			
			addressText = address.getLatitude() + "\n" + address.getLongitude();
		}

		return addressText;
	}
}