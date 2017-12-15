package com.ericbandiero.phonebook.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


import com.ericbandiero.phonebook.R;
import com.ericbandiero.phonebook.code.AppConstant;
import com.ericbandiero.phonebook.models.ContactsModel;

import java.util.List;

/**
 * Created by Eric Bandiero on 12/13/2017.
 */

public class Contacts_Recycler_Adapter extends RecyclerView.Adapter<Contacts_Recycler_Adapter.ViewHolder>  {

	private int maxLengthOfDataField;
	private int maxLengthOfDataValue;
	private boolean performTotalOnValues;
	private int colorTextField;
	private int colorTextValue;
	private Context context;
	private int totalBackColorTotalField = Color.WHITE;
	private int totalBackColorTotalValue = Color.WHITE;
	private List<ContactsModel> contactsModelList;
	// Store the context for easy access
	private Context mContext;

	public Contacts_Recycler_Adapter(List<ContactsModel> contactsModelList, Context mContext) {
		this.contactsModelList = contactsModelList;
		//TODO Do we need the context
		this.mContext = mContext;
		setUpData();
	}

	private void setUpData(){

	}
	/**
	 * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
	 * an item.
	 * <p>
	 * This new ViewHolder should be constructed with a new View that can represent the items
	 * of the given type. You can either create a new View manually or inflate it from an XML
	 * layout file.
	 * <p>
	 * The new ViewHolder will be used to display items of the adapter using
	 * . Since it will be re-used to display
	 * different items in the data set, it is a good idea to cache references to sub views of
	 * the View to avoid unnecessary {@link View#findViewById(int)} calls.
	 *
	 * @param parent   The ViewGroup into which the new View will be added after it is bound to
	 *                 an adapter position.
	 * @param viewType The view type of the new View.
	 * @return A new ViewHolder that holds a View of the given view type.
	 * @see #getItemViewType(int)
	 * @see #onBindViewHolder(ViewHolder, int)
	 */
	@Override
	public Contacts_Recycler_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);

		// Inflate the custom layout
		View statsGridItemsView = inflater.inflate(R.layout.recyler_row_items, parent, false);
		// Return a new holder instance
		return new ViewHolder(statsGridItemsView);
	}

	/**
	 * Called by RecyclerView to display the data at the specified position. This method should
	 * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
	 * position.
	 * <p>
	 * Note that unlike {@link ListView}, RecyclerView will not call this method
	 * again if the position of the item changes in the data set unless the item itself is
	 * invalidated or the new position cannot be determined. For this reason, you should only
	 * use the <code>position</code> parameter while acquiring the related data item inside
	 * this method and should not keep a copy of it. If you need the position of an item later
	 * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
	 * have the updated adapter position.
	 * <p>
	 * Override  instead if Adapter can
	 * handle efficient partial bind.
	 *
	 * @param viewHolder   The ViewHolder which should be updated to represent the contents of the
	 *                 item at the given position in the data set.
	 * @param position The position of the item within the adapter's data set.
	 */
	@Override
	public void onBindViewHolder(Contacts_Recycler_Adapter.ViewHolder viewHolder, int position) {
		ContactsModel contactsModel = contactsModelList.get(position);
		TextView textViewFieldName = viewHolder.holderTextViewName;
		TextView textViewValuePhone = viewHolder.holderTextViewPhone;

		String contactName=contactsModel.getContactName();
		String contactPhone=(contactsModel.getContactNumber().isEmpty()?"N/A":contactsModel.getContactNumber());
		textViewFieldName.setText("Name:"+contactName);
		textViewValuePhone.setText("Phone:"+contactPhone);
	}

	/**
	 * Returns the total number of items in the data set held by the adapter.
	 *
	 * @return The total number of items in this adapter.
	 */
	@Override
	public int getItemCount() {
		return contactsModelList.size();
	}

	// Easy access to the context object in the recyclerView
	private Context getContext() {
		return mContext;
	}

	public void setColorTextField(int colorTextField) {
		this.colorTextField = colorTextField;
	}

	public void setColorTextValue(int colorTextValue) {
		this.colorTextValue = colorTextValue;
	}

	public void setTotalBackColorTotalField(int totalBackColorTotalField) {
		this.totalBackColorTotalField = totalBackColorTotalField;
	}

	public void setTotalBackColorTotalValue(int totalBackColorTotalValue) {
		this.totalBackColorTotalValue = totalBackColorTotalValue;
	}
	public void setPerformTotalOnValues(boolean performTotalOnValues) {
		this.performTotalOnValues = performTotalOnValues;
	}

	// Provide a direct reference to each of the views within a data item
// Used to cache the views within the item layout for fast access
public class ViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
	// Your holder should contain a member variable
	// for any view that will be set as you render a row
	public TextView holderTextViewName;
	public TextView holderTextViewPhone;

	// We also create a constructor that accepts the entire item row
	// and does the view lookups to find each subview
	public ViewHolder(View itemView) {
		// Stores the itemView in a public final member variable that can be used
		// to access the context from any ViewHolder instance.
		super(itemView);
		itemView.setOnClickListener(this);
		holderTextViewName = itemView.findViewById(R.id.row_textViewName);
		holderTextViewPhone = itemView.findViewById(R.id.row_textViewPhone);
	}

		@Override
		public void onClick(View view) {
			ContactsModel contactsModel = contactsModelList.get(getAdapterPosition());
			//TODO Dial the number for this contact
			if (AppConstant.DEBUG) Log.d(this.getClass().getSimpleName()+">","Need to implement!");
		}
	}
}


