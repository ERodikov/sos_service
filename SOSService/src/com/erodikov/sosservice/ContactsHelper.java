package com.erodikov.sosservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

public class ContactsHelper {	
	
	private ContentResolver contentResolver;
	private HashMap<Integer, String> phoneTypes;
	
	public ContactsHelper(Context context){		
		contentResolver = context.getContentResolver();
		phoneTypes = new HashMap<Integer, String>();
		phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_HOME, context.getResources().getString(R.string.home));
		phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE, context.getResources().getString(R.string.mobile));
		phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_WORK, context.getResources().getString(R.string.work));		
	}
	
	public List<String> getContactList(){		
		List<String> phoneList = new ArrayList<String>();
		Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		
		if(cursor!=null && cursor.getCount()>0){
			String contactId = null;
			String contactName = null;
			
			while(cursor.moveToNext()){
				contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
				contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));			
				
				int hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				if(hasPhoneNumber>0){
					List<String> phones = getPhoneListByContactId(contactId);
					if(phones!=null){
						for (String phone : phones) {
							phoneList.add(contactName+": "+phone);
						}
					}
				}				
			}
		}
		return phoneList;
	}
	
	private List<String> getPhoneListByContactId(String contactId){
		List<String> phoneList = null;
		
		Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = ?", new String[]{contactId}, null);
		if(phoneCursor!=null && phoneCursor.getCount()>0){
			phoneList = new ArrayList<String>();
			
			Integer phoneType = null;
			StringBuilder phoneBuilde = null;
			
			while(phoneCursor.moveToNext()){
				phoneBuilde = new StringBuilder();
				
				phoneType = phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
				String typeDescription = null;
				if((typeDescription=phoneTypes.get(phoneType))!=null){
					phoneBuilde.append(typeDescription).append(" ");
				}
				phoneBuilde.append(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
				phoneList.add(phoneBuilde.toString());
			}
		}		
		return phoneList;		
	}	
}
