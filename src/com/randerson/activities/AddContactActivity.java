package com.randerson.activities;

import libs.ApplicationDefaults;
import libs.RegExManager;
import libs.UniArray;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.randerson.hidn.R;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.interfaces.Refresher;
import com.randerson.support.DataManager;
import com.randerson.support.ThemeMaster;

public class AddContactActivity extends Activity implements FragmentSetup, Refresher {

	public final String TITLE = "New Contact";
	public boolean defaultNavType;
	public String theme;
	public String themeB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// set the activity to full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		
		setContentView(R.layout.activity_add_contact);
		
		// set fail result code
		setResult(RESULT_CANCELED);
		
		// load the application settings
		loadApplicationSettings();
		
		Button submitButton = (Button) findViewById(R.id.newSubmitBtn);
		
		if (submitButton != null)
		{
			// set up the listeners
			submitButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v)
				{
					// try and save a new contact
					saveContact();
				}
			});
		}
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
		LinearLayout layoutBg = (LinearLayout) findViewById(R.id.addContactBg);
		
		// verify the view is valid first
		if (layoutBg != null)
		{
			layoutBg.setBackground(getResources().getDrawable(themeBId));
		}
		
		ScrollView layoutBg2 = (ScrollView) findViewById(R.id.addContactBg2);
		
		if (layoutBg2 != null)
		{
			// set the drawable for the listView bg
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
		}
		
		// method for setting the actionBar
		setupActionBar();
	}

	@Override
	public void onActionBarItemClicked(int itemId) {
		
	}
	
	@Override
	public void onBackPressed() {
		
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
		
		finish();
	}
	
	public void saveContact()
	{
		String fName = "";
		String lName = "";
		String primaryNum = "";
		String secondaryNum = "";
		String address = "";
		String email = "";
		
		EditText fNameField = (EditText) findViewById(R.id.newFirstName);
		EditText lNameField = (EditText) findViewById(R.id.newLastName);
		EditText addressField = (EditText) findViewById(R.id.newAddress);
		EditText emailField = (EditText) findViewById(R.id.newEmail);
		EditText primaryField = (EditText) findViewById(R.id.newPrimaryPhone);
		EditText secondaryField = (EditText) findViewById(R.id.newSecondaryPhone);
		
		// get the first name
		if (fNameField != null)
		{
			fName = fNameField.getText().toString();
		}
		
		// get the last name
		if (lNameField != null)
		{
			lName = lNameField.getText().toString();
		}
		
		// get the secondary phone
		if (secondaryField != null)
		{
			secondaryNum = secondaryField.getText().toString();
		}
		
		// get the primary phone
		if (primaryField != null)
		{
			primaryNum = primaryField.getText().toString();
		}
		
		// get the address
		if (addressField != null)
		{
			address = addressField.getText().toString();
		}
		
		// get the email
		if (emailField != null)
		{
			email = emailField.getText().toString();
		}
		
		
		// verify that the form data is valid and capture the result
		boolean fieldsOk = verifyData(fName, lName, email, primaryNum, secondaryNum);
		
		// check if the fieldsOk returned true for valid data
		if (fieldsOk)
		{
			// save the data
			DataManager dm = new DataManager(this);
			
			if (dm != null)
			{
				UniArray contact = null;
				String[] names = {fName, lName};
				String[] numbers = {primaryNum, secondaryNum};
				
				// create a contact object to save
				contact = dm.createContactItem(names, email, numbers, address);
				
				if (dm != null)
				{
					// save the contact
					boolean saved = dm.saveItem(DataManager.CONTACT_DATA, contact);
					
					if (saved)
					{
						// set fail result code
						setResult(RESULT_OK);
						
						// exit the activity
						onBackPressed();
					}
				}
			}
		}
		else
		{
			// create a toast to inform the user of invalid data
			Toast msg = Toast.makeText(getApplicationContext(), "1 Or More Invalid Form Fields", Toast.LENGTH_SHORT);
			
			if (msg != null)
			{
				msg.show();
			}
		}
	}
	
	public boolean verifyData(String firstName, String lastName, String email, String primaryNumber, String secondaryNumber)
	{
		boolean hasValidForms = true;
		int infractions = 0;
		
		final String VALID_NAME = "^[a-zA-Z]+";
		
		// check the firstName is valid **required**
		if (RegExManager.checkPattern(firstName, VALID_NAME) == false)
		{
			infractions++;
		}
		
		// check the lastName is valid
		if (lastName.length() > 0 && RegExManager.checkPattern(lastName, VALID_NAME) == false)
		{
			infractions++;
		}
		
		// check the email is valid
		if (email.length() > 0 && RegExManager.checkPattern(email, RegExManager.EMAIL_PATTERN) == false)
		{
			infractions++;
		}
		
		// check the primaryPhone is valid **required**
		if (RegExManager.checkPattern(primaryNumber, RegExManager.DOMESTIC_PHONE_PATTERN) == false)
		{
			infractions++;
		}
		
		// check the secondaryPhone is valid
		if (secondaryNumber.length() > 0 && RegExManager.checkPattern(secondaryNumber, RegExManager.DOMESTIC_PHONE_PATTERN) == false)
		{
			infractions++;
		}
		
		
		// check if there was any invalid formatting
		if (infractions > 0)
		{
			hasValidForms = false;
		}
		
		return hasValidForms;
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
