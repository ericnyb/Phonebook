package com.ericbandiero.phonebook.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.ericbandiero.phonebook.R;
import com.ericbandiero.phonebook.Utils.UtilityPhone;
import com.ericbandiero.phonebook.adapters.Contacts_Recycler_Adapter;
import com.ericbandiero.phonebook.code.AppConstant;
import com.ericbandiero.phonebook.dagger.PhoneBookApp;
import com.ericbandiero.phonebook.fragments.MainActivityFragment;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

	public static final String EXTRA_CONTACT_NAME = "CONTACT_NAME";
	public static final String EXTRA_CONTACT_PHONE = "CONTACT_PHONE";

	private Context contextActivity;
	final private int request_code_add_contact=1;

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

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent  intent=new Intent(contextActivity,AddContactActivity.class);
				startActivityForResult(intent,request_code_add_contact);
			}
		});

		//Dagger
		PhoneBookApp.app().basicComponent().inject(this);

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

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			UtilityPhone.toastShowLong(this,getString(R.string.setting_not_yet_implemented));
			return true;
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
				MainActivityFragment mainActivityFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
				mainActivityFragment.insertNewContact(contactInfo.getStringExtra(EXTRA_CONTACT_NAME),contactInfo.getStringExtra(EXTRA_CONTACT_PHONE));
				// The user picked a contact.
				// The Intent's data Uri identifies which contact was selected.
				if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","Data name:"+contactInfo.getStringExtra(EXTRA_CONTACT_NAME));
				if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","Data phone:"+contactInfo.getStringExtra(EXTRA_CONTACT_PHONE));
				// Do something with the contact here (bigger example below)
			}
		}
	}
}
