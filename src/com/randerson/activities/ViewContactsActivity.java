package com.randerson.activities;

import libs.ApplicationDefaults;

import com.randerson.hidn.R;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.support.ThemeMaster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

@SuppressLint("DefaultLocale")
public class ViewContactsActivity extends Activity implements FragmentSetup {

	public String fName = "";
	public String lName = "";
	public String address = "";
	public String email = "";
	public String primaryPhone = "";
	public String secondaryPhone = "";
	public String theme;
	public String themeB;
	public boolean defaultNavType;
	public String TITLE = "Contact Viewer";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// set the activity to full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_view_contact);
		
		// load the application settings
		loadApplicationSettings();
		
		Intent intent = getIntent();
		
		if (intent != null)
		{
			Bundle extras = intent.getExtras();
			
			if (extras != null)
			{
				// make the data available to the intent
				fName = (String) extras.get("firstName");
				lName = (String) extras.get("lastName");
				address = (String) extras.get("address");
				email = (String) extras.get("email");
				primaryPhone = (String) extras.get("primaryPhone");
				secondaryPhone = (String) extras.get("secondaryPhone");
			}
		}
		
		// setup the assets
		EditText nameField = (EditText) findViewById(R.id.contactNameField);
		EditText addressField = (EditText) findViewById(R.id.contactAddressField);
		EditText emailField = (EditText) findViewById(R.id.contactEmailField);
		EditText primaryPhoneField = (EditText) findViewById(R.id.contactPrimaryPhoneField);
		EditText secondaryPhoneField = (EditText) findViewById(R.id.contactSecondaryPhoneField);
		
		// set the data to field
		if (nameField != null)
		{
			nameField.setText(fName + " " + lName); 
		}
		
		// set the data to field
		if (addressField != null)
		{
			addressField.setText(address); 
		}
		
		// set the data to field
		if (emailField != null)
		{
			emailField.setText(email); 
		}
		
		// set the data to field
		if (primaryPhoneField != null)
		{
			primaryPhoneField.setText(primaryPhone); 
		}
		
		// set the data to field
		if (secondaryPhoneField != null)
		{
			secondaryPhoneField.setText(secondaryPhone); 
		}
		
		// setup the buttons
		ImageView callPrimary = (ImageView) findViewById(R.id.primaryPhoneBtn);
		ImageView msgPrimary = (ImageView) findViewById(R.id.primaryMsgBtn);
		ImageView callSecondary = (ImageView) findViewById(R.id.secondaryPhoneBtn);
		ImageView msgSecondary = (ImageView) findViewById(R.id.secondaryMsgBtn);
		ImageView emailBtn = (ImageView) findViewById(R.id.emailBtn);
		ImageView addressBtn = (ImageView) findViewById(R.id.addressBtn);
		
		if (emailBtn != null)
		{
			// set click listener
			emailBtn.setOnClickListener(new View.OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					// verify the string is valid
					if (email != null && !email.equals(""))
					{
						
					}
				}
			});
		}
		
		if (addressBtn != null)
		{
			// set click listener
			addressBtn.setOnClickListener(new View.OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					// verify the string is valid
					if (address != null && !address.equals(""))
					{
						
					}
				}
			});
		}
		
		if (callSecondary != null)
		{
			// set click listener
			callSecondary.setOnClickListener(new View.OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					// verify the string is valid
					if (secondaryPhone != null && !secondaryPhone.equals(""))
					{
						
					}
				}
			});
		}
		
		if (callPrimary != null)
		{
			// set click listener
			callPrimary.setOnClickListener(new View.OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					// verify the string is valid
					if (primaryPhone != null && !primaryPhone.equals(""))
					{
						
					}
				}
			});
		}
		
		if (msgPrimary != null)
		{
			// set click listener
			msgPrimary.setOnClickListener(new View.OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					// verify the string is valid
					if (primaryPhone != null && !primaryPhone.equals(""))
					{
						
					}
				}
			});
		}
		
		if (msgSecondary != null)
		{
			// set click listener
			msgSecondary.setOnClickListener(new View.OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					// verify the string is valid
					if (secondaryPhone != null && !secondaryPhone.equals(""))
					{
						
					}
				}
			});
		}
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	@Override
	public void setupActionBar() {
		
		int color = ThemeMaster.getThemeId(theme);
		
		// set the actionBar styling
		getActionBar().setBackgroundDrawable(getResources().getDrawable(color));
		
		if (defaultNavType == false)
		{	
			// set the title to appear for the drawerlist view
			getActionBar().setTitle(TITLE);
		}
		else if (defaultNavType)
		{
			// remove the title for pagerView
			getActionBar().setTitle("");
		}
		
		int themeBId = ThemeMaster.getThemeId(themeB.toLowerCase());
		
		// set the background styling
		ScrollView layoutBg = (ScrollView) findViewById(R.id.viewContactBg);
		
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
		
		finish();
	}
	
}
