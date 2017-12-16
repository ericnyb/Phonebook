package com.ericbandiero.phonebook.dagger;

import com.ericbandiero.phonebook.activities.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Eric Bandiero on 12/16/2017.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface BasicComponent {
	void inject(MainActivity mainActivity);
}
