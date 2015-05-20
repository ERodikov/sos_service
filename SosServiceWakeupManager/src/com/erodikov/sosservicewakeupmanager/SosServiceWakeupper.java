package com.erodikov.sosservicewakeupmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;

public class SosServiceWakeupper extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();        
        Uri notification1 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);		
	    Ringtone r1 = RingtoneManager.getRingtone(context, notification1);
	    r1.play();        
        wl.release();		
	}
	
	public void SetAlarm(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, SosServiceWakeupper.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 30 * 1, pi); // Millisec * Second * Minute
    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, SosServiceWakeupper.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}