package com.ericbandiero.phonebook.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;

import com.ericbandiero.phonebook.R;
import com.ericbandiero.phonebook.code.AppConstant;

public class AddContactActivity extends AppCompatActivity {

	private Button buttonOk;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		buttonOk = findViewById(R.id.button_act_ok);
		buttonOk.setOnClickListener((View v) -> {
			Intent intent=new Intent();
			intent.putExtra(MainActivity.CONTACT_NAME,"Testing");
			setResult(RESULT_OK,intent);
			finish();//finishing activity
		});
	}
}
