package com.randerson.activities;

import java.io.File;

import libs.ApplicationDefaults;
import libs.FileSystem;
import libs.UniArray;
import com.randerson.hidn.R;
import com.randerson.interfaces.Constants;
import com.randerson.interfaces.DataSetup;
import com.randerson.interfaces.KinveySetup;
import com.randerson.kinvey.KinveyManager;
import com.randerson.support.ThemeMaster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

@SuppressLint("HandlerLeak")
public class StartUpActivity extends Activity implements Constants{

	ProgressDialog loadingAlert = null;
	boolean firstRun = true;
	String username;
	String password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// remove the title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// set the activity to full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// create a loading dialog
		loadingAlert = new ProgressDialog(this);
		
		if (loadingAlert != null)
		{
			// set the dialog attributes
			loadingAlert.setCancelable(false);
			loadingAlert.setTitle("Contacting Backend");
			loadingAlert.setMessage("Loading Please Wait");
			
			// show the dialog
			loadingAlert.show();
		}
		
		// inflate the xml layout file
		setContentView(R.layout.activity_startup);
		
		ApplicationDefaults defaults = new ApplicationDefaults(this);
		
		if (defaults != null)
		{
			// check for first app run, return true if no data is set
			firstRun = defaults.getData().getBoolean("firstRun", true);
			
			// get the user credentials if exist
			username = defaults.getData().getString("email", null);
			password = defaults.getData().getString("password", null);
		}
		
		// check if this is the first app run, run setup if so otherwise boot up password screen
		if (firstRun)
		{
			// setup the defaults
			setupApp();
			
			// create the intent to start the signup activity
			Intent setupScreen = new Intent(this, SignupActivity.class);
			
			// verify the intent is valid
			if (setupScreen != null)
			{
				// remove the loading dialog
				loadingAlert.dismiss();
				
				// start the activity
				startActivity(setupScreen);
			}
		}
		else if (!firstRun)
		{
			Handler handler = new Handler()
			{
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					
					if (msg != null && msg.arg1 == Activity.RESULT_OK)
					{
						if (msg.arg2 == KinveySetup.SIGN_IN_NO_SYNC_RESULT)
						{
							// app should be have been setup previously and data synced so no other action is required
							// create an intent to start security activity
							Intent securityScreen = new Intent(getApplicationContext(), PasswordActivity.class);
							
							if (securityScreen != null)
							{
								// remove the loading dialog
								loadingAlert.dismiss();
								
								// start the activity
								startActivity(securityScreen);
							}
						}
					}
				}
			};
			
			// init the Kinvey manager class
			KinveyManager kvManager = new KinveyManager(this);
			
			// try to login to kinvey service; no data sync required
			kvManager.signIn(username.toLowerCase(), password.toLowerCase(), KinveySetup.SIGN_IN_NO_SYNC_RESULT, handler);
		}
	}
	
	public void setupApp()
	{
		ApplicationDefaults defaults = new ApplicationDefaults(this.getApplicationContext());
		
		if (defaults != null)
		{
			// set the first run boolean to false now that setup has taken place
			defaults.set("firstRun", false);
			
			// set the default theme style
			defaults.set("theme", "4_3");
			defaults.set("themeB", "Dark");
			
			// set the default nav tyle
			defaults.set("defaultNavType", false);
			
			// set the mock access setting
			defaults.set("mockAccess", true);
			
			// set the private mode setting
			defaults.set("privateMode", false);
			
			// set last view reloading setting
			defaults.set("loadLastView", false);
			
			// set the lastView setting
			defaults.set("lastView", HOME);
			
			// set the list theme colors
			defaults.set("themeListA", ThemeMaster.getColor("#9FA6A4"));
			defaults.set("themeListB", ThemeMaster.getColor("#000000"));
		}
		
		// create the top-level UniArray
		String[] CONTAINER_PATHS = new String[]{DataSetup.APP_BROWSER_FILENAME, DataSetup.APP_CONTACTS_FILENAME,
										   DataSetup.APP_DOCUMENTS_FILENAME, DataSetup.APP_ENCRYPTION_FILENAME,
										   DataSetup.APP_NOTES_FILENAME, DataSetup.APP_PHOTOS_FILENAME, DataSetup.APP_VIDEOS_FILENAME
										  };
		
		// the 1st level UniArray keys
		String[] CONTAINER_NAMES = new String[]{ DataSetup.BROWSER_KEY, DataSetup.CONTACT_KEY,
											   	 DataSetup.DOCUMENT_KEY, DataSetup.ENCRYPTION_KEY,
											   	 DataSetup.NOTE_KEY, DataSetup.PHOTO_KEY, DataSetup.VIDEO_KEY
												};
		
		// iterate over the container names creating and adding them to the save file
		for (int n = 0; n < CONTAINER_NAMES.length; n++)
		{
			// create the empty parent container
			UniArray appData = new UniArray();
			
			// create an empty data container object
			UniArray container = new UniArray();
			
			// add the container with its key to the parent data container
			appData.putObject(CONTAINER_NAMES[n], container);
			
			// create the initial save file
			boolean success = FileSystem.writeObjectFile(this, appData, CONTAINER_PATHS[n], false);
		
			if (success)
			{
				Log.i("App Setup", "Data container " + CONTAINER_NAMES[n] + " created and saved");
			}
			else
			{
				Log.i("App Setup", "Data container " + CONTAINER_NAMES[n] + " failed to create and save");
			}
		}
		
		// setup the app public folder
		File publicDir = new File(Environment.getExternalStorageDirectory() + "/HidN/"); 
		publicDir.mkdir();
	}
	

}
