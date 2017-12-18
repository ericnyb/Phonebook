package com.ericbandiero.phonebook.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.ericbandiero.phonebook.R;
import com.ericbandiero.phonebook.code.AppConstant;
import com.ericbandiero.phonebook.interfaces.ICommand;

/**
 * Created by Eric Bandiero on 12/15/2017.
 */

public class UtilityPhone {
	public static void toastShowLong(Context context,String string){
		toastIt(context,string,Toast.LENGTH_LONG);
	}

	public static void toastShowShort(Context context,String string){
		toastIt(context,string,Toast.LENGTH_SHORT);
	}

	private static void toastIt(Context context,String string,int toastDuration){
		Toast toast=Toast.makeText(context,string,toastDuration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static void askForContactsReadPermission(final Activity activity){
		final int MY_PERMISSIONS_REQUEST_FINE_LOCATION=12345;
		if (AppConstant.DEBUG) Log.d(new Object() { }.getClass().getEnclosingClass()+">","Asking for permissions...");
		//Check for permissions
		final String[] permissions = new String[]{Manifest.permission.READ_CONTACTS};
		if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setMessage("To use this app you need to allow access to your contacts.");
				builder.setTitle("Read Contacts");
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ActivityCompat.requestPermissions(activity, permissions,MY_PERMISSIONS_REQUEST_FINE_LOCATION );
					}
				});
				builder.show();
			} else {
				// if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","Requesting permissions...");
				ActivityCompat.requestPermissions(activity, permissions, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
			}
		}
	}

	public static void AlertMessageSimple(Context context, String title, String message, final ICommand command){

		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int which) {
				if (command!=null){
					command.execute();
				}
				// TODO Add your code for the button here.
			}
		});
		// Set the Icon for the Dialog
		alertDialog.setIcon(R.drawable.ic_alert_2);
		alertDialog.show();
	}

	public static final boolean checkNameIsValid(String name){
		if (name.isEmpty()){
			return false;
		}
		else{
			return true;
		}
	}

	public static final boolean checkPhoneIsValid(String phone) {
		if (phone.isEmpty()) {
			return false;
		}

		int len = phone.length();
		for (int i = 0; i < len; i++) {
			if (!PhoneNumberUtils.isDialable(phone.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
