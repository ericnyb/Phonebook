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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * This will get the contacts data and return a list.
 * Created by Eric Bandiero on 12/15/2017.
 */

public class ContactsDao {

	final private int MY_PERMISSIONS_REQUEST_READ_CONTACTS=12345;

	List<ContactsModel> listContacts=new ArrayList<>();

	public ContactsDao() {

	}

	public List<ContactsModel> getAllContacts(final Activity context){
		/*
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
*/

		//getContactsRxJava(context).subscribe(s->showResults(s));


		//return contactsRxJava.blockingFirst();

		return allContacts(context);
	}

	private void showResults(List<ContactsModel> m){
		if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","List return from rx:"+m.size());
	}

	public Observable<List<ContactsModel>> getContactsRxJava(Activity context) {
		final String[] permissions = new String[]{Manifest.permission.READ_CONTACTS};
		if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_CONTACTS)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage("To use this app you need to allow access to your contacts.");
				builder.setTitle("Read Contacts");
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ActivityCompat.requestPermissions(context, permissions, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
					}
				});
				builder.show();
			} else {
				// if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","Requesting permissions...");
				ActivityCompat.requestPermissions(context, permissions, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
			}
		} else {
			return Observable.fromCallable(() -> {
				if (AppConstant.DEBUG)
					Log.d(this.getClass().getSimpleName() + ">", "Thread:" + Thread.currentThread().getName());
				return allContacts(context);
			}).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
		}
		return null;
	}

	/*
	 * Gets all the contacts
	 * @return A list of type {@link ContactsModel}
	 */
	private List<ContactsModel> allContacts(Context context){
		String name="";
		String phone="";
		String contactId;

		if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","Getting contacts...");
		List<ContactsModel> contactsModelList=new ArrayList<>();
		ContentResolver contentResolver = context.getContentResolver();
		Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

		while (cursor.moveToNext()) {
			name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

			//We need to check to make sure they have a phone number
			int hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

			if (hasPhone==1)
			{
				Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
				while (phones.moveToNext())
				{
					phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				}
				phones.close();
			}
			contactsModelList.add(new ContactsModel(name,phone.isEmpty()?"n/a":phone));
		}
			if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","Contacts:"+contactsModelList.toString());

		Collections.sort(contactsModelList, (ContactsModel c1, ContactsModel c2) -> c1.getContactName().toUpperCase().compareTo(c2.getContactName().toUpperCase()));

		for (ContactsModel c : contactsModelList) {
			if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">",c.getContactName());
			if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">",c.getContactPhoneNumber());

		}
		return contactsModelList;
	}

}
