package com.ericbandiero.phonebook.activities;

import android.content.Intent;
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

public class AddContactActivity extends AppCompatActivity {

	public static final int REQUEST_CODE_FOR_ADD_CONTACT = 10023;
	private Button buttonOk;
	private EditText editTextName;
	private EditText editTextPhone;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		editTextName=findViewById(R.id.act_add_edit_name);
		editTextPhone=findViewById(R.id.act_add_edit_phone);

		buttonOk = findViewById(R.id.button_act_ok);
		buttonOk.setOnClickListener((View v) -> {

			/*
			Intent intent = new Intent();
			intent.putExtra(MainActivity.EXTRA_CONTACT_NAME, "Testing name");
			intent.putExtra(MainActivity.EXTRA_CONTACT_PHONE, "Testing phone");
			setResult(RESULT_OK, intent);
			finish();//finishing activity
			*/
			addContact();
		});
	}

	private void addContact() {
		//TODO Put in actual validation
		if (!validateName(editTextName.getText().toString()) | !validatePhone(editTextPhone.getText().toString())){
			UtilityPhone.toastShowLong(this,"Invalid data!");
			return;
		}

		// Creates a new Intent
		Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
		// Sets the MIME type to match the Contacts Provider
		intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
		intent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
		// Inserts a phone number
		intent.putExtra(ContactsContract.Intents.Insert.NAME, editTextName.getText().toString());

		String unFormattedPhone=editTextPhone.getText().toString();
		String formattedPhone;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			formattedPhone= PhoneNumberUtils.formatNumber(unFormattedPhone,"US");
		}
		else {
			//TODO USe another format routine - regex
			formattedPhone=unFormattedPhone;
		}

		intent.putExtra(ContactsContract.Intents.Insert.PHONE, editTextPhone.getText().toString());
		//We will assume user is going to enter a mobile number.
		intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
		//This is so we come back to our activity
		intent.putExtra("finishActivityOnSaveCompleted", true);
		startActivityForResult(intent, REQUEST_CODE_FOR_ADD_CONTACT);

	}
	public boolean validateName(String name){
		return true;
	}
	public boolean validatePhone(String phone){
		return true;
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
		if (requestCode==REQUEST_CODE_FOR_ADD_CONTACT){
			if (resultCode==RESULT_OK){
				if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","result code:"+resultCode);
				if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","data:"+data.getDataString());
			}
			else{
				UtilityPhone.toastShowLong(this,"User did not add contact...");
			}
		}
	}
}
