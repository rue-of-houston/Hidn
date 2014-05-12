package com.randerson.activities;

import com.randerson.hidn.R;
import com.randerson.interfaces.Constants;
import com.randerson.interfaces.DataSetup;

import libs.ApplicationDefaults;
import libs.FileSystem;
import libs.UniArray;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class SignupActivity extends Activity implements Constants {

	public Button submit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// remove the title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// set the activity to full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_signup);
		
		submit = (Button) findViewById(R.id.submitBtn);
		
		if (submit != null)
		{
			submit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// check if all fields are valid
					boolean isValid = validateForm();
					
					// verify that the forms are valid
					if (isValid)
					{
						// create intent to start security activity
						Intent securityScreen = new Intent(getApplicationContext(), PasswordActivity.class);
						
						if (securityScreen != null)
						{
							startActivity(securityScreen);
						}
					}
				}
			});
		}
	}
	
	@Override
	public void onBackPressed() {
		
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
			defaults.set("defaultNavType", true);
			
			// set the mock access setting
			defaults.set("mockAccess", true);
			
			// set the private mode setting
			defaults.set("privateMode", false);
			
			// set the lastView setting
			defaults.set("lastView", HOME);
			
			// set last view reloading setting
			defaults.set("loadLastView", false);
			
		}
		
		// create the top-level UniArray
		UniArray APP_DATA = new UniArray();
		
		// the 1st level UniArray keys
		String[] CONTAINER_NAMES = new String[]{ DataSetup.BROWSER_KEY, DataSetup.CONTACT_KEY,
											   	 DataSetup.DOCUMENT_KEY, DataSetup.ENCRYPTION_KEY,
											   	 DataSetup.NOTE_KEY, DataSetup.PHOTO_KEY, DataSetup.VIDEO_KEY
												};
		
		// iterate over the container names creating and adding them to the save file
		for (String key : CONTAINER_NAMES)
		{
			UniArray container = new UniArray();
			
			APP_DATA.putObject(key, container);
		}
		
		// create the initial save file
		FileSystem.writeObjectFile(getApplicationContext(), APP_DATA, DataSetup.APP_DATA_FILENAME, true);
	}
	
	// method for validating form fields
	public boolean validateForm()
	{
		// setup application settings
		setupApp();
		
		return true;
	}
	
}
