package com.ericbandiero.phonebook.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ericbandiero.phonebook.R;
import com.ericbandiero.phonebook.Utils.UtilityPhone;
import com.ericbandiero.phonebook.code.AppConstant;
import com.ericbandiero.phonebook.dagger.PhoneBookApp;
import com.ericbandiero.phonebook.fragments.MainActivityFragment;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

	//Contacts
	public static final String EXTRA_CONTACT_NAME = "CONTACT_NAME";
	public static final String EXTRA_CONTACT_PHONE = "CONTACT_PHONE";
	final private int REQUEST_CONTACTS=1;
	final private int request_code_add_contact=1;

	//For saving a variable
	final private String SAVE_TEST_VARIABLE="save_test";
	private String savedString;

	//Activity context
	private Context contextActivity;

	private MainActivityFragment mainActivityFragment;

	@Inject
	SharedPreferences sharedPreferences;

	@Inject
	Context contextApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		contextActivity=this;
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		setTitle("");
 		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent  intent=new Intent(contextActivity,AddContactActivity.class);
				startActivityForResult(intent,request_code_add_contact);
			}
		});

		//Dagger
		PhoneBookApp.app().basicComponent().inject(this);

		mainActivityFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		String title=item.getTitle().toString();
		if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","Menu tile:"+title);
		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			UtilityPhone.toastShowLong(this,getString(R.string.setting_not_yet_implemented));
			return true;
		}

		if (title.equals(getString(R.string.menu_goto_bottom))){
			mainActivityFragment.GotoBottomOfList();
		}

		if (title.equals(getString(R.string.menu_goto_top))){
			mainActivityFragment.gotoTopOfList();
		}

		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent contactInfo) {
		if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","Result code:"+resultCode);
		// Check which request we're responding to
		if (requestCode == request_code_add_contact) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {

				mainActivityFragment.insertNewContact(contactInfo.getStringExtra(EXTRA_CONTACT_NAME),contactInfo.getStringExtra(EXTRA_CONTACT_PHONE));
				// The user picked a contact.
				// The Intent's data Uri identifies which contact was selected.
				if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","Data name:"+contactInfo.getStringExtra(EXTRA_CONTACT_NAME));
				if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","Data phone:"+contactInfo.getStringExtra(EXTRA_CONTACT_PHONE));
				// Do something with the contact here (bigger example below)
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
		super.onSaveInstanceState(outState, outPersistentState);
		savedString="A sample saved variable";
		outState.putString(SAVE_TEST_VARIABLE,savedString);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		savedString=savedInstanceState.getString(SAVE_TEST_VARIABLE);
	}
}
