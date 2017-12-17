package com.ericbandiero.phonebook.interfaces;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * Created by Eric Bandiero on 12/17/2017.
 */

public interface IHandleClickFromRecycler {
	void handleClick(Context activityContext, View view, Object object);
}
