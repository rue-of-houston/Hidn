package com.randerson.activities;

import libs.ApplicationDefaults;

import com.randerson.hidn.R;
import com.randerson.interfaces.Constants;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.interfaces.ViewHandler;
import com.randerson.support.FragmentAdapter;
import com.randerson.support.ThemeMaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class PagerFragmentActivity extends FragmentActivity implements ViewHandler, Constants {
	
	public Menu fragmentMenu;
	public Fragment currentFragment;
	public ViewPager pager;
	public String theme = "4_3";
	public boolean shouldDisablePassLock = false;
	public FragmentAdapter fragAdapter;
	private boolean hasValidPin = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// set the activity to full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// inflate the layout xml file
		setContentView(R.layout.activity_main);
		
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
		
		// reference the pager from layout
		pager = (ViewPager) findViewById(R.id.mainPager);
		
		ApplicationDefaults defaults = new ApplicationDefaults(this);
		
		if (defaults != null)
		{
			theme = defaults.getData().getString("theme", "4_3");
		}
		
		// verify that the pager is valid
		if (pager != null)
		{	
			// create an instance of the pager's adapter
			fragAdapter = new FragmentAdapter(getSupportFragmentManager());
			
			// set the adapter
			pager.setAdapter(fragAdapter);
			
			PagerTabStrip tabStrip = (PagerTabStrip) pager.getChildAt(0);
			
			if (tabStrip != null)
			{
				// set the pagerview background color to match the theme
				int color = ThemeMaster.getThemeId(theme);
				tabStrip.setBackground(getResources().getDrawable(color));
			}
			
			pager.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int lastViewId) {
					
					if (lastViewId != HOME && lastViewId == pager.getCurrentItem())
					{
						ApplicationDefaults defaults = new ApplicationDefaults(getApplicationContext());
						
						if (defaults != null)
						{
							// update the current view as the lastView
							defaults.set("lastView", lastViewId);
						}
						
						// verify that the adapter is valid
						if (fragAdapter != null)
						{
							// get the current fragment
							currentFragment = fragAdapter.getFragment(lastViewId);
						}
					}
					
				}
				
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// reset the active lock boolean
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
		
		// close the activity and return to password screen if not changing to exempt activity
		if (shouldDisablePassLock == false) 
		{
			finish();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// capture the menu
		fragmentMenu = menu;
		
		return true;
	}
	
	 @Override
	public void onBackPressed()
	{
		// removes backing out
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		FragmentSetup fs = null;
		
		if (currentFragment instanceof FragmentSetup)
		{
			fs = (FragmentSetup) currentFragment;
		}
		
		switch(item.getItemId())
		{
		
			case R.id.main_settings:
			
				// going into extended menu so temporarily disable the passlock for view change
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
	public void loadPreviousView(int layoutId)
	{
		// the item id to jump the pager to
		int previousView = HOME;
		
		// check the layoutId passed in and set the proper pager item value
		switch(layoutId)
		{
				
			case BROWSER:
				previousView = BROWSER;
				break;
		
			case CONTACTS:
				previousView = CONTACTS;
				break;
				
			case PHOTOS:
				previousView = PHOTOS;
				break;
				
			case VIDEOS:
				previousView = VIDEOS;
				break;
				
			case NOTES:
				previousView = NOTES;
				break;
				
			case DOCUMENTS:
				previousView = DOCUMENTS;
				break;
				
				default:
					break;
		}
		
		// verify the pager item is not home screen
		if (previousView > 0)
		{
			// set the item for the previous view in pager
			pager.setCurrentItem(previousView, true);
		}
		
	}
	
	@Override
	public void passFragmentToParent(Fragment fragment, int lastViewId)
	{
		// this does nothing inside the context of the pagerView
		// the task is delegated to the page change listener
	}

	@Override
	public void setDisablePassLock(boolean state)
	{
		shouldDisablePassLock = state;
	}

	@Override
	public boolean hasValidPin() {
		return hasValidPin;
	}
	
}