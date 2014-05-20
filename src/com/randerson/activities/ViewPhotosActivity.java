package com.randerson.activities;

import java.io.File;

import libs.ApplicationDefaults;
import libs.ApplicationManager;
import libs.RegExManager;
import libs.UniArray;

import com.randerson.hidn.R;
import com.randerson.interfaces.DataSetup;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.services.BitmapDecoderService;
import com.randerson.support.DataManager;
import com.randerson.support.HidNExplorer;
import com.randerson.support.ThemeMaster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewPhotosActivity extends Activity implements FragmentSetup {

	public String TITLE = "Photo Viewer";
	public String theme;
	public String themeB;
	public boolean defaultNavType;
	public TextView textView;
	public String filePath;
	public String fileName;
	public AlertDialog alert;
	public String key;
	
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		// set the activity to full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// inflate the view
		setContentView(R.layout.activity_view_photos);
		
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
		
		// create the text view from the xml res
		textView = (TextView) findViewById(R.id.photoName);
		
		if (textView != null)
		{
			// set the text
			textView.setText(fileName);
			
			// set the click listener
			textView.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) {
					
					// show the rename popup
					alert.show();
				}
			});
			
		}
	
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		if (builder != null)
		{
			// inflate the xml resource in the view
			
			View view =  getLayoutInflater().inflate(R.layout.rename_photo_alert, null);
			
			// create the input field and button from res
			final EditText alertInputField = (EditText) view.findViewById(R.id.alertRenamePhoto);
			
			// set the builder params
			builder.setCancelable(false);
			builder.setView(view);
			builder.setTitle("Rename File");
			
			builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
				
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
								UniArray photos = dataManager.load(DataSetup.PHOTO_DATA);
								
								// verify the req. classes are valid
								if (photos != null && explorer != null)
								{
									// retrieve the actual data item
									UniArray item = (UniArray) photos.getObject(key);
									
									if (item != null)
									{
										// create the new file
										File currentFile = new File(filePath);
										
										// rename the hidnPath
										boolean isRenamed = explorer.renameFile(currentFile, ("." + name));
										
										if (isRenamed)
										{
											Log.i("Photo Renaming", "Photo Renamed");
											
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
												boolean didSave = dataManager.saveItem(DataManager.PHOTO_DATA, newItem);
												
												// if it saved
												if (didSave)
												{
													// try to remove the old item and capture the result
													boolean isRemoved = dataManager.removeItem(DataManager.PHOTO_DATA, key);
													
													if (isRemoved)
													{
														Log.i("Photo Success", "Item successfully added");
													}
												}
											}
											
										}
										else
										{
											Log.i("Photo Renaming", "Photo Rename Failed");
										}
									}
								}
							}
							
							// show the message
							showMessage("File renamed");
							
							// dismiss the dialog
							dialog.dismiss();
							
							finish();
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
		
		// create the imageview from the xml res
		final ImageView imageView = (ImageView) findViewById(R.id.photoViewer);
	
		if (imageView != null)
		{
			// get a random number for the id
			final int serviceCode = (int) (Math.random() * 1000000);
			
			// create the handler callback
			Handler handler = new Handler()
			{
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					
					// verify the msg has received ok
					if (msg.arg1 == RESULT_OK && msg.arg2 == serviceCode)
					{
						if (msg.obj != null)
						{
							// retrieve the drawable
							Drawable image = (Drawable) msg.obj;
							
							// set the drawable
							imageView.setBackground(image);
						}
					}
				}
			}; 
			
			// instantiate the decoding service
			Intent decoderService = new Intent(getApplicationContext(), BitmapDecoderService.class);
			
			if (decoderService != null)
			{
				// create the messenger for the handler
				Messenger messenger = new Messenger(handler);
				
				if (messenger != null)
				{

					// get the window size
					int height = (int) (0.8 * ApplicationManager.getWindowSize(getApplicationContext())[1]);
					int width = (int) (0.8 * ApplicationManager.getWindowSize(getApplicationContext())[0]);
					
					// add the service args
					decoderService.putExtra("filePath", filePath);
					decoderService.putExtra("messenger", messenger);
					decoderService.putExtra("serviceCode", serviceCode);
					decoderService.putExtra("height", height);
					decoderService.putExtra("width", width);
					
					// start the service
					startService(decoderService);
				}
			}
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
		ScrollView layoutBg = (ScrollView) findViewById(R.id.viewPhotosBg);
		
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
	
	private void showMessage(String message)
	{
		Toast msg = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
		
		if (msg != null)
		{
			msg.show();
		}
	}
	
}
