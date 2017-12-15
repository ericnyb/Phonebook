package com.ericbandiero.phonebook.models;

/**
 * Created by Eric Bandiero on 12/15/2017.
 */

public class ContactsModel {

	private String ContactName;
	private String ContactPhoneNumber;


	public ContactsModel(String contactName, String contactPhoneNumber) {
		ContactName = contactName;
		ContactPhoneNumber = contactPhoneNumber;
	}

	public String getContactName() {
		return ContactName;
	}

	public void setContactName(String contactName) {
		ContactName = contactName;
	}

	public String getContactPhoneNumber() {
		return ContactPhoneNumber;
	}

	public void setContactPhoneNumber(String contactPhoneNumber) {
		ContactPhoneNumber = contactPhoneNumber;
	}
}
