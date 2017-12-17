package com.ericbandiero.phonebook.dagger;

import com.ericbandiero.phonebook.activities.AddContactActivity;
import com.ericbandiero.phonebook.activities.MainActivity;
import com.ericbandiero.phonebook.fragments.MainActivityFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Eric Bandiero on 12/16/2017.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface BasicComponent {
	void inject(AddContactActivity addContactActivity);
	void inject(MainActivity mainActivity);
	void inject (MainActivityFragment mainActivityFragment);
}
