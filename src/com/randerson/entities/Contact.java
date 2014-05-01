package com.randerson.entities;

import libs.RegExManager;

public class Contact {

	private String FIRST_NAME;
	private String LAST_NAME;
	private String[] PHONE_NUMBERS = new String[2];
	private String ADDRESS;
	private String EMAIL_ADDRESS;
	
	public final int MOBILE_NUMBER = 0;
	public final int HOME_NUMBER = 1;
	
	public Contact(String firstName, String lastName, String phoneNumber, String address, String emailAddress)
	{
		// set the contact fields
		FIRST_NAME = firstName;
		LAST_NAME = lastName;
		ADDRESS = address;
		PHONE_NUMBERS[0] = phoneNumber;
		EMAIL_ADDRESS = emailAddress;
	}
	
	
	// contact setter methods
	
	public void setFirstName(String name)
	{
		FIRST_NAME = name;
	}
	
	public void setLastName(String name)
	{
		LAST_NAME = name;
	}
	
	public void setAddress(String address)
	{
		ADDRESS = address;
	}
	
	public void setPhoneNumber(String number, int index)
	{
		// check if the number position is in valid range
		if (index < PHONE_NUMBERS.length)
		{
			PHONE_NUMBERS[index] = number;
		}
	}
	
	public void setEmail(String email)
	{
		if (RegExManager.checkPattern(email, RegExManager.EMAIL_PATTERN))
		{
			EMAIL_ADDRESS = email;
		}
		else
		{
			
		}
	}
	
	
	// contact getter methods
	
	public String getPhoneNumber(int number)
	{
		return PHONE_NUMBERS[number];
	}
	
	public String getAddress()
	{
		return ADDRESS;
	}
	
	public String getFirstName()
	{
		return FIRST_NAME;
	}
	
	public String getLastName()
	{
		return LAST_NAME;
	}
	
	public String getFullName()
	{
		return FIRST_NAME + " " + LAST_NAME;
	}
	
	public String getEmail()
	{
		return EMAIL_ADDRESS;
	}
}
