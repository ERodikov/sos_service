package com.erodikov.sosservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.erodikov.sosservice.bo.PhoneView;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.SparseArray;

public class ContactsHelper {	
	
	private ContentResolver contentResolver;
	private SparseArray<String> phoneTypes;
	
	public ContactsHelper(Context context){		
		contentResolver = context.getContentResolver();
		phoneTypes = new SparseArray<String>();
		phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_HOME, context.getResources().getString(R.string.home));
		phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE, context.getResources().getString(R.string.mobile));
		phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_WORK, context.getResources().getString(R.string.work));		
	}
	
	public HashMap<String,PhoneView> getContactList(){		
		HashMap<String,PhoneView> phoneList = new HashMap<String, PhoneView>();
		Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		
		if(cursor!=null && cursor.getCount()>0){
			String contactId = null;
			String contactName = null;
			
			while(cursor.moveToNext()){
				contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
				contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));			
				
				int hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				if(hasPhoneNumber>0){
					List<PhoneView> phoneViewList = getPhoneListByContactId(contactId,contactName);
					if(phoneViewList!=null){
						for (PhoneView viewItem : phoneViewList) {							
							phoneList.put(viewItem.getNumber(), viewItem);
						}
					}
				}				
			}
		}
		return phoneList;
	}
	
	private List<PhoneView> getPhoneListByContactId(String contactId, String contactName){
		List<PhoneView> phoneList = null;
		
		Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = ?", new String[]{contactId}, null);
		if(phoneCursor!=null && phoneCursor.getCount()>0){
			phoneList = new ArrayList<PhoneView>();
			PhoneView pView = null;
			while(phoneCursor.moveToNext()){				
				pView = new PhoneView();
				pView.setType(phoneTypes.get(phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))));
				pView.setNumber(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
				pView.setName(contactName);
				phoneList.add(pView);
			}
		}		
		return phoneList;		
	}
	

	
	
}
