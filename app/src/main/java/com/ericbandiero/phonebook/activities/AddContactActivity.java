package com.ericbandiero.phonebook.activities;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ericbandiero.phonebook.R;

public class AddContactActivity extends AppCompatActivity {

	private Button buttonOk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
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
		// Creates a new Intent to insert a contact
		Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
// Sets the MIME type to match the Contacts Provider
		intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

		// EditText mEmailAddress = (EditText) findViewById(R.id.email);
		//private EditText mPhoneNumber = (EditText) findViewById(R.id.phone);

/*
 * Inserts new data into the Intent. This data is passed to the
 * contacts app's Insert screen
 */

/*
 * In this example, sets the email type to be a work email.
 * You can set other email types as necessary.
 */
		intent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
		// Inserts a phone number
		intent.putExtra(ContactsContract.Intents.Insert.NAME, "Eric B");
		intent.putExtra(ContactsContract.Intents.Insert.PHONE, "+12127692422");
/*
 * In this example, sets the phone type to be a work phone.
 * You can set other phone types as necessary.
 */
		intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);

		startActivity(intent);

	}
}
