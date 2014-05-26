package com.randerson.activities;

import libs.ApplicationDefaults;

import com.randerson.hidn.R;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.interfaces.Refresher;
import com.randerson.support.ActionManager;
import com.randerson.support.PhoneListener;
import com.randerson.support.ThemeMaster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

@SuppressLint("DefaultLocale")
public class ViewContactsActivity extends Activity implements FragmentSetup, Refresher {

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
	private String TEXT_MESSAGE = "";
	public AlertDialog alert;
	int phoneType = 0;  // 0 - primary; 1 - secondary
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// set the activity to full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_view_contact);
		
		// load the application settings
		loadApplicationSettings();
		
		// alert builder for building the custom rename file alert
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		if (builder != null)
		{
			// inflate the xml resource in the view
			View view =  getLayoutInflater().inflate(R.layout.text_msg_alert, null);
			
			// create the input field and button from res
			final EditText alertInputField = (EditText) view.findViewById(R.id.textContent);
			
			if (alertInputField != null)
			{
				// set the filename to appear
				alertInputField.setText(TEXT_MESSAGE);
			}
			
			// set the builder params
			builder.setCancelable(false);
			builder.setView(view);
			builder.setTitle("SMS Composer");
			
			builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which)
				{
					
					if (alertInputField != null)
					{
						// get the text field value
						String message = alertInputField.getText().toString();
						
						if (message.length() < 1)
						{
							// show the message
							ActionManager.showMessage(getApplication(), "Text can't be empty");
						}
						else if (message.length() > 0)
						{
							
							if (phoneType == 0)
							{
								// remove the hyphens from the number string
								String number = primaryPhone.replace("-", "");
								
								// try to make a call to number
								ActionManager.sendText(number, message);
							}
							else if (phoneType == 1)
							{
								// remove the hyphens from the number string
								String number = primaryPhone.replace("-", "");
								
								// try to make a call to number
								ActionManager.sendText(number, message);
							}
							
							// show the message
							ActionManager.showMessage(getApplication(), "Text sent");
							
							// dismiss the dialog
							dialog.dismiss();
						}
					}
				}

			});
			
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					if (alertInputField != null)
					{
						// set the message incase user returns
						TEXT_MESSAGE = alertInputField.getText().toString();
					}
					
					// show the message
					ActionManager.showMessage(getApplication(), "Text not sent");
		
					// cancel the dialog
					dialog.cancel();
				}
			});
			
			// create the dialog alert
			alert = builder.create();
		}
		
		// create the phone state listener
		PhoneListener pListener = new PhoneListener(this);
		
		if (pListener != null)
		{
			// get instance of telephony manager
			TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			
			// verify the telephony manager is valid
			if (telManager != null)
			{
				// register the listener
				telManager.listen(pListener, PhoneStateListener.LISTEN_CALL_STATE);
			}
		}
		
		// get the activity data
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
		
		// set the background styling
		LinearLayout contactHeader = (LinearLayout) findViewById(R.id.contactHeader);
		
		if (contactHeader != null)
		{
			// set the drawable for the border bg
			int color2 = ThemeMaster.getThemeId(theme)[2];
			contactHeader.setBackgroundColor(color2);
		}
		
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
						// try to open an app to send an email
						ActionManager.sendEmail(getApplication(), email);
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
						// open map with address
						ActionManager.launchMap(getApplication(), address);
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
						// remove the hyphens from the number string
						String number = secondaryPhone.replace("-", "");
						
						// try to make a call to number
						ActionManager.makeCall(getApplication(), number);
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
						// remove the hyphens from the number string
						String number = primaryPhone.replace("-", "");
						
						// try to make a call to number
						ActionManager.makeCall(getApplication(), number);
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
						// set the phone type
						phoneType = 0;
						
						// show the alert
						alert.show();
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
						// set the phone type
						phoneType = 1;
						
						// show the alert
						alert.show();
					}
				}
			});
		}
		
	}
	
	@Override
	public void onBackPressed() {
		
		restartParent();
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
