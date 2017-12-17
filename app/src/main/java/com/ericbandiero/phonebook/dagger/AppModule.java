package com.ericbandiero.phonebook.dagger;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ericbandiero.phonebook.code.ContactsDao;
import com.ericbandiero.phonebook.code.HandleClickFromRecyclerContactsModel;
import com.ericbandiero.phonebook.models.ContactsModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Eric Bandiero on 12/16/2017.
 */
@Module
public class AppModule {
	private Context context;


	public AppModule(Context context) {
		this.context = context;
	}

	@Singleton
	@Provides
	public Context provideContext(){
		return context;
	}

	@Singleton
	@Provides
	public SharedPreferences provideSharedPreferences(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	@Provides
	public ContactsDao providesContactModel(){
		return new ContactsDao();
	}

	@Singleton
	@Provides
	public HandleClickFromRecyclerContactsModel providesHandleClickFromRecyclerContactsModel(){
		return new HandleClickFromRecyclerContactsModel();
	}
}
