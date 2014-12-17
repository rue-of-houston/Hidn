package com.randerson.activities;

import java.io.File;
import java.util.ArrayList;

import libs.ApplicationDefaults;
import libs.UniArray;

import com.randerson.hidn.R;
import com.randerson.interfaces.EncryptionSetup;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.interfaces.Refresher;
import com.randerson.support.DataManager;
import com.randerson.support.HidNCipher;
import com.randerson.support.HidNExplorer;
import com.randerson.support.ImageAdapter;
import com.randerson.support.ThemeMaster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

@SuppressLint("DefaultLocale")
public class AddPhotosActivity extends Activity implements FragmentSetup, Refresher {

	public final String TITLE = "Photo Browser";
	ArrayList<File> photos;
	public boolean defaultNavType;
	public boolean privateMode = false;
	public String theme;
	public String themeB;
	GridView gridView;
	HidNExplorer explorer;
	DataManager dataManager;
	HidNCipher cipher;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		// set the activity to full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		
		setContentView(R.layout.activity_add_photos);
		
		// set fail result code
		setResult(RESULT_CANCELED);
		
		new Thread(new Runnable() {
			
			@Override
			public void run()
			{
				// load the application settings
				loadApplicationSettings();
				
				// initialize the required support classes
				dataManager = new DataManager(getApplicationContext());
				cipher = new HidNCipher(getApplicationContext(), EncryptionSetup.AES_ALGORITHM);
				explorer = new HidNExplorer(getApplicationContext());
				
				if (explorer != null)
				{
					photos = explorer.getListOfPhotos();
				}
				
				// create gridview from layout xml res
				gridView = (GridView) findViewById(R.id.photosGrid);
				
				if (gridView != null && photos != null)
				{
					// set the drawable for the listView bg
					int color = ThemeMaster.getThemeId(theme)[2];
					gridView.setBackgroundColor(color);
					
					// set the gridView to allow multiple item selections
					gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
					
					// set the gridView custom adapter
					gridView.setAdapter(new ImageAdapter(getApplicationContext(), photos));
					
					// set the on item click listener
					gridView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							
							// get instance of the imageView
							ImageView img = (ImageView) view;
							
							// get the check state of the item at current position
							boolean state = gridView.isItemChecked(position);
							
							// highlight checked items and un-highlight unchecked items
							if (state == true)
							{
								img.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
							}
							else
							{
								img.setBackgroundColor(getResources().getColor(android.R.color.transparent));
							}
						}
					});
				}
			}
		}).run();
	}
	
	@SuppressLint("DefaultLocale")
	public void setupActionBar() {
			
		int color = ThemeMaster.getThemeId(theme)[0];
		
		// set the actionBar styling
		getActionBar().setBackgroundDrawable(getResources().getDrawable(color));
		
		// set the title to appear for the drawerlist view
		getActionBar().setTitle(TITLE);
		
		int themeBId = ThemeMaster.getThemeId(themeB.toLowerCase())[0];
		
		// set the background styling
		LinearLayout layoutBg = (LinearLayout) findViewById(R.id.addPhotoBg);
		
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
			
			// get the private boolean
			privateMode = defaults.getData().getBoolean("privateMode", false);
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
	
		restartParent();
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
		
		if (privateMode)
		{
			finish();
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		
		restartParent();
	}
	
	public void saveFiles()
	{
		long[] itemIds = gridView.getCheckedItemIds();
		File[] selectedImages = new File[itemIds.length];
				
		if (photos != null)
		{
			for (int i = 0; i < itemIds.length; i++)
			{
				int index = (int) itemIds[i];
				selectedImages[i] = photos.get(index);
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
				File root = getApplication().getExternalFilesDir("Album");
				
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
						for (int x = 0; x < bytes.size(); x++)
						{
							// capture each iteration of encoded data bytes
							encodedData[x] = cipher.encodeData(bytes.get(x));
						}
						
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
								boolean didSave = dataManager.saveItem(DataManager.PHOTO_DATA, mediaItem);
								
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
	
	@Override
	public void restartParent()
	{
		ApplicationDefaults defaults = new ApplicationDefaults(this);
		
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
			navStyle = new Intent(this, PagerFragmentActivity.class);
		}
		else if (!defaultNavType)
		{
			// drawerlist nav
			navStyle = new Intent(this, DrawerFragmentActivity.class);
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
			navStyle.putExtra("passwordIsValid", true);
			
			// restart the parent
			startActivity(navStyle);
		}
	}
}
