package com.randerson.activities;

import libs.ApplicationDefaults;

import com.randerson.hidn.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class StartUpActivity extends Activity {

	boolean firstRun = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// remove the title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// set the activity to full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
				
		// inflate the xml layout file
		setContentView(R.layout.activity_startup);
		
		ApplicationDefaults defaults = new ApplicationDefaults(this);
		
		if (defaults != null)
		{
			// check for first app run, return true if no data is set
			firstRun = defaults.getData().getBoolean("firstRun", true);
		}
		
		// check if this is the first app run, run setup if so otherwise bootup password screen
		if (firstRun)
		{
			// create the intent to start the signup activity
			Intent setupScreen = new Intent(this, SignupActivity.class);
			
			// verify the intent is valid
			if (setupScreen != null)
			{
				startActivity(setupScreen);
			}
		}
		else if (!firstRun)
		{
			// create the intent to start the signup activity
			Intent securityScreen = new Intent(this, PasswordActivity.class);
			
			// verify the intent is valid
			if (securityScreen != null)
			{
				startActivity(securityScreen);
			}
		}
	}
	
}
