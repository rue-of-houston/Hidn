package com.randerson.activities;
import java.io.File;

import libs.ApplicationDefaults;
import libs.RegExManager;
import libs.UniArray;

import com.randerson.hidn.R;
import com.randerson.interfaces.DataSetup;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.interfaces.Refresher;
import com.randerson.support.DataManager;
import com.randerson.support.HidNExplorer;
import com.randerson.support.ThemeMaster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

@SuppressLint("HandlerLeak")
public class ViewVideosActivity extends Activity implements FragmentSetup, Refresher {

	public String TITLE = "Video Viewer";
	public String theme;
	public String themeB;
	public boolean defaultNavType;
	public boolean privateMode = false;
	public String filePath;
	public String fileName;
	public String tempPath;
	public AlertDialog alert;
	public String key;
	public VideoView vPlayer;
	public TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// set the activity to full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// inflate the view
		setContentView(R.layout.activity_view_videos);
		
		// get the extras
		Intent data = getIntent();
		
		if (data != null)
		{
			// get the bundle of extras
			Bundle extras = data.getExtras();
			
			if (extras != null)
			{
				// set the filepath
				filePath = extras.getString("filePath");
				fileName = extras.getString("fileName");
				key = extras.getString("key");
			}
		}

		// load the application settings
		loadApplicationSettings();
		
		// create the views from layout res
		vPlayer = (VideoView) findViewById(R.id.vPlayer);
		textView = (TextView) findViewById(R.id.videoName);
		
		// create the media controller
		MediaController controller = new MediaController(this);
		
		// verify the textView is valid
		if (textView != null)
		{
			// set the file name for the view
			textView.setText(fileName);
			
			textView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// show the rename alert
					alert.show();
				}
			});
		}
		
		// alert builder for building the custom rename file alert
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		if (builder != null)
		{
			// inflate the xml resource in the view
			View view =  getLayoutInflater().inflate(R.layout.rename_photo_alert, null);
			
			// create the input field and button from res
			final EditText alertInputField = (EditText) view.findViewById(R.id.alertRenamePhoto);
			
			if (alertInputField != null)
			{
				// set the filename to appear
				alertInputField.setText(fileName);
			}
			
			// set the builder params
			builder.setCancelable(false);
			builder.setView(view);
			builder.setTitle("Rename File");
			
			builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which)
				{
					
					if (alertInputField != null)
					{
						// get the text field value
						String name = alertInputField.getText().toString();
						
						if (name.length() < 3)
						{
							// show the message
							showMessage("File name must be at least 3 characters long");
						}
						else if (RegExManager.checkPattern(name, RegExManager.FILE_NAME) == true)
						{
							// reset the text view
							textView.setText(name);
							
							// set the new filename
							fileName = name;
							
							// instantiate the required classes
							HidNExplorer explorer = new HidNExplorer(getApplicationContext());
							DataManager dataManager = new DataManager(getApplicationContext());
							
							if (dataManager != null)
							{
								// get the photos object
								UniArray videos = dataManager.load(DataSetup.VIDEO_DATA);
								
								// verify the req. classes are valid
								if (videos != null && explorer != null)
								{
									// retrieve the actual data item
									UniArray item = (UniArray) videos.getObject(key);
									
									if (item != null)
									{
										// create the new file
										File currentFile = new File(filePath);
										
										// rename the hidnPath
										boolean isRenamed = explorer.renameFile(currentFile, ("." + name));
										
										if (isRenamed)
										{
											Log.i("Video Renaming", "Video Renamed");
											
											// create the new hidden path
											String hidNPath = currentFile.getParent() + "/." + name;
											Log.i("Renamed to: ", hidNPath);
											
											// get the unchanged data
											String sourcePath = item.getString("sourcePath");
											String[] encodedData = (String[]) item.getObject("encodedData");
											
											// create new save item
											UniArray newItem = dataManager.createMediaItem(name, sourcePath, hidNPath, encodedData);
											
											if (newItem != null)
											{
												// try to save the item and capture the result
												boolean didSave = dataManager.saveItem(DataManager.VIDEO_DATA, newItem);
												
												// if it saved
												if (didSave)
												{
													// try to remove the old item and capture the result
													boolean isRemoved = dataManager.removeItem(DataManager.VIDEO_DATA, key);
													
													if (isRemoved)
													{
														Log.i("Video Success", "Item successfully added");
													}
												}
											}
											
										}
										else
										{
											Log.i("Video Renaming", "Video Rename Failed");
										}
									}
								}
							}
							
							// show the message
							showMessage("File renamed");
							
							// dismiss the dialog
							dialog.dismiss();
							
							onBackPressed();
						}
						else
						{
							// show the message
							showMessage("File must contain a valid extension format");
						}
					}
				}

			});
			
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					// show the message
					showMessage("File not renamed");
		
					// cancel the dialog
					dialog.cancel();
				}
			});
			
			// create the dialog alert
			alert = builder.create();
		}
		
		// verify the videoView is valid
		if (vPlayer != null)
		{
			if (controller != null)
			{
				// sets the anchor view
				controller.setAnchorView(vPlayer);
				
				// set the media controller
				vPlayer.setMediaController(controller);
			}
		
			// create the onprep listener
			vPlayer.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					
					// start the video
					mp.start();
				}
			});
			
			// request focus for the player
			vPlayer.requestFocus();
			
			// set the video data
			vPlayer.setVideoURI(Uri.parse(filePath));
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
		LinearLayout layoutBg = (LinearLayout) findViewById(R.id.viewVideosBg);
		
		// verify the view is valid first
		if (layoutBg != null)
		{
			layoutBg.setBackground(getResources().getDrawable(themeBId));
		}
		
		// set the background styling
		LinearLayout layoutBg2 = (LinearLayout) findViewById(R.id.videoBg2);
		
		if (layoutBg2 != null)
		{
			// set the drawable for the border bg
			int color2 = ThemeMaster.getThemeId(theme)[2];
			layoutBg2.setBackgroundColor(color2);
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
	public void onActionBarItemClicked(int itemId) {
		// TODO Auto-generated method stub
		
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
			onBackPressed();
		}
	}
	
	@Override
	public void onBackPressed() 
	{
		restartParent();
	}
	
	public void showMessage(String message)
	{
		Toast msg = Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT);
		
		if (msg != null)
		{
			msg.show();
		}
	}
	
	@Override
	public void restartParent()
	{
		boolean privateMode = false;
		
		ApplicationDefaults defaults = new ApplicationDefaults(this);
		
		if (defaults != null)
		{
			// set the app to reload the last view upon restart
			defaults.set("loadLastView", true);
			
			// get the private boolean
			privateMode = defaults.getData().getBoolean("privateMode", false);
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
