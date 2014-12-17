package com.randerson.fragments;

import libs.ApplicationDefaults;
import libs.UniArray;

import com.randerson.activities.AddContactActivity;
import com.randerson.activities.DrawerFragmentActivity;
import com.randerson.activities.PagerFragmentActivity;
import com.randerson.activities.ViewContactsActivity;
import com.randerson.hidn.R;
import com.randerson.interfaces.Constants;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.interfaces.Refresher;
import com.randerson.interfaces.ViewHandler;
import com.randerson.support.ActionManager;
import com.randerson.support.DataManager;
import com.randerson.support.ListViewAdapter;
import com.randerson.support.ThemeMaster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class ContactsActivity extends android.support.v4.app.Fragment implements FragmentSetup, Constants, Refresher {

	public static final String TITLE = "Contacts";
	public ViewHandler parentView;
	public String theme;
	public String themeB;
	public boolean defaultNavType;
	public boolean privateMode = false;
	public View root;
	public String[] contactNames;
	public String[] contactIds;
	public DataManager dataManager;
	public String[] contactPaths;
	ListViewAdapter adapter;
	ListView contactsList;
	public boolean isDeleting = false;
	
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
			contactsList = (ListView) root.findViewById(R.id.contactsList);
			
			if (contactsList != null && contactNames != null)
			{
				// create a new adapter
				adapter = new ListViewAdapter(getActivity(), R.layout.contact_list_item, R.id.contactListItem, contactNames);
				
				// check if the adapter is valid
				if (adapter != null)
				{
					contactsList.setAdapter(adapter);
				}
				
				// set the drawable for the listView bg
				int color = ThemeMaster.getThemeId(theme)[2];
				contactsList.setBackgroundColor(color);
				
				// setup the single click listeners
				contactsList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id)
					{
						
						if (isDeleting == false)
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
										
										detailView.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
										
									}

								}
								
								// disable the passLock
								parentView.setDisablePassLock(true);
								
								// start the activity
								startActivity(detailView);
							}
						}
						else 
						{
							// get the textView
							TextView textView = (TextView) view.findViewById(R.id.contactListItem);
							
							if (textView != null)
							{
								
								// check if the backgrounds match as being selected
								if (textView.isSelected() == true)
								{
									// set the view as unselected
									textView.setSelected(false);
									
									// restore the color
									ActionManager.restoreBgColor(getActivity(), textView, position);
								}
								else if (textView.isSelected() == false)
								{
									// set the view as selected
									textView.setSelected(true);
									
									// set the view to be highlighted
									ActionManager.setHighlighted(getActivity(), textView);
								}
							}
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
		
		int color = ThemeMaster.getThemeId(theme)[0];
		
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
		
		int themeBId = ThemeMaster.getThemeId(themeB.toLowerCase())[0];
		
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
			
			// get the private boolean
			privateMode = defaults.getData().getBoolean("privateMode", false);
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
				if (privateMode)
				{
					addContact.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				}
				
				startActivity(addContact);
			}
		}
		else if (itemId == R.id.contact_remove_contact && parentView.hasValidPin())
		{
			if (isDeleting)
			{
				// create an alert
				AlertDialog alert = null;
				
				// get the number of items checked
				long[] checkedItemIds = contactsList.getCheckedItemIds();
				
				// verify that at least 1 item is checked
				if (checkedItemIds.length > 0)
				{
					// build the alert
					AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
					
					if (alertBuilder != null)
					{
						
						// set the builder params
						alertBuilder.setCancelable(false);
						alertBuilder.setTitle("Confirm Delete");
						
						alertBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which)
							{
								// initiate the delete
								removeItems();
								
								// dismiss the dialog
								dialog.dismiss();
							}

						});
						
						alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								// show the message
								ActionManager.showMessage(getActivity(), "Delete Mode Disabled");
								
								// put the list in multiple choice mode
								contactsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
										
								// take the list out of delete mode
								//isDeleting = false;
					
								// cancel the dialog
								dialog.cancel();
							}
						});
						
						// set the dialog alert
						alert = alertBuilder.create();
					}
					
					// show the alert
					alert.show();
				}
				else
				{
					// show the message
					ActionManager.showMessage(getActivity(), "Delete Mode Disabled");
					
					// put the list in multiple choice mode
					contactsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
							
					// take the list out of delete mode
					isDeleting = false;
				}
				
			}
			else
			{
				// put the list in multiple choice mode
				contactsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
				
				// put the list in delete mode
				isDeleting = true;
				
				ActionManager.showMessage(getActivity(), "Delete Mode Enabled");
			}
		}
	}
	
	public void removeItems()
	{
		boolean didDelete = false;
		long[] checkedItemIds = contactsList.getCheckedItemIds();
		
		// verify that there is at least 1 item checked
		if (checkedItemIds.length > 0)
		{
			DataManager dataManager = new DataManager(getActivity());
			
			if (dataManager != null)
			{
				UniArray container = dataManager.load(DataManager.CONTACT_DATA);
				
				if (container != null)
				{
					// get a list of all the keys
					String[] keys = container.getAllObjectKeys();
					
					// iterate over the length of the items checked
					for (int i = 0; i < checkedItemIds.length; i++)
					{
						// set the key to the key returned for checkedItem position returned for the value of i
						int keyIndex = (int) checkedItemIds[i];
						String key = keys[keyIndex];
						
						// try to remove the item at the key
						dataManager.removeItem(DataManager.CONTACT_DATA, key);
					}
				}
				
				// show the result toast
				ActionManager.showMessage(getActivity(), checkedItemIds.length + " items removed");
				
				// to refresh the parent
				didDelete = true;
			}
		}
		else
		{
			// show the result toast
			ActionManager.showMessage(getActivity(), " No items selected / removed");
		}
		
		// put the list in multiple choice mode
		contactsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				
		// take the list out of delete mode
		isDeleting = false;
		
		if (didDelete)
		{
			restartParent();
		}
	}

	@Override
	public void restartParent()
	{
		
		ApplicationDefaults defaults = new ApplicationDefaults(getActivity());
		
		if (defaults != null)
		{
			// set the app to reload the last view upon restart
			defaults.set("loadLastView", true);
		}
		
		Intent navStyle = null;
		
		// create intent on navStyle that is selected
		if (defaultNavType)
		{
			// pagerview swipe nav
			navStyle = new Intent(getActivity(), PagerFragmentActivity.class);
		}
		else if (!defaultNavType)
		{
			// drawerlist nav
			navStyle = new Intent(getActivity(), DrawerFragmentActivity.class);
		}
		
		// verify the intent is valid and change the activity
		if (navStyle != null)
		{
			// check if private mode is enabled
			if (privateMode)
			{
				// set the flag to exclude from recent menu
				navStyle.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			}
			
			// set the flag clearing duplicate activities
			navStyle.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			// set the password validation arg
			navStyle.putExtra("passwordIsValid", parentView.hasValidPin());
			
			// restart the parent
			startActivity(navStyle);
		}
	}
	
}
