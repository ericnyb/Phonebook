package com.ericbandiero.phonebook.fragments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ericbandiero.phonebook.R;
import com.ericbandiero.phonebook.Utils.UtilityPhone;
import com.ericbandiero.phonebook.adapters.Contacts_Recycler_Adapter;
import com.ericbandiero.phonebook.code.ContactsDao;
import com.ericbandiero.phonebook.models.ContactsModel;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

	//TODO Make this a dagger injection
	ContactsDao contactsDao=new ContactsDao();


	public MainActivityFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_main, container, false);

	}

	/**
	 * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
	 * has returned, but before any saved state has been restored in to the view.
	 * This gives subclasses a chance to initialize themselves once
	 * they know their view hierarchy has been completely created.  The fragment's
	 * view hierarchy is not however attached to its parent at this point.
	 *
	 * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
	 * @param savedInstanceState If non-null, this fragment is being re-constructed
	 */
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		//Check for permissions before calling this
		RecyclerView rvDataHolderTwoFields =  getView().findViewById(R.id.rvContacts);

		List<ContactsModel> allContacts =contactsDao.getAllContacts(this.getActivity());

		RecyclerView.ItemDecoration itemDecoration = new
				DividerItemDecoration(this.getActivity(), VERTICAL);
		Contacts_Recycler_Adapter adapter=new Contacts_Recycler_Adapter(allContacts,this.getActivity());

		rvDataHolderTwoFields.setAdapter(adapter);
		rvDataHolderTwoFields.addItemDecoration( itemDecoration);
		// Set layout manager to position the items
		rvDataHolderTwoFields.setLayoutManager(new LinearLayoutManager(this.getActivity()));

		if (allContacts.isEmpty()){
			UtilityPhone.AlertMessageSimple(this.getActivity(),"No Contacts on File","You can add contacts by tapping the plus button",null);
		}

	}
}
