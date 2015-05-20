package com.erodikov.sosservice;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class SosServiceImpl extends Service implements SensorEventListener{
	
	final String LOG_TAG = "SosServiceImpl";
	
	private PowerManager.WakeLock wl;
	private SosSettings sosSettings;
	private Boolean sensorChecked = false;
	
	SensorManager sensorManager;
	Sensor accelerometerSensor;
	
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        try{
			PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
	        wl= pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SOSServiceImplLock");        
	        wl.acquire();        
			startForeground(1, new Notification());
	        
			Context context = getApplicationContext();
			SosServiceDBHelper dbHelper = new SosServiceDBHelper(context);
			sosSettings = dbHelper.getSosServiceSettings();
			
			if(sosSettings!=null){				
				if(SOSServiceConstants.SOS_SERVICE_STATE_RUNNING.equals(sosSettings.getState())){
					sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);        
				    accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
				    sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
				}else{
					wl.release();
		        	stopSelf();
				}			
			}else{				
				wl.release();
	        	stopSelf();
			}
        }catch(Exception e){
        	wl.release();
        	stopSelf();
        }
        return START_STICKY;
	}
	
	private void check(final Context context, int x, int y, int z){	
		Boolean isReleaseLock = true;
		try{		
			int lastX = sosSettings.getLastX();
			int lastY = sosSettings.getLastY();
			int lastZ = sosSettings.getLastZ();		
			
			int flipCount = -1;		
			if(lastX==0 && lastY==0 && lastZ==0){
				sosSettings.setLastX(x);
				sosSettings.setLastY(y);
				sosSettings.setLastZ(z);			
				SosServiceDBHelper dbHelper = new SosServiceDBHelper(context);
				Log.d(LOG_TAG, "check, save values: "+sosSettings.toString());
				dbHelper.setSettings(sosSettings);
				dbHelper.closeDB();
			}else{				
				Log.d(LOG_TAG, "check, Last values: x:"+lastX+", y:"+lastY+", z:"+lastZ);
				Log.d(LOG_TAG, "check, Last values: x:"+x+", y:"+y+", z:"+z);
				
				if((lastX>0 && x<0) || (lastX<0 && x>0)){
					flipCount++;
				}			
				if((lastY>0 && y<0) || (lastY<0 && y>0)){
					flipCount++;
				}						
				if((lastZ>0 && z<0) || (lastZ<0 && z>0)){
					flipCount++;
				}		
			}

			if(flipCount>=0){			
				try{
					Intent hotCallIntent = new Intent("com.erodikov.sosservice.HotCallActivity");
					hotCallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(hotCallIntent);
					
					//Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
					//Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
					//r.play();
					
					isReleaseLock = false;
				}catch(Exception e){}						
			}
		}catch(Exception e){			
		}finally{
			if(isReleaseLock){
				wl.release();		
				stopSelf();
			}
		}		
		//Log.d(LOG_TAG, "check, END");
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(!sensorChecked){
			sensorChecked = true;
			sensorManager.unregisterListener(this);
			float x = 100*event.values[0];
			float y = 100*event.values[1];
			float z = 100*event.values[2];			
			Math.round(x);
			check(getApplicationContext(),(int)x,(int)y,(int)z);
		}
	}
	
	@Override
	public void onDestroy() {
		if(wl!=null && wl.isHeld()){			
			wl.release();
		}
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
