package com.ericbandiero.phonebook.code;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.ericbandiero.phonebook.dagger.PhoneBookApp;
import com.ericbandiero.phonebook.interfaces.IHandleClickFromRecycler;
import com.ericbandiero.phonebook.models.ContactsModel;

/**
 * Created by Eric Bandiero on 12/17/2017.
 */

public class HandleClickFromRecyclerContactsModel implements IHandleClickFromRecycler {
	public HandleClickFromRecyclerContactsModel() {
		//Dagger
		PhoneBookApp.app().basicComponent().inject(this);
	}

	@Override
	public void handleClick(Context context, View view, Object object) {
		ContactsModel contactsModel = (ContactsModel)object;
		if (AppConstant.DEBUG)
			Log.d(this.getClass().getSimpleName() + ">", "Need to implement!");
		Intent callIntent = new Intent(Intent.ACTION_DIAL);
		String phoneNumber = contactsModel.getContactPhoneNumber();
		callIntent.setData(Uri.parse("tel:" + phoneNumber));
		context.startActivity(callIntent);
	}
}
