package com.randerson.fragments;

import libs.ApplicationDefaults;

import com.randerson.activities.NewViewContactActivity;
import com.randerson.hidn.R;
import com.randerson.interfaces.Constants;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.interfaces.ViewHandler;
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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		root = inflater.inflate(R.layout.activity_contacts, container, false);
		
		// load the application settings
		loadApplicationSettings();
		
		// method for setting the actionBar
		setupActionBar();
		
		// turns on options menu in fragment
		setHasOptionsMenu(true);
		
		String[] contactNames = new String[]{"John Smith", "Harold Moyado", "Charlotte Lopez", "Tommy Jones"};
		
		ListView contactsList = (ListView) root.findViewById(R.id.contactsList);
		
		if (contactsList != null)
		{
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.contact_list_item, R.id.contactListItem, contactNames);
			
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
					Intent detailView = new Intent(getActivity(), NewViewContactActivity.class);
					
					// the selected item data will be passed into the detailView intent for showing / editing
					switch(position)
					{
						case 0:
							
							break;
							
							default:
								break;
					}
					
					// verify the intent is valid, if so pass in the args and load it up
					if (detailView != null)
					{
						// determine that the view should be ready to show detailed contact information
						detailView.putExtra("isNewContact", false);
						
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
		switch(itemId)
		{
		case R.id.contact_new_contact:
			break;
		
			default:
				break;
		}
	}
	
}
