package com.ericbandiero.phonebook.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ericbandiero.phonebook.R;
import com.ericbandiero.phonebook.Utils.UtilityPhone;
import com.ericbandiero.phonebook.activities.MainActivity;
import com.ericbandiero.phonebook.adapters.Contacts_Recycler_Adapter;
import com.ericbandiero.phonebook.code.AppConstant;
import com.ericbandiero.phonebook.code.ContactsDao;
import com.ericbandiero.phonebook.code.HandleClickFromRecyclerContactsModel;
import com.ericbandiero.phonebook.dagger.PhoneBookApp;
import com.ericbandiero.phonebook.interfaces.ICommand;
import com.ericbandiero.phonebook.models.ContactsModel;

import java.security.Permission;
import java.security.Permissions;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

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

	private Disposable disposable;

	private final int REQUEST_CONTACTS=1;

	private ProgressBar progressBar;

	private static String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS};

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
		if (savedInstanceState!=null){
			//Restore saved data.
		}
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

		if (savedInstanceState != null) {
			//Restore the fragment's state here
		}
	}


	/**
	 *	We put code in here to fetch data.
	 *	If user leaves app and they come back they may have changed contacts data.
	 */
	@Override
	public void onResume() {
		super.onResume();
		if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","In on resume...");
		rvContactsModelView = getView().findViewById(R.id.rvContacts);
		if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_CONTACTS)
				!= PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_CONTACTS)
				!= PackageManager.PERMISSION_GRANTED){
			// Contacts permissions have not been granted.
			if (AppConstant.DEBUG)
				Log.d(this.getClass().getSimpleName() + ">", "Contact permissions has NOT been granted. Requesting permissions.");
			requestContactsPermissions();

		} else {
			// Contact permissions have been granted. Show the contacts fragment.
			if (AppConstant.DEBUG)
				Log.d(this.getClass().getSimpleName() + ">", "Contact permissions have already been granted. Displaying contact details.");
			//allContacts = contactsDao.getAllContacts(this.getActivity());
			fetchContacts();
		}

	}

	private void fetchContacts(){
		//TODO Finish action bar or add it to menu...
		progressBar=new ProgressBar(this.getContext());
		progressBar.setVisibility(View.VISIBLE);
		Observable<List<ContactsModel>> contactsRxJava = contactsDao.getContactsRxJava(this.getActivity());
		if (contactsRxJava != null) {
			contactsRxJava.subscribe(s -> setUpData(s));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//Save the fragment's state here
	}

	public void setUpData(List<ContactsModel> contactsModelsList){
		allContacts=contactsModelsList;
		RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this.getActivity(), VERTICAL);
		adapter=new Contacts_Recycler_Adapter(this.allContacts,this.getActivity(),handleClickFromRecyclerContactsModel);
		rvContactsModelView.setAdapter(adapter);
		rvContactsModelView.addItemDecoration( itemDecoration);
		// Set layout manager to position the items
		rvContactsModelView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
		if (progressBar!=null){
			progressBar.setVisibility(View.INVISIBLE);
		}
		if (this.allContacts.isEmpty()){
			UtilityPhone.AlertMessageSimple(this.getActivity(),"No Contacts on File","You can add contacts by tapping the plus button",null);
		}
		//disposable.dispose();
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

	private void requestContactsPermissions() {
		// BEGIN_INCLUDE(contacts_permission_request)
		if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
				Manifest.permission.READ_CONTACTS)||ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
				Manifest.permission.WRITE_CONTACTS)) {

			// Provide an additional rationale to the user if the permission was not granted
			// and the user would benefit from additional context for the use of the permission.
			// For example, if the request has been denied previously.
			if (AppConstant.DEBUG)
				Log.d(this.getClass().getSimpleName() + ">", "Displaying contacts permission rationale to provide additional context.");

			UtilityPhone.AlertMessageSimple(this.getContext(), "Contacts Permission Needed", getString( R.string.permission_contacts_rationale), new ICommand() {
				@Override
				public void execute() {
					requestPermissions(PERMISSIONS_CONTACT, REQUEST_CONTACTS);
				}
			});
		} else {
			if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","Will request permissions...");
			// Contact permissions have not been granted yet. Request them directly.
			requestPermissions(PERMISSIONS_CONTACT, REQUEST_CONTACTS);
		}
	}
	/**
	 * Callback for the result from requesting permissions. This method
	 * is invoked for every call on {@link #requestPermissions(String[], int)}.
	 * <p>
	 * <strong>Note:</strong> It is possible that the permissions request interaction
	 * with the user is interrupted. In this case you will receive empty permissions
	 * and results arrays which should be treated as a cancellation.
	 * </p>
	 *
	 * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
	 * @param permissions  The requested permissions. Never null.
	 * @param grantResults The grant results for the corresponding permissions
	 *                     which is either {@link PackageManager#PERMISSION_GRANTED}
	 *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
	 * @see #requestPermissions(String[], int)
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","Permission results from fragment...");
		if (requestCode==REQUEST_CONTACTS){
			if(grantResults[0]== PackageManager.PERMISSION_GRANTED & grantResults[1]==PackageManager.PERMISSION_GRANTED){
				if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","Permission granted!");
				fetchContacts();
			}
			else{
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
			}
		}
	}
}
