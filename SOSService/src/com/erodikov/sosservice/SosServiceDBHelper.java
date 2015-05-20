package com.erodikov.sosservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SosServiceDBHelper extends SQLiteOpenHelper{
	
	public static final String DATABASE_NAME = "SOSSERVICESETTINFS";
	public static final int DATABASE_NAME_VERSION = 2;
	
	//Tables name
	public static final String TABLE_SOS_SETTINGS = "SOSSETTINGS";
	
	//Columns names
	private static final String ID = "_id";
	private static final String CREATED_AT = "created_at";
	private static final String STATE = "state";
	private static final String TIME_BEGIN = "time_begin";
	private static final String TIME_END = "time_end";
	private static final String LAST_X = "last_x";
	private static final String LAST_Y = "last_y";
	private static final String LAST_Z = "last_z";
	private static final String SMS_PHONE_NUMBER = "sms_phone_number";
	private static final String SMS_TEXT = "sms_text";
	private static final String HOT_CALL_PHONE_NUMBER1 = "hot_call_phone_number1";
	private static final String HOT_CALL_PHONE_NUMBER2 = "hot_call_phone_number2";
	
	private static final String CREATE_TABLE_SOS_SETTINGS = "CREATE TABLE "
            + TABLE_SOS_SETTINGS + "("
			+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ STATE + " TEXT,"
			+ TIME_BEGIN + " TEXT,"
			+ TIME_END + " TEXT,"
			+ LAST_X + " INTEGER,"
			+ LAST_Y + " INTEGER,"
			+ LAST_Z + " INTEGER,"
			+ SMS_TEXT + " TEXT,"
			+ SMS_PHONE_NUMBER +" TEXT,"			
			+ HOT_CALL_PHONE_NUMBER1 +" TEXT,"
			+ HOT_CALL_PHONE_NUMBER2 +" TEXT,"
            + CREATED_AT + " DEFAULT (datetime('now','localtime'))" + ")";	
	
	public SosServiceDBHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_NAME_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_SOS_SETTINGS);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_SOS_SETTINGS);
		db.execSQL(CREATE_TABLE_SOS_SETTINGS);
	}
	
	public void closeDB(){
		SQLiteDatabase db = this.getReadableDatabase();
		if(db!=null && db.isOpen()){
			db.close();
		}
	}
	
	public long setSettings(SosSettings settings){
		long resultCode = SOSServiceConstants.RESULT_OK;
		try{
			SQLiteDatabase db = this.getWritableDatabase();
			
			ContentValues values = new ContentValues();
			values.put(STATE,settings.getState());
			values.put(TIME_BEGIN,settings.gettBegin());
			values.put(TIME_END,settings.gettEnd());
			values.put(LAST_X,settings.getLastX());
			values.put(LAST_Y,settings.getLastY());
			values.put(LAST_Z,settings.getLastZ());
			values.put(SMS_PHONE_NUMBER, settings.getSmsNumber());
			values.put(SMS_TEXT,settings.getSmsText());
			values.put(HOT_CALL_PHONE_NUMBER1,settings.getHotNumber1());
			values.put(HOT_CALL_PHONE_NUMBER2,settings.getHotNumber2());
			
			if(settings.getId()==null){
				resultCode = db.insert(TABLE_SOS_SETTINGS, null, values);
			}else {
				resultCode = db.update(TABLE_SOS_SETTINGS, values, ID+"=?", new String[]{String.valueOf(settings.getId())});	
			}			
		}catch(Exception e){
			resultCode = SOSServiceConstants.RESULT_ERROR;
		}
		return resultCode;
	}
	
	
	public SosSettings getSosServiceSettings(){
		SosSettings settings = null;
		String selectQuery = "SELECT * FROM "+TABLE_SOS_SETTINGS;
		try{
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor dbCursor = db.rawQuery(selectQuery, null);
			if(dbCursor.moveToFirst()){
				settings = new SosSettings(
					dbCursor.getString(dbCursor.getColumnIndex(ID)),
					dbCursor.getString(dbCursor.getColumnIndex(CREATED_AT)),						
					dbCursor.getString(dbCursor.getColumnIndex(STATE)),
					dbCursor.getString(dbCursor.getColumnIndex(TIME_BEGIN)),
					dbCursor.getString(dbCursor.getColumnIndex(TIME_END)),
					dbCursor.getInt(dbCursor.getColumnIndex(LAST_X)),
					dbCursor.getInt(dbCursor.getColumnIndex(LAST_Y)),
					dbCursor.getInt(dbCursor.getColumnIndex(LAST_Z)),
					dbCursor.getString(dbCursor.getColumnIndex(SMS_PHONE_NUMBER)),
					dbCursor.getString(dbCursor.getColumnIndex(SMS_TEXT)),
					dbCursor.getString(dbCursor.getColumnIndex(HOT_CALL_PHONE_NUMBER1)),
					dbCursor.getString(dbCursor.getColumnIndex(HOT_CALL_PHONE_NUMBER2)));
			}			
		}catch(Exception e){}		
		return settings;
	} 

}
