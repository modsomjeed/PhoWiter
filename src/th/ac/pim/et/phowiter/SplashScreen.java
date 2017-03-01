package th.ac.pim.et.phowiter;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class SplashScreen extends Activity {

	private CheckBox cameraCB;
	private CheckBox internetCB;
	private CheckBox gpsCB;
	private TextView status;
	
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		cameraCB = (CheckBox) findViewById(R.id.Camera_CheckBox);
		internetCB = (CheckBox) findViewById(R.id.Internet_CheckBox);
		gpsCB = (CheckBox) findViewById(R.id.GPS_CheckBox);
		status = (TextView) findViewById(R.id.status);
		
		intent = new Intent(this, EditImage.class);
		
        final Button button = (Button) findViewById(R.id.but_continue);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                startActivity(intent);
            }
        });
	}

	@Override
	protected void onResume(){
		super.onResume();
		
		if(check()) {			
	        Handler handler = new Handler();
	        handler.postDelayed(new Runnable() {
	            @Override
	            public void run() {
	                finish();
	                startActivity(intent);
	            }
	        }, 1500);
		}
	}

	/* 
	 * dawn/sunrise	before morning
	 * dusk/sunset	before night
	 * twilight		between above
	 * 
	 * morning		[05:00	12:00)
	 * noon			[12:00	13:00)
	 * afternoon	[13:00	18:00)
	 * evening		[18:00	20:00)
	 * night		[20:00	23:30)
	 * midnight		[23:30	00:00)
	 * late night	[00:00	04:00)
	 * ????			[04:00	05:00)
	 */

	protected boolean check(){

		boolean isAll = true;
		if (MyUtil.isCameraInstalled(this)) {
			cameraCB.setChecked(true);
		} else {
			cameraCB.setChecked(false);
			isAll = false;
		}

		if (MyUtil.isConnected(this)) {
			internetCB.setChecked(true);
		} else {
			internetCB.setChecked(false);
			isAll = false;
		}

		if (MyUtil.isGPS(this)) {
			gpsCB.setChecked(true);
		} else {
			gpsCB.setChecked(false);
			isAll = false;
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
			.setCancelable(false)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
					startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
				}
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
					dialog.cancel();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
		status.setText("Checked");
		
		return isAll;
	}
}