package com.ericbandiero.phonebook.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ericbandiero.phonebook.R;
import com.ericbandiero.phonebook.Utils.UtilityPhone;
import com.ericbandiero.phonebook.adapters.Contacts_Recycler_Adapter;
import com.ericbandiero.phonebook.code.AppConstant;
import com.ericbandiero.phonebook.code.ContactsDao;
import com.ericbandiero.phonebook.code.HandleClickFromRecyclerContactsModel;
import com.ericbandiero.phonebook.dagger.PhoneBookApp;
import com.ericbandiero.phonebook.models.ContactsModel;

import java.util.List;

import javax.inject.Inject;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

	//TODO Make this a dagger injection

	@Inject
	ContactsDao contactsDao;

	@Inject
	HandleClickFromRecyclerContactsModel handleClickFromRecyclerContactsModel;

	private TextView textViewHeader;

	private List<ContactsModel> allContacts;

	private Contacts_Recycler_Adapter adapter;

	private RecyclerView rvContactsModelView;

	public MainActivityFragment() {

	}

	/**
	 * Called when a fragment is first attached to its context.
	 * {@link #onCreate(Bundle)} will be called after this.
	 *
	 * @param context
	 */
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		//Dagger
		PhoneBookApp.app().basicComponent().inject(this);
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


	}

	/**
	 * Called when the fragment's activity has been created and this
	 * fragment's view hierarchy instantiated.  It can be used to do final
	 * initialization once these pieces are in place, such as retrieving
	 * views or restoring state.  It is also useful for fragments that use
	 * {@link #setRetainInstance(boolean)} to retain their instance,
	 * as this callback tells the fragment when it is fully associated with
	 * the new activity instance.  This is called after {@link #onCreateView}
	 * and before {@link #onViewStateRestored(Bundle)}.
	 *
	 * @param savedInstanceState If the fragment is being re-created from
	 *                           a previous saved state, this is the state.
	 */
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		rvContactsModelView =  getView().findViewById(R.id.rvContacts);
		allContacts = contactsDao.getAllContacts(this.getActivity());
		RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this.getActivity(), VERTICAL);
		adapter=new Contacts_Recycler_Adapter(allContacts,this.getActivity(),handleClickFromRecyclerContactsModel);
		rvContactsModelView.setAdapter(adapter);
		rvContactsModelView.addItemDecoration( itemDecoration);
		// Set layout manager to position the items
		rvContactsModelView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
		if (allContacts.isEmpty()){
			UtilityPhone.AlertMessageSimple(this.getActivity(),"No Contacts on File","You can add contacts by tapping the plus button",null);
		}

	}

	public void insertNewContact(String name, String phoneNumber){
		allContacts.add(new ContactsModel(name,phoneNumber));
		adapter.notifyDataSetChanged();
	}

	public void GotoBottomOfList(){
		if(!allContacts.isEmpty()) {
			rvContactsModelView.scrollToPosition(allContacts.size() - 1);
		}
	}

	public void gotoTopOfList(){
		if (allContacts.size()>0) {
			rvContactsModelView.scrollToPosition(0);
		}
	}
}
