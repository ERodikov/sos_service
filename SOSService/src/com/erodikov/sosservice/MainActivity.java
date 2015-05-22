package com.erodikov.sosservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnItemClickListener{
	
	private final String LOG_TAG = "MainActivity";
	
	private SosServiceDBHelper dbHelper = null;
	private SosSettings sosSettings = null;
	
	EditText etStartTime;
	EditText etStopTime;
	
	EditText etHotNumber1;
	EditText etHotNumber2;
	EditText etHotSMSNumber;
	EditText etHotSMSText;
	
	View bindView;
	
	SensorManager sensorManager;
	Sensor accelerometerSensor;
	
	AlertDialog contactDualog;
	HashMap<String, String> phoneList;
	
	float[] valuesAccelGravity = new float[3];
	
	private final SimpleDateFormat dFormat= new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);                
        dbHelper =  new SosServiceDBHelper(getApplicationContext());
        ContactsHelper contactsHelper = new ContactsHelper(getApplicationContext());
        
        etHotNumber1 = (EditText)findViewById(R.id.et_sos_hot_call_number_1);
        etHotNumber2 = (EditText)findViewById(R.id.et_sos_hot_call_number_2);
        etHotSMSNumber = (EditText)findViewById(R.id.et_sos_sms_number);
        etHotSMSText = (EditText)findViewById(R.id.et_sos_sms_text);
        
        etStartTime = (EditText)findViewById(R.id.et_sos_start_time);
        etStopTime = (EditText)findViewById(R.id.et_sos_stop_time);
        
        phoneList = contactsHelper.getContactList();
    }
    
    public void onBtnStartClick(View v){    	
    	sosSettings = getSosServiceSettings();
    	dbHelper.setSettings(sosSettings);
    	
    	SosServiceAlarm sosAlarm = new SosServiceAlarm();
    	sosAlarm.setStartTime(sosSettings.gettBegin());
    	sosAlarm.setStopTime(sosSettings.gettEnd());
    	sosAlarm.setCheckPeriod(30000);
    	
    	
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
    	SosSettings settings = dbHelper.getSosServiceSettings();
    	
    	if(settings==null){
    		settings = new SosSettings();    	
    	}
    	
    	settings.settBegin(etStartTime.getText().toString());
    	settings.settEnd(etStopTime.getText().toString());
    	settings.setHotNumber1(etHotNumber1.getText().toString());
    	settings.setHotNumber2(etHotNumber2.getText().toString());
    	settings.setSmsNumber(etHotSMSNumber.getText().toString());
    	settings.setSmsText(etHotSMSText.getText().toString());
    	settings.setLastX(0);
    	settings.setLastY(0);
    	settings.setLastZ(0);
    	settings.setState("T");
    	
    	return settings;    	
    }
    
    public void onTimePickerClick(View v){
    	
    	switch (v.getId()) {
			case R.id.btn_sos_start_time:
				bindView = etStartTime;
				break;
			case R.id.btn_sos_stop_time:
				bindView = etStopTime;
    	}
    	
    	TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
    		@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    			StringBuilder timeView = new StringBuilder();
    			
    			if(hourOfDay<10){
    				timeView.append("0");
    			}
    			timeView.append(hourOfDay).append(".");
    			
    			if(minute<10){
    				timeView.append("0");
    			}
    			timeView.append(minute);
    			
    			((EditText)bindView).setText(timeView.toString());
			}
		},0,0, true);    	
    	tpd.show();
    }
    
    public void onIconClick(View v){
    	
    	switch (v.getId()) {
			case R.id.btn_sos_hot_call_number_1:
				bindView = etHotNumber1;
				break;
	    	case R.id.btn_sos_hot_call_number_2:
	    		bindView = etHotNumber2;
				break;
	    	case R.id.btn_sos_sms_number:
	    		bindView = etHotSMSNumber;
				break;
		}
    	showContactDialog();    	
    }

    
    private void showContactDialog(){
    	
    	AlertDialog.Builder contactDialogBilder = new AlertDialog.Builder(MainActivity.this);    	 
    	ListView listview = new ListView(MainActivity.this);    	
    	
    	List<String> phoneListDisplay = new ArrayList<String>(phoneList.values());    	
    	SosDialogListAdapter arrayAdapter=new SosDialogListAdapter(MainActivity.this, phoneListDisplay);
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
		String selectedNumber = (new ArrayList<String>(phoneList.keySet())).get(position);		
		((EditText)bindView).setText(selectedNumber);
		
		contactDualog.dismiss();
	}
}
