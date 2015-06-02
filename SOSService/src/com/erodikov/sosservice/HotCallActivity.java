package com.erodikov.sosservice;

import android.app.Activity;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class HotCallActivity extends Activity {
	
	private final String LOG_TAG = "HotCallActivity";
	
		
	private SosSettings sosSettings;
	private SosServiceDBHelper dbHelper;
	
	
	Intent sosServiceBindIntent = new Intent("com.erodikov.sosservice.SosServiceImpl");
	ServiceConnection sConn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "onCreate BEGIN -->");
        
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
        fillFrom();
        Log.d(LOG_TAG, "onCreate END <--");
	}
	
	private void fillFrom(){
		StringBuilder text = new StringBuilder();
		Button btnHotCall1 = (Button)findViewById(R.id.btn_hot_call_1);
		
		if(sosSettings.getHotName1()!=null){
			text.append(sosSettings.getHotName1()).append("\n");
		}
		text.append(sosSettings.getHotNumber1());		
		btnHotCall1.setText(text.toString());
		
		Button btnHotCall2 = (Button)findViewById(R.id.btn_hot_call_2);
		text = new StringBuilder();
		if(sosSettings.getHotName2()!=null){
			text.append(sosSettings.getHotName2()).append("\n");
		}
		text.append(sosSettings.getHotNumber2());		
		btnHotCall2.setText(text.toString());
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
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
	
	public void onHotCallReleaseClick(View v){		
		Log.d(LOG_TAG, "onHotCallReleaseClick BEGIN -->");
		try{
			if(sosSettings!=null){
				sosSettings.setState("T");
				sosSettings.setLastX(0);
				sosSettings.setLastY(0);
				sosSettings.setLastZ(0);
				dbHelper.setSettings(sosSettings);				
				SosServiceAlarm sosAlarm = new SosServiceAlarm();		
		    	Boolean setAlarmResult = sosAlarm.SetAlarm(getApplicationContext(), sosSettings.gettBegin(), sosSettings.gettEnd());
		    	if(!setAlarmResult){
		    		Toast.makeText(getApplicationContext(), "Alarm reset ERROR", Toast.LENGTH_SHORT).show();
		    	}
	    	}
			finish();
		}catch(Exception e){
			 Log.d(LOG_TAG, "onHotCallReleaseClick ERROR: "+e.getMessage());
		}
		Log.d(LOG_TAG, "onHotCallReleaseClick END <--");
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
