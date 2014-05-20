package com.randerson.fragments;

import libs.ApplicationDefaults;
import libs.UniArray;

import com.randerson.activities.AddVideosActivity;
import com.randerson.activities.ViewVideosActivity;
import com.randerson.hidn.R;
import com.randerson.interfaces.Constants;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.interfaces.ViewHandler;
import com.randerson.support.DataManager;
import com.randerson.support.ListViewAdapter;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class VideosActivity extends android.support.v4.app.Fragment implements FragmentSetup, Constants {

	public static final String TITLE = "Videos";
	public ViewHandler parentView;
	public String theme;
	public String themeB;
	public boolean defaultNavType;
	public View root;
	public DataManager dataManager;
	public String[] videoNames;
	public String[] videoPaths;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{

		root = inflater.inflate(R.layout.activity_videos, container, false);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {

				// turns on options menu in fragment
				setHasOptionsMenu(true);
				
				// load the application settings
				loadApplicationSettings();
				
				if (parentView != null && parentView.hasValidPin())
				{

					// init the datamanager class
					dataManager = new DataManager(getActivity());
					
					if (dataManager != null)
					{
						// get the videos data object
						UniArray videos = (UniArray) dataManager.load(DataManager.VIDEO_DATA);
						
						if (videos !=  null)
						{
							// get all of the videos data keys
							videoNames = videos.getAllObjectKeys();

						}
					}
					
					
					// create the listview from layout file
					ListView videoList = (ListView) root.findViewById(R.id.videoList);
					
					if (videoList != null && videoNames != null)
					{
						// create the adapter
						ListViewAdapter adapter = new ListViewAdapter(getActivity(), R.layout.video_list_item, R.id.videoListItem, videoNames);
						
						// check if the adapter is valid
						if (adapter != null)
						{
							videoList.setAdapter(adapter);
						}
						
						// set the drawable for the listView bg
						int color = ThemeMaster.getThemeId(theme)[2];
						videoList.setBackgroundColor(color);
						
						// setup the single click listeners
						videoList.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view,
									int position, long id)
							{
								
								// create the intent to launch the detail view activity and the bundle for passing
								// the activity details upon loading
								Intent detailView = new Intent(getActivity(), ViewVideosActivity.class);
								
								// verify the intent is valid, if so pass in the args and load it up
								if (detailView != null)
								{	
									if (dataManager != null)
									{
										// get the videos data object
										UniArray videos = (UniArray) dataManager.load(DataManager.VIDEO_DATA);
										
										if (videos !=  null)
										{
											// get all of the videos data keys
											UniArray video = (UniArray) videos.getObject(videoNames[position]);
											
											if (video != null)
											{
												// add the data to the intent
												String fileName = video.getString("fileName");
												String filePath = video.getString("hidnPath");
												
												detailView.putExtra("key", videoNames[position]);
												detailView.putExtra("fileName", fileName);
												detailView.putExtra("filePath", filePath);
												
												detailView.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
											}

										}
									}
									
									// disable the passLock
									parentView.setDisablePassLock(true);
									
									// start activity
									startActivity(detailView);
								}
							}
						});
						
						// setup the long click listener
						videoList.setOnItemLongClickListener(new OnItemLongClickListener() {

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
				
			}
		}).run();
		
		return root;
	}

	@SuppressLint("DefaultLocale")
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
		LinearLayout layoutBg = (LinearLayout) root.findViewById(R.id.videosBg);
		
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
		
		inflater.inflate(R.menu.videos_menu, menu);
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
				parentView.passFragmentToParent(this, VIDEOS);
			}
		}
	}


	@Override
	public void onActionBarItemClicked(int itemId)
	{
		// verify the id matches and the pin was valid
		if (itemId == R.id.videos_add_video && parentView.hasValidPin())
		{
			Intent importVideos = new Intent(getActivity(), AddVideosActivity.class);
			
			if (importVideos != null)
			{
				importVideos.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				
				startActivity(importVideos);
			}
		}
	}
	
}
