package com.ericbandiero.phonebook.Utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Eric Bandiero on 12/15/2017.
 */

public class PhoneUtility {
	public static void toastShowLong(Context context,String string){
		Toast toast=Toast.makeText(context,string,Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}
