package com.randerson.hidn;

import libs.ApplicationDefaults;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SignupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// remove the title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// set the activity to full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		
		setContentView(R.layout.activity_signup);
		
		// setup application settings
		//setupApp();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	public void setupApp()
	{
		ApplicationDefaults defaults = new ApplicationDefaults(this);
		
		if (defaults != null)
		{
			// set the first run boolean to false now that setup has taken place
			defaults.set("firstRun", false);
			
			// set the default theme style
			defaults.set("theme", "1_1");
			
			// set the default nav tyle
			defaults.set("defaultNavType", true);
			
			// set the mock access setting
			defaults.set("mockAccess", true);
			
			// set the private mode setting
			defaults.set("privateMode", false);
			
		}
	}
	
}
