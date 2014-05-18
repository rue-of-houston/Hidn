package com.randerson.activities;

import libs.ApplicationDefaults;

import com.randerson.fragments.BrowserActivity;
import com.randerson.fragments.ContactsActivity;
import com.randerson.fragments.DocumentsActivity;
import com.randerson.fragments.HomeActivity;
import com.randerson.fragments.NotesActivity;
import com.randerson.fragments.PhotosActivity;
import com.randerson.fragments.VideosActivity;
import com.randerson.hidn.R;
import com.randerson.interfaces.Constants;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.interfaces.ViewHandler;
import com.randerson.support.DrawerItemClickListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DrawerFragmentActivity extends FragmentActivity implements ViewHandler, Constants {
	
	public Menu FragmentMenu;
	public Fragment CurrentFragment;
	public ListView list;
	public boolean shouldDisablePassLock = false;
	private boolean hasValidPin = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// set the activity to full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// inflate the layout xml file
		setContentView(R.layout.activity_main2);
		
		// get the intent to check if the correct pin was entered
		Intent intent = getIntent();
		
		if (intent != null)
		{
			// retrieve the intent extras object
			Bundle extras = intent.getExtras();
			
			if (extras != null)
			{
				// get the result of the pin entry
				hasValidPin = extras.getBoolean("passwordIsValid");
			}
		}
		
		// set the titlebar color
		setTitleColor(getResources().getColor(android.R.color.white));
		
		// create the list and drawerlayout from ref xml
		list = (ListView) findViewById(R.id.drawerList);
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
		
		// create the drawer string array of titles
		String[] titles = new String[]{HomeActivity.TITLE, BrowserActivity.TITLE, ContactsActivity.TITLE, PhotosActivity.TITLE, 
									   VideosActivity.TITLE, NotesActivity.TITLE, DocumentsActivity.TITLE};
		
		// create the adapter for the list
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.drawer_list, titles);
		
		// verify the list is created
		if (list != null)
		{	
			// verify the adapter is valid
			if (adapter != null)
			{
				// set the list adapter
				list.setAdapter(adapter);
				
				// set the item click listener for the list
				list.setOnItemClickListener(new DrawerItemClickListener(getSupportFragmentManager(), list, drawer));
			}
		}
		
		// inflate default view and set the drawer item to be checked
		list.setItemChecked(HOME, true);
		getSupportFragmentManager().beginTransaction().replace(R.id.drawerContent, new HomeActivity()).commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// capture the menu
		FragmentMenu = menu;
		
		return true;
	}
	
	@Override
	public void onBackPressed()
	{
		// remove backing out
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		FragmentSetup fs = null;
		
		if (CurrentFragment instanceof FragmentSetup)
		{
			fs = (FragmentSetup) CurrentFragment;
		}
		
		switch(item.getItemId())
		{
		
			case R.id.main_settings:
			
				// going into settings menu so temporarily disable the passlock for view change
				shouldDisablePassLock = true;
				
				// create the intent to start the settings activity
				Intent settingsActivity = new Intent(this, SettingsActivity.class);
				
				// verify the intent is created and start the activity
				if (settingsActivity != null)
				{
					// pass in the valid pin boolean
					settingsActivity.putExtra("hasValidPin", hasValidPin);
					
					// start the activity
					startActivity(settingsActivity);
				}
				
				break;
				
			// these cases fall-through on purpose
			case R.id.photos_add_photo:
			case R.id.videos_add_video:
			case R.id.notes_add_note:
			case R.id.documents_add_doc:
			case R.id.browser_view_bookmark:
			case R.id.contact_new_contact:
		
			// going into extended menu so temporarily disable the passlock for view change
			shouldDisablePassLock = true;
			
			  // calls on the interface method for the current fragment passing in the 
			  // selected actionbar menu item id
			default:
				
				fs.onActionBarItemClicked(item.getItemId());
				break;
		}
		
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// reset the activty exemption boolean
		shouldDisablePassLock = false;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		ApplicationDefaults defaults = new ApplicationDefaults(this);
		
		if (defaults != null)
		{
			// set the app to reload the last view upon restart
			defaults.set("loadLastView", true);
		}
		
		// close the activity and return to password screen if not changing to settings activity
		if (shouldDisablePassLock == false) 
		{
			finish();
		}
	}

	@Override
	public void loadPreviousView(int layoutId)
	{
		// the fragment to inflate
		Fragment previousView = null;
		
		// check the layoutId passed in and instantiate the proper fragment
		switch(layoutId)
		{
			case BROWSER:
				previousView = new BrowserActivity();
				break;
				
			case CONTACTS:
				previousView = new ContactsActivity();
				break;
				
			case PHOTOS:
				previousView = new PhotosActivity();
				break;
				
			case VIDEOS:
				previousView = new VideosActivity();
				break;
				
			case NOTES:
				previousView = new NotesActivity();
				break;
				
			case DOCUMENTS:
				previousView = new DocumentsActivity();
				break;
				
				default:
					break;
		}
		
		// verify the fragment is valid
		if (previousView != null)
		{
			// inflate previous view and check the drawer item
			list.setItemChecked(layoutId, true);
			getSupportFragmentManager().beginTransaction().replace(R.id.drawerContent, previousView).commit();
		}
		
	}

	@Override
	public void passFragmentToParent(Fragment fragment, int lastViewId)
	{
		// set reference to the current inflated fragment
		CurrentFragment = fragment;
		
		if (lastViewId != HOME)
		{
			ApplicationDefaults defaults = new ApplicationDefaults(this);
		
			if (defaults != null)
			{
				// update the current view as the lastView
				defaults.set("lastView", lastViewId);
			}
		}
	}

	@Override
	public void setDisablePassLock(boolean state)
	{
		shouldDisablePassLock = state;
	}

	@Override
	public boolean hasValidPin()
	{
		return hasValidPin;
	}

}
