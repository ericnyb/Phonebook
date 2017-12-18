package com.ericbandiero.phonebook.activities;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ericbandiero.phonebook.R;
import com.ericbandiero.phonebook.Utils.UtilityPhone;
import com.ericbandiero.phonebook.code.AppConstant;
import com.ericbandiero.phonebook.dagger.PhoneBookApp;

import java.util.ArrayList;

import javax.inject.Inject;

public class AddContactActivity extends AppCompatActivity {

	public static final int REQUEST_CODE_FOR_ADD_CONTACT = 10023;
	private Button buttonAddViaContacts;
	private Button buttonAddAuto;
	private EditText editTextName;
	private EditText editTextPhone;

	@Inject
	SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);

		//Dagger
		PhoneBookApp.app().basicComponent().inject(this);



		editTextName=findViewById(R.id.act_add_edit_name);
		editTextPhone=findViewById(R.id.act_add_edit_phone);

		buttonAddViaContacts = findViewById(R.id.button_act_ok);
		buttonAddAuto = findViewById(R.id.button_act_auto_add);

		buttonAddViaContacts.setOnClickListener((View v) -> {
			addContact(false);
		});

		buttonAddAuto.setOnClickListener((View v)->{
			addContact(true);
		});

	}

	private void addContact(boolean autoAdd) {

		//TODO Put in actual validation
		if (!validateName(editTextName.getText().toString()) | !validatePhone(editTextPhone.getText().toString())){
			UtilityPhone.toastShowLong(this,getString(R.string.contact_data_not_valid));
			return;
		}

		String nameToInsert=editTextName.getText().toString();

		String unFormattedPhone=editTextPhone.getText().toString();
		String formattedPhone;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			formattedPhone= PhoneNumberUtils.formatNumber(unFormattedPhone,"US");
		}
		else {
			//TODO USe another format routine - regex
			formattedPhone=unFormattedPhone;
		}

		editTextPhone.setText(formattedPhone);

		if (autoAdd){
			writeContact(nameToInsert,formattedPhone);
		}

		else {
			// Creates a new Intent
			Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
			// Sets the MIME type to match the Contacts Provider
			intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
			// Inserts a name
			intent.putExtra(ContactsContract.Intents.Insert.NAME, nameToInsert);
			//Insert phone number
			intent.putExtra(ContactsContract.Intents.Insert.PHONE, formattedPhone);
			//We will assume user is going to enter a mobile number.
			intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
			//This is so we come back to our activity
			intent.putExtra("finishActivityOnSaveCompleted", true);
			startActivityForResult(intent, REQUEST_CODE_FOR_ADD_CONTACT);
		}
	}
	public boolean validateName(String name){
		return !name.isEmpty();
	}
	public boolean validatePhone(String phone){
		return !phone.isEmpty();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","On start...");
	}

	/**
	 * Dispatch incoming result to the correct fragment.
	 *
	 * @param requestCode Need to differentiate between requests
	 * @param resultCode  Result of action -1 is success
	 * @param data Any extras that the intent sends back
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","request code:"+requestCode);
		if (requestCode==REQUEST_CODE_FOR_ADD_CONTACT){
			if (resultCode==RESULT_OK){
				if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","result code:"+resultCode);
				if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","data:"+data.getDataString());
				Intent resultIntent=new Intent();
				resultIntent.putExtra(MainActivity.EXTRA_CONTACT_NAME, editTextName.getText().toString());
				resultIntent.putExtra(MainActivity.EXTRA_CONTACT_PHONE, editTextPhone.getText().toString());
				setResult(MainActivity.RESULT_OK, resultIntent);
				finish();
			}
			else{
				UtilityPhone.toastShowLong(this,"User did not add contact...");
			}
		}
	}

	private void writeContact(String displayName, String number) {
		ArrayList contentProviderOperations = new ArrayList();
		//insert raw contact using RawContacts.CONTENT_URI
		contentProviderOperations.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
				.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
		//insert contact display name using Data.CONTENT_URI
		contentProviderOperations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
				.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, displayName).build());
		//insert mobile number using Data.CONTENT_URI
		contentProviderOperations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
				.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number).withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());
		try {
			getApplicationContext().getContentResolver().
					applyBatch(ContactsContract.AUTHORITY, contentProviderOperations);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			e.printStackTrace();
		}
	}
}
