package com.erodikov.sosservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

public class SosServiceAlarm extends BroadcastReceiver{
	
	private final String LOG_TAG = "SosServiceAlarm";

	@Override
	public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ww");
        wl.acquire();
        Log.d(LOG_TAG, "onReceive before --> ");
        Intent sosServiceIntent = new Intent(context, SosServiceImpl.class);
        context.startService(sosServiceIntent); 
        Log.d(LOG_TAG, "onReceive after <-- ");
        wl.release();		
	}
	
	public void SetAlarm(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, SosServiceAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+2000, 1000 * 60 * 10, pi); // Millisec * Second * Minute
    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, SosServiceAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}
