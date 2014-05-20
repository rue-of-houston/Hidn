package com.randerson.activities;
import libs.ApplicationDefaults;
import libs.UniArray;

import com.randerson.hidn.R;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.support.DataManager;
import com.randerson.support.ListViewAdapter;
import com.randerson.support.ThemeMaster;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ScrollView;

public class ViewBookmarks extends Activity implements FragmentSetup{

	String[] bookmarkNames;
	public String theme;
	public String themeB;
	public boolean defaultNavType;
	public String TITLE = "Bookmark Viewer";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// set the activity to full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		
		setContentView(R.layout.activity_view_bookmarks);
		
		// set the default return value
		setResult(RESULT_CANCELED);
		
		// setup the bookmark names array
		DataManager dataManager = new DataManager(this);
		
		if (dataManager != null)
		{
			// load the browser data file
			UniArray browserData = dataManager.load(DataManager.BROWSER_DATA);
			
			if (browserData != null)
			{
				// retrieve the bookmark names (keys)
				bookmarkNames = browserData.getAllObjectKeys();
			}
		}
		
		// load the app settings
		loadApplicationSettings();
		
		// create the list from layout res
		ListView list = (ListView) findViewById(R.id.bookmarksList);
		
		if (list != null)
		{
			// create the adapter
			ListViewAdapter adapter = new ListViewAdapter(this, R.layout.bookmark_list_item, R.id.bookmarkListItem, bookmarkNames);
			
			if (adapter != null)
			{
				// set the adapter
				list.setAdapter(adapter);
			}
			
			// set the drawable for the listView bg
			int color = ThemeMaster.getThemeId(theme)[2];
			list.setBackgroundColor(color);
			
			// set the listview click listener
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					// setup the bookmark names array
					DataManager dataManager = new DataManager(getApplicationContext());
					
					if (dataManager != null)
					{
						// load the browser data file
						UniArray browserData = dataManager.load(DataManager.BROWSER_DATA);
						
						if (browserData != null)
						{
							// get the individual bookmark item
							UniArray bookmark = (UniArray) browserData.getObject(bookmarkNames[position]);
							
							if (bookmark != null)
							{
								// get the url string from the bookmark
								String url = bookmark.getString("url");
								
								if (url != null)
								{
									// create an intent to return the data in
									Intent urlData = new Intent();
									
									if (urlData != null)
									{
										// add the url to the intent
										urlData.putExtra("url", url);
										
										// set the result to ok and pass in the url data
										setResult(RESULT_OK, urlData);
										
										finish();
									}
									
								}
							}
						}
					}
					
				}
			});
		}
	}
	
	@Override
	public void setupActionBar() {
		
		int color = ThemeMaster.getThemeId(theme)[0];
		
		// set the actionBar styling
		getActionBar().setBackgroundDrawable(getResources().getDrawable(color));
		
		// set the title to appear for the drawerlist view
		getActionBar().setTitle(TITLE);
		
		int themeBId = ThemeMaster.getThemeId(themeB.toLowerCase())[0];
		
		// set the background styling
		ScrollView layoutBg = (ScrollView) findViewById(R.id.viewContactBg);
		
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
		ApplicationDefaults defaults = new ApplicationDefaults(this);
		
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
	public void onActionBarItemClicked(int itemId) {
		// null
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
		
		finish();
	}

}
