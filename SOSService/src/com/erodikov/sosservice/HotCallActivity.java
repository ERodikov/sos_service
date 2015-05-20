package com.erodikov.sosservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class HotCallActivity extends Activity {
	
	private final String LOG_TAG = "HotCallActivity";
	
	private PowerManager.WakeLock wl;	
	private SosSettings sosSettings;
	private SosServiceDBHelper dbHelper;
	
	
	Intent sosServiceBindIntent = new Intent("com.erodikov.sosservice.SosServiceImpl");
	ServiceConnection sConn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "onCreate");
		//PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "HotCallWakeLock");     
        //wl.acquire();
        
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | 
			    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | 
			    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | 
			    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
			    WindowManager.LayoutParams.FLAG_FULLSCREEN | 
			    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | 
			    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | 
			    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		
		setContentView(R.layout.activity_hot_call);		
		        
        dbHelper = new SosServiceDBHelper(getApplicationContext());
        sosSettings = dbHelper.getSosServiceSettings();        
        stopSosService();
        

        
        //bindToService();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(LOG_TAG, "onResume -->");
		//Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		//Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
		//r.play();
		

        Log.d(LOG_TAG, "onResume <--");
	}
	
	public void onHotCallOneClick(View v){
		Toast.makeText(getApplicationContext(), sosSettings.getHotNumber1(), Toast.LENGTH_SHORT).show();
	}
	
	public void onHotCallTwoClick(View v){
		Toast.makeText(getApplicationContext(), sosSettings.getHotNumber2(), Toast.LENGTH_SHORT).show();
	}
	
	public void onHotCallCancelClick(View v){
		Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_SHORT).show();
		finish();
	}
	
	private void stopSosService(){
		
		SosSettings sosSettings =  dbHelper.getSosServiceSettings();		
		if(sosSettings!=null){
			sosSettings.setState("F");
			dbHelper.setSettings(sosSettings);
		}		
		SosServiceAlarm sosAlarm = new SosServiceAlarm();
    	sosAlarm.CancelAlarm(getApplicationContext());
	}
	
	protected void onDestroy() {
		super.onDestroy();
	    try{
	    	Log.d(LOG_TAG, "onDestroy");
	        Intent sosServiceIntent = new Intent(getApplicationContext(), SosServiceImpl.class);
	        stopService(sosServiceIntent);
	    }catch(Exception e){
	    	Log.d(LOG_TAG, "onStop ERROR: "+e.getMessage());
	    }
	}
}
