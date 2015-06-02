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
	
	static int count = 0;
	public SosServiceAlarm(){
		count++;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {		
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ww");
        wl.acquire();
        String startTime = intent.getStringExtra("startTime");
        String stopTime = intent.getStringExtra("stopTime");
        
        Log.d(LOG_TAG, "onReceive before --> "+count+" | "+startTime+" - "+stopTime);
        Intent sosServiceIntent = new Intent(context, SosServiceImpl.class);
        context.startService(sosServiceIntent);
        Log.d(LOG_TAG, "onReceive after <-- ");
        setNextAlarm(context,startTime,stopTime);
        wl.release();		
	}
	
	private void setNextAlarm(Context context, String startTime, String stopTime){
		try{			
			Log.d(LOG_TAG, "setNextAlarm begin <-- ");
			
			Calendar currenTime = Calendar.getInstance();			
			Calendar sTime = formatDateTime(startTime);
			Calendar fTime = formatDateTime(stopTime);
			
			Long deltaTime = Long.valueOf(3000);
			if(sTime.compareTo(fTime)<0){
				if(currenTime.compareTo(fTime)>0){				
					sTime.set(Calendar.DAY_OF_MONTH, sTime.get(Calendar.DAY_OF_MONTH)+1);
					deltaTime = sTime.getTimeInMillis()-currenTime.getTimeInMillis();
					Log.d(LOG_TAG, "setNextAlarm reset to next day <-- "+deltaTime);
				}	
			}else{
				if(currenTime.compareTo(sTime)<0){
					deltaTime = sTime.getTimeInMillis()-currenTime.getTimeInMillis();
				}
			}				
			
			AlarmManager am =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			Intent i = new Intent(context, SosServiceAlarm.class);
			i.putExtra("startTime", startTime);
	        i.putExtra("stopTime", stopTime);
	        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
	        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+deltaTime, pi);
			
	        Log.d(LOG_TAG, "setNextAlarm end <-- ");
        }catch(Exception e){
        	Log.d(LOG_TAG, "setNextAlarm exception: "+e.getMessage());
        }
	}
	
	public Boolean SetAlarm(Context context, String startTime, String stopTime)
    {
		try{			
			if(!checkDate(startTime)){
				return false;
			}
			
			if(!checkDate(stopTime)){
				return false;
			}
			
			
			
			
	        Calendar currentTime = Calendar.getInstance();
	        
	        Calendar sTime = formatDateTime(startTime);
	        Calendar fTime = formatDateTime(stopTime);
	        
	        if(sTime.compareTo(fTime)>0){
	        	fTime.set(Calendar.DAY_OF_MONTH, fTime.get(Calendar.DAY_OF_MONTH)+1);
	        }
	        
	        Long deltaTime = Long.valueOf(10000);
	    	if(currentTime.compareTo(sTime)<0){
	    		deltaTime = sTime.getTimeInMillis()-currentTime.getTimeInMillis();
	    	}else{	    		
	    		if(currentTime.compareTo(fTime)<0){
	    			Toast.makeText(context, context.getString(R.string.sos_start_one_minute), Toast.LENGTH_SHORT).show();	    			
	    		}else{
	    			sTime.set(Calendar.DAY_OF_MONTH, sTime.get(Calendar.DAY_OF_MONTH)+1);
	    			deltaTime = sTime.getTimeInMillis()-currentTime.getTimeInMillis();
	    		}	    		    		
	    	}	
			AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        Intent i = new Intent(context, SosServiceAlarm.class);
	        i.putExtra("startTime", startTime);
	        i.putExtra("stopTime", stopTime);
	        
	        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
	        //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+deltaTime, 1000 * 30 * 1, pi); // Millisec * Second * Minute
	        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+deltaTime, pi);
	        
	        return true;
        }catch( Exception e){
        	return false;
        }
    }
	
	private Boolean checkDate(String date){
		//TODO check date format before start
		return true;
	}

    public Boolean CancelAlarm(Context context)
    {
        try{
	    	Intent intent = new Intent(context, SosServiceAlarm.class);
	        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
	        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	        alarmManager.cancel(sender);
	        return true;
        }catch(Exception e){
        	return false;
        }
    }
    
    private Calendar formatDateTime(String timeValue){    	
    	String[] timeArray = timeValue.split("\\.");    	
    	Calendar time = Calendar.getInstance();
    	time.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
    	time.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
    	time.set(Calendar.SECOND, 0);    	
    	return time;
    }



}
