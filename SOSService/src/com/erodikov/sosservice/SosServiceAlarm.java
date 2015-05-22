package com.erodikov.sosservice;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class SosServiceAlarm extends BroadcastReceiver{
	
	private final String LOG_TAG = "SosServiceAlarm";
	
	private String startTime;
	private String stopTime;
	private Integer checkPeriod=0;
	
	
	public SosServiceAlarm(){
		checkPeriod ++;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {		
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ww");
        wl.acquire();
        Log.d(LOG_TAG, "onReceive before --> "+stopTime);
        Intent sosServiceIntent = new Intent(context, SosServiceImpl.class);
        context.startService(sosServiceIntent);
        Log.d(LOG_TAG, "onReceive after <-- ");
        setNextAlarm(context);
        wl.release();		
	}
	
	private void setNextAlarm(Context context){
		try{			
			Log.d(LOG_TAG, "setNextAlarm begin <-- ");
			AlarmManager am =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			Intent i = new Intent(context, SosServiceAlarm.class);
	        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
	        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+30000, pi);
	        Log.d(LOG_TAG, "setNextAlarm end <-- ");
        }catch(Exception e){
        	Log.d(LOG_TAG, "setNextAlarm exception: "+e.getMessage());
        }
	}
	
	public void SetAlarm(Context context)
    {        
        Calendar currentTime = Calendar.getInstance();
        Calendar sTime = formatDateTime(startTime);
        
        Long deltaTime = Long.valueOf(10000);
    	if(currentTime.compareTo(sTime)<0){
    		deltaTime = sTime.getTimeInMillis()-currentTime.getTimeInMillis();
    	}else{
    		Toast.makeText(context, context.getString(R.string.sos_start_one_minute), Toast.LENGTH_SHORT).show();    		
    	}

		AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, SosServiceAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+deltaTime, 1000 * 30 * 1, pi); // Millisec * Second * Minute
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+deltaTime, pi);
    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, SosServiceAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
    
    private Calendar formatDateTime(String timeValue){    	
    	String[] timeArray = timeValue.split("\\.");    	
    	Calendar time = Calendar.getInstance();
    	time.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
    	time.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
    	time.set(Calendar.SECOND, 0);    	
    	return time;
    }

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStopTime() {
		return stopTime;
	}

	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}

	public Integer getCheckPeriod() {
		return checkPeriod;
	}

	public void setCheckPeriod(Integer checkPeriod) {
		this.checkPeriod = checkPeriod;
	}

}
