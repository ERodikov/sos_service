package com.erodikov.sosservice;

import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

public class MainActivity extends ActionBarActivity implements OnItemClickListener{
	
	private final String LOG_TAG = "MainActivity";
	
	private SosServiceDBHelper dbHelper = null;
	private SosSettings sosSettings = null;
	
	SensorManager sensorManager;
	Sensor accelerometerSensor;
	
	AlertDialog contactDualog;
	List<String> phoneList;
	
	float[] valuesAccelGravity = new float[3];
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);                
        dbHelper =  new SosServiceDBHelper(getApplicationContext());
        ContactsHelper contactsHelper = new ContactsHelper(getApplicationContext());
        phoneList = contactsHelper.getContactList();
        
    }
    
    public void onBtnStartClick(View v){    	
    	
    	sosSettings = dbHelper.getSosServiceSettings();
    	if(sosSettings==null){    		
    		sosSettings = getSosServiceSettings();
    		dbHelper.setSettings(sosSettings);    		
    	}else{
    		sosSettings.setLastX(0);
    		sosSettings.setLastY(0);
    		sosSettings.setLastZ(0);
    		sosSettings.setState("T");
    		dbHelper.setSettings(sosSettings);
    	}
    	
    	SosServiceAlarm sosAlarm = new SosServiceAlarm();
    	sosAlarm.CancelAlarm(getApplicationContext());
    	sosAlarm.SetAlarm(getApplicationContext());
    }
    
    public void onBtnStopClick(View v){    	
    	sosSettings = dbHelper.getSosServiceSettings();
    	if(sosSettings!=null && sosSettings.getState().equals(SOSServiceConstants.SOS_SERVICE_STATE_RUNNING)){
    		sosSettings.setState("F");
    		dbHelper.setSettings(sosSettings);
    	}
    	
    	SosServiceAlarm sosAlarm = new SosServiceAlarm();
    	sosAlarm.CancelAlarm(getApplicationContext());
    }
   
    private SosSettings getSosServiceSettings(){
    	SosSettings settings = null;    	
    	
    	//TODO only for test
    	settings = new SosSettings(null,null,"T","23.00","06.00",0,0,0,"+375293771906","SOS!!!","+375293771906","104");
    	//TODO only for test
    	
    	return settings;    	
    }
    
    public void onTimePickerClick(View v){
    	TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
			
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				
			}
		}, 6,0, true);
    	tpd.show();
    }
    
    public void onContactPickerClick(View v){
    	AlertDialog.Builder contactDialogBilder = new AlertDialog.Builder(MainActivity.this);
    	
    	ListView listview = new ListView(MainActivity.this);    	
    	SosDialogListAdapter arrayAdapter=new SosDialogListAdapter(MainActivity.this, phoneList);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(this);
    	
    	
    	LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(listview);
        contactDialogBilder.setView(layout);
        
        
        contactDialogBilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {        	 
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        
        contactDualog = contactDialogBilder.show();
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
		contactDualog.dismiss();
		
	}
}
