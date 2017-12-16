package com.ericbandiero.phonebook.dagger;

import android.app.Application;
import android.util.Log;

import com.ericbandiero.phonebook.code.AppConstant;

/**
 * Created by Eric Bandiero on 12/16/2017.
 */

public class PhoneBookApp extends Application {
	private static PhoneBookApp app;
	private BasicComponent basicComponent;


	@Override
	public void onCreate() {
		super.onCreate();
		if (AppConstant.DEBUG) Log.d(new Object() { }.getClass().getEnclosingClass()+">","App created!");
		app = this;

		basicComponent = DaggerBasicComponent.builder()
				.appModule(new AppModule(getApplicationContext()))
				.build();
	}

	public static PhoneBookApp app() {
		return app;
	}

	public BasicComponent basicComponent() {
		return basicComponent;
	}
}
