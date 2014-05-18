package com.randerson.fragments;

import libs.ApplicationDefaults;
import libs.UniArray;

import com.randerson.activities.AddContactActivity;
import com.randerson.activities.ViewContactsActivity;
import com.randerson.hidn.R;
import com.randerson.interfaces.Constants;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.interfaces.ViewHandler;
import com.randerson.support.DataManager;
import com.randerson.support.ThemeMaster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

@SuppressLint("DefaultLocale")
public class ContactsActivity extends android.support.v4.app.Fragment implements FragmentSetup, Constants {

	public static final String TITLE = "Contacts";
	public ViewHandler parentView;
	public String theme;
	public String themeB;
	public boolean defaultNavType;
	public View root;
	public String[] contactNames;
	public String[] contactIds;
	public DataManager dataManager;
	public String[] contactPaths;
	ArrayAdapter<String> adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		root = inflater.inflate(R.layout.activity_contacts, container, false);
		
		// turns on options menu in fragment
		setHasOptionsMenu(true);
		
		// load the application settings
		loadApplicationSettings();
		
		if (parentView != null && parentView.hasValidPin())
		{
			
			dataManager = new DataManager(getActivity());
			
			if (dataManager != null)
			{
				// get the contact object of data
				UniArray contacts = (UniArray) dataManager.load(DataManager.CONTACT_DATA);
				
				if (contacts !=  null)
				{
					// get the list of contact keys
					contactIds = contacts.getAllObjectKeys();
					
					// init the contactNames array
					contactNames = new String[contactIds.length];
					
					for (int x = 0; x < contactIds.length; x++)
					{
						// get the individual contact from the complete contacts object
						UniArray contact = (UniArray) contacts.getObject(contactIds[x]);
						
						if (contact != null)
						{
							// set the actual contact name
							String fName = contact.getString("firstName");
							String lName = contact.getString("lastName");
							
							// set the contact name to display
							contactNames[x] = (fName + " " + lName);
						}
					}

				}
			}
			
			// create the listview from ref file
			ListView contactsList = (ListView) root.findViewById(R.id.contactsList);
			
			if (contactsList != null && contactNames != null)
			{
				// create a new adapter
				adapter = new ArrayAdapter<String>(getActivity(), R.layout.contact_list_item, R.id.contactListItem, contactNames);
				
				// check if the adapter is valid
				if (adapter != null)
				{
					contactsList.setAdapter(adapter);
				}
				
				// setup the single click listeners
				contactsList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id)
					{
						
						// create the intent to launch the detail view activity and the bundle for passing
						// the activity details upon loading
						Intent detailView = new Intent(getActivity(), ViewContactsActivity.class);
						
						// verify the intent is valid, if so pass in the args and load it up
						if (detailView != null)
						{
							// get the key to retrieve the contact data at the given index
							String contactKey = contactIds[position];
							
							// retrieve the contact data array
							UniArray contacts = (UniArray) dataManager.load(DataManager.CONTACT_DATA);
							
							if (contacts !=  null)
							{
								// retrieve the contact data for the contactKey
								UniArray contactItem = (UniArray) contacts.getObject(contactKey);
								
								if (contactItem != null)
								{
									String fName = "";
									String lName = "";
									String address = "";
									String email = "";
									String primaryPhone = "";
									String secondaryPhone = "";
									
									// set the string to corresponding data
									fName = contactItem.getString("firstName");
									lName = contactItem.getString("lastName");
									address = contactItem.getString("address");
									email = contactItem.getString("email");
									primaryPhone = contactItem.getString("primaryPhone");
									secondaryPhone = contactItem.getString("secondaryPhone");
									
									// make the data available to the intent
									detailView.putExtra("firstName", fName);
									detailView.putExtra("lastName", lName);
									detailView.putExtra("address", address);
									detailView.putExtra("email", email);
									detailView.putExtra("primaryPhone", primaryPhone);
									detailView.putExtra("secondaryPhone", secondaryPhone);
									
								}

							}
							
							// disable the passLock
							parentView.setDisablePassLock(true);
							
							// start the activity
							startActivity(detailView);
						}
					}
				});
				
				// setup the long click listener
				contactsList.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id)
					{
						
						switch(position)
						{
							case 0:
								break;
							
								default:
									break;
						}
						
						// returns false when no click event is consumed
						return false;
					}
				});
			}
		}
		
		return root;
	}

	@Override
	public void setupActionBar() {
		
		int color = ThemeMaster.getThemeId(theme);
		
		// set the actionBar styling
		getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(color));
		
		if (defaultNavType == false)
		{	
			// set the title to appear for the drawerlist view
			getActivity().getActionBar().setTitle(TITLE);
		}
		else if (defaultNavType)
		{
			// remove the title for pagerView
			getActivity().getActionBar().setTitle("");
		}
		
		int themeBId = ThemeMaster.getThemeId(themeB.toLowerCase());
		
		// set the background styling
		LinearLayout layoutBg = (LinearLayout) root.findViewById(R.id.contactsBg);
		
		// verify the view is valid first
		if (layoutBg != null)
		{
			layoutBg.setBackground(getResources().getDrawable(themeBId));
		}
	}

	@Override
	public void loadApplicationSettings()
	{
		// create a application defaults object to load app settings
		ApplicationDefaults defaults = new ApplicationDefaults(getActivity());
		
		if (defaults != null)
		{
			defaultNavType = defaults.getData().getBoolean("defaultNavType", true);
			theme = defaults.getData().getString("theme", "4_3");
			themeB = defaults.getData().getString("themeB", "Dark");
		}
		
		// method for setting the actionBar
		setupActionBar();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.contacts_menu, menu);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		if (activity instanceof ViewHandler)
		{
			parentView = (ViewHandler) activity;
			
			if (parentView != null)
			{
				// pass a reference to this fragment to the parent activity
				parentView.passFragmentToParent(this, CONTACTS);
			}
		}
	}

	@Override
	public void onActionBarItemClicked(int itemId)
	{
		// verify the id matches and the pin was valid
		if (itemId == R.id.contact_new_contact && parentView.hasValidPin())
		{
			Intent addContact = new Intent(getActivity(), AddContactActivity.class);
			
			if (addContact != null)
			{
				startActivityForResult(addContact, 100);
			}
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 100 && resultCode == Activity.RESULT_OK)
		{
			
			// update the list string array resource
			if (dataManager != null)
			{
				// get the contact object of data
				UniArray contacts = (UniArray) dataManager.load(DataManager.CONTACT_DATA);
				
				if (contacts !=  null)
				{
					// get the list of contact keys
					contactIds = contacts.getAllObjectKeys();
					
					// init the contactNames array
					contactNames = new String[contactIds.length];
					
					for (int x = 0; x < contactIds.length; x++)
					{
						// get the individual contact from the complete contacts object
						UniArray contact = (UniArray) contacts.getObject(contactIds[x]);
						
						if (contact != null)
						{
							// set the actual contact name
							String fName = contact.getString("firstName");
							String lName = contact.getString("lastName");
							
							// set the contact name to display
							contactNames[x] = (fName + " " + lName);
						}
					}
					
					adapter.notifyDataSetChanged();

				}
			}
			
		}
	}
	
}
