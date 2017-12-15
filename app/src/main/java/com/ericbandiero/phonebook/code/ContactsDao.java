package com.ericbandiero.phonebook.code;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.ericbandiero.phonebook.models.ContactsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * This will get the contacts data and return a list.
 * Created by Eric Bandiero on 12/15/2017.
 */

public class ContactsDao {

	final private int MY_PERMISSIONS_REQUEST_READ_CONTACTS=12345;

	public ContactsDao() {

	}

	public List<ContactsModel> getAllContacts(final Activity context){
		final String[] permissions = new String[]{Manifest.permission.READ_CONTACTS};
		if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_CONTACTS)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage("To use this app you need to allow access to your contacts.");
				builder.setTitle("Read Contacts");
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ActivityCompat.requestPermissions(context, permissions,MY_PERMISSIONS_REQUEST_READ_CONTACTS );
					}
				});
				builder.show();
			} else {
				// if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","Requesting permissions...");
				ActivityCompat.requestPermissions(context, permissions, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
				return null;
			}
		}
		return allContacts(context);
	}

	/*
	 * Gets all the contacts
	 * @return A list of type {@link ContactsModel}
	 */
	private List<ContactsModel> allContacts(Context context){
		if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","Getting contacts...");
		List<ContactsModel> contactsModelList=new ArrayList<>();
		ContentResolver contentResolver = context.getContentResolver();
		Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
		if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","Cursor count:"+cursor.getCount());
		return contactsModelList;
	}


	public void insertContact(){
		
	}


}
