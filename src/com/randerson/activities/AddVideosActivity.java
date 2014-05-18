package com.randerson.activities;

import java.io.File;
import java.util.ArrayList;

import libs.ApplicationDefaults;
import libs.UniArray;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.randerson.hidn.R;
import com.randerson.interfaces.EncryptionSetup;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.support.DataManager;
import com.randerson.support.HidNCipher;
import com.randerson.support.HidNExplorer;
import com.randerson.support.ListViewAdapter;
import com.randerson.support.ThemeMaster;

public class AddVideosActivity extends Activity implements FragmentSetup {

	public final String TITLE = "Video Browser";
	ArrayList<File> videos;
	public boolean defaultNavType;
	public String theme;
	public String themeB;
	ListView listView;
	HidNExplorer explorer;
	DataManager dataManager;
	HidNCipher cipher;
	String[] videoNames;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_add_videos);
		
		// set fail result code
		setResult(RESULT_CANCELED);
		
		new Thread(new Runnable() {
			
			@Override
			public void run()
			{
				// load the application settings
				//loadApplicationSettings();
				
				// initialize the required support classes
				dataManager = new DataManager(getApplicationContext());
				cipher = new HidNCipher(getApplicationContext(), EncryptionSetup.AES_ALGORITHM);
				explorer = new HidNExplorer(getApplicationContext());
				
				if (explorer != null)
				{
					videos = explorer.getListOfVideos();
					
					if (videos != null)
					{
						// initialize the video names resource
						videoNames = new String[videos.size()];
						
						for (int n = 0; n < videos.size(); n++)
						{
							// get the file name for the file at current index
							String filename = videos.get(n).getName();
							
							// add the filename to the resource string array
							videoNames[n] = filename;
						}
					}
				}
				
				// create gridview from layout xml res
				listView = (ListView) findViewById(R.id.videosList);
				
				if (listView != null && videos != null)
				{
					// set the gridView to allow multiple item selections
					listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
					
					ArrayAdapter<String> adapter = new ListViewAdapter(getApplicationContext(), R.layout.video_list_item, R.id.videoListItem, videoNames);
					
					// set the listView adapter
					listView.setAdapter(adapter);
					
					// set the on item click listener
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							
							// get instance of the linearLayout
							LinearLayout layout = (LinearLayout) view;
							TextView txtView = null;
							
							if (layout != null)
							{
								txtView = (TextView) layout.findViewById(R.id.videoListItem);
							}
							
							// get the check state of the item at current position
							boolean state = listView.isItemChecked(position);
							
							// highlight checked items and un-highlight unchecked items
							if (state == true && txtView != null)
							{
								txtView.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
							}
							else
							{
								txtView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
							}
						}
					});
				}
			}
		}).run();
	}
	
	@SuppressLint("DefaultLocale")
	public void setupActionBar() {
			
		int color = ThemeMaster.getThemeId(theme);
		
		// set the actionBar styling
		getActionBar().setBackgroundDrawable(getResources().getDrawable(color));
			
		// set the title to appear for the drawerlist view
		getActionBar().setTitle(TITLE);
	
		int themeBId = ThemeMaster.getThemeId(themeB.toLowerCase());
		
		// set the background styling
		LinearLayout layoutBg = (LinearLayout) findViewById(R.id.addVideoBg);
		
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
		ApplicationDefaults defaults = new ApplicationDefaults(this.getApplicationContext());
		
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
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public void onActionBarItemClicked(int itemId)
	{
		
	}
	
	@Override
	public void onBackPressed() {
		
		// method for saving the selected files
		saveFiles();
		
		super.onBackPressed();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		ApplicationDefaults defaults = new ApplicationDefaults(this.getApplicationContext());
		
		if (defaults != null)
		{
			// set the app to reload the last view upon restart
			defaults.set("loadLastView", true);
		}
		
		finish();
	}
	
	public void saveFiles()
	{
		long[] itemIds = listView.getCheckedItemIds();
		File[] selectedImages = new File[itemIds.length];
				
		if (videos != null)
		{
			for (int i = 0; i < itemIds.length; i++)
			{
				int index = (int) itemIds[i];
				selectedImages[i] = videos.get(index);
			}
		}
		
		final File[] images = selectedImages;
		
		// creat a new thread to handle the file transfering
		new Thread(new Runnable()
		{
			
			@Override
			public void run() 
			{
	
				// create a root of the app storage directory
				File root = getApplication().getExternalFilesDir("Movies");
				
				if (explorer != null)
				{
					
					// iterat over the file array of images
					for (int i = 0; i < images.length; i++)
					{
						// create a source file representing the current index file of images array
						File source = images[i];
						
						// get the file meta data for storing
						String originPath = source.getPath();
						String filename = source.getName();
						String hidnPath = root + "/." + filename;
						
						// get the byte array and create an array for storing the encoded data
						ArrayList<byte[]> bytes = HidNCipher.toByteArray(source);
						String[] encodedData = new String[bytes.size()];
						
						// iterate over the byte array and encode the data, storing it into the endcoded string array
						/*for (int x = 0; x < bytes.size(); x++)
						{
							// capture each iteration of encoded data bytes
							encodedData[x] = cipher.encodeData(bytes.get(x));
						}*/
						
						// attempt to move the file and capture the result
						boolean result = explorer.moveFile(source, root, true);
						
						// verify the result true or false
						// true - remove the original file and save encoded data
						if (result)
						{
							Log.i("Result", "File moved");
							
							// put the retrieved meta data into valid object and store it on device
							UniArray mediaItem = dataManager.createMediaItem(filename, originPath, hidnPath, encodedData);
							
							if (mediaItem != null)
							{
								// save the encoded item
								boolean didSave = dataManager.saveItem(DataManager.VIDEO_DATA, mediaItem);
								
								// verify the item was saved
								if (didSave)
								{
									// remove the file from system
									boolean fileRemoved = explorer.deleteFile(source);
									
									// verify the file was removed
									if (fileRemoved)
									{
										// set the result code to success
										setResult(RESULT_OK);
										
										Log.i("Result", "File Removed");
										
									}
								}
								
							}
							
						}
						else
						{
							Log.i("Result", "File not moved");
						}
					}
				}
				
			}
		}).run();
	}
	
}
