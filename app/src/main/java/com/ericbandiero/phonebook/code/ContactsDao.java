package com.ericbandiero.phonebook.code;

import com.ericbandiero.phonebook.models.ContactsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * This will get the contacts data and return a list.
 * Created by Eric Bandiero on 12/15/2017.
 */

public class ContactsDao {
	public ContactsDao() {

	}
	public List<ContactsModel> getAllContacts(){
		List<ContactsModel> contactsModelList=new ArrayList<>();
		return contactsModelList;
	}

}
