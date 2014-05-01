package com.randerson.hidn;

import com.randerson.entities.DrawerItemClickListener;
import com.randerson.fragmentviews.BrowserActivity;
import com.randerson.fragmentviews.ContactsActivity;
import com.randerson.fragmentviews.DocumentsActivity;
import com.randerson.fragmentviews.HomeActivity;
import com.randerson.fragmentviews.NotesActivity;
import com.randerson.fragmentviews.PhotosActivity;
import com.randerson.fragmentviews.VideosActivity;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.interfaces.MenuHandler;

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

public class DrawerFragmentActivity extends FragmentActivity implements MenuHandler {
	
	public Menu FragmentMenu;
	public Fragment CurrentFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// set the activity to full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// inflate the layout xml file
		setContentView(R.layout.activity_main2);
		
		// create the list and drawerlayout from ref xml
		ListView list = (ListView) findViewById(R.id.drawerList);
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
		
		// create the drawer string array of titles
		String[] titles = new String[]{HomeActivity.TITLE, ContactsActivity.TITLE, BrowserActivity.TITLE, NotesActivity.TITLE, 
									   PhotosActivity.TITLE, VideosActivity.TITLE, DocumentsActivity.TITLE};
		
		// create the adapter for the list
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplication(), R.layout.drawer_list, titles);
		
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
		
		list.setItemChecked(0, true);
		
		// inflate default view
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
	public Menu getParentMenu(Fragment fragment)
	{
		// set the reference to the currently inflated fragment
		CurrentFragment = fragment;
		
		// reset the actionbar
		int pos;
		while ((pos = (getActionBar().getTabCount())) > 0)
		{
			getActionBar().removeTabAt(pos-1);
		}
		
		return FragmentMenu;
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
		
			case R.id.home_settings:
			case R.id.contacts_settings:
			case R.id.browser_settings:
			case R.id.notes_settings:
			case R.id.documents_settings:
			case R.id.photos_settings:
			case R.id.videos_settings:
			
				// create the intent to start the settings activity
				Intent settingsActivity = new Intent(this, SettingsActivity.class);
				
				// verify the intent is created and start the activity
				if (settingsActivity != null)
				{
					startActivity(settingsActivity);
				}
				
				break;
			
			  // calls on the interface method for the current fragment passing in the 
			  // selected actionbar menu item id
			default:
				
				fs.onActionBarItemClicked(item.getItemId());
				break;
		}
		
		return true;
	}

}
