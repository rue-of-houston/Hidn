package com.randerson.hidn;

import com.randerson.entities.FragmentAdapter;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.interfaces.MenuHandler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class PagerFragmentActivity extends FragmentActivity implements MenuHandler {
	
	public Menu FragmentMenu;
	public Fragment CurrentFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// set the activity to full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// inflate the layout xml file
		setContentView(R.layout.activity_main);
		
		// reference the pager from layout
		ViewPager pager = (ViewPager) findViewById(R.id.mainPager);
		
		// verify that the pager is valid
		if (pager != null)
		{
			// create an instance of the pager's adapter
			FragmentAdapter fragAdapter = new FragmentAdapter(getSupportFragmentManager());
			
			// set the adapter
			pager.setAdapter(fragAdapter);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		// close the activity and return to password screen
		finish();
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
		// removes backing out
	}

	@Override
	public Menu getParentMenu(Fragment fragment)
	{
		// set the reference to the currently inflated fragment
		CurrentFragment = fragment;
						
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