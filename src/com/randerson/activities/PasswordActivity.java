package com.randerson.activities;

import com.randerson.hidn.R;

import libs.ApplicationDefaults;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordActivity extends Activity {

	Button[] KEYPAD;
	TextView PASSWORD_FIELD;
	String PASSWORD = "";
	boolean defaultNavType;
	boolean mockAccess;
	boolean privateMode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// remove the title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// set the activity to full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
				
		// inflate the xml layout file
		setContentView(R.layout.activity_password);
		
		// create the button id ref array
		int[] buttonIds = {
				R.id.num0, R.id.num1, R.id.num2, R.id.num3, R.id.num4, R.id.num5, 
				R.id.num6, R.id.num7, R.id.num8, R.id.num9, R.id.backspaceBtn, R.id.clearBtn
		};
		
		// initialize the textview
		PASSWORD_FIELD = (TextView) findViewById(R.id.passwordField);
		
		// initialize the button array
		KEYPAD = new Button[12];
		
		// iterate over the button array setting the button refs
		for (int i = 0; i < 12; i++)
		{
			KEYPAD[i] = (Button) findViewById(buttonIds[i]);
		}
		
		// set the button click listeners
		// keypad indexes 0-9 correspond to their given key number
		// indexes 10 & 11 correspond to the backspace and clear keys
		setupClickListeners();
	}
	
	// keypad click listener setup function
	public void setupClickListeners()
	{
		// verify that the button is valid
		if (KEYPAD[0] != null)
		{
			KEYPAD[0].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// verify that the password length has not reached maximum
					if (PASSWORD.length() < 6)
					{
						// append the button value to the string
						PASSWORD += "0";
						
						updateKeyField();
					}
				}
			});
		}
		
		// verify that the button is valid
		if (KEYPAD[1] != null)
		{
			KEYPAD[1].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// verify that the password length has not reached maximum
					if (PASSWORD.length() < 6)
					{
						// append the button value to the string
						PASSWORD += "1";
						
						updateKeyField();
					}
				}
			});
		}
		
		// verify that the button is valid
		if (KEYPAD[2] != null)
		{
			KEYPAD[2].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// verify that the password length has not reached maximum
					if (PASSWORD.length() < 6)
					{
						// append the button value to the string
						PASSWORD += "2";
						
						updateKeyField();
					}
				}
			});
		}
		
		// verify that the button is valid
		if (KEYPAD[3] != null)
		{
			KEYPAD[3].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// verify that the password length has not reached maximum
					if (PASSWORD.length() < 6)
					{
						// append the button value to the string
						PASSWORD += "3";
						
						updateKeyField();
					}
				}
			});
		}
		
		// verify that the button is valid
		if (KEYPAD[4] != null)
		{
			KEYPAD[4].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// verify that the password length has not reached maximum
					if (PASSWORD.length() < 6)
					{
						// append the button value to the string
						PASSWORD += "4";
						
						updateKeyField();
					}
				}
			});
		}
		
		// verify that the button is valid
		if (KEYPAD[5] != null)
		{
			KEYPAD[5].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// verify that the password length has not reached maximum
					if (PASSWORD.length() < 6)
					{
						// append the button value to the string
						PASSWORD += "5";
						
						updateKeyField();
					}
				}
			});
		}
		
		// verify that the button is valid
		if (KEYPAD[6] != null)
		{
			KEYPAD[6].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// verify that the password length has not reached maximum
					if (PASSWORD.length() < 6)
					{
						// append the button value to the string
						PASSWORD += "6";
						
						updateKeyField();
					}
				}
			});
		}
		
		// verify that the button is valid
		if (KEYPAD[7] != null)
		{
			KEYPAD[7].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// verify that the password length has not reached maximum
					if (PASSWORD.length() < 6)
					{
						// append the button value to the string
						PASSWORD += "7";
						
						updateKeyField();
					}
				}
			});
		}
		
		// verify that the button is valid
		if (KEYPAD[8] != null)
		{
			KEYPAD[8].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// verify that the password length has not reached maximum
					if (PASSWORD.length() < 6)
					{
						// append the button value to the string
						PASSWORD += "8";
						
						updateKeyField();
					}
				}
			});
		}
		
		// verify that the button is valid
		if (KEYPAD[9] != null)
		{
			KEYPAD[9].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// verify that the password length has not reached maximum
					if (PASSWORD.length() < 6)
					{
						// append the button value to the string
						PASSWORD += "9";
						
						updateKeyField();
					}
				}
			});
		}
		
		// verify that the button is valid (backspace button)
		if (KEYPAD[10] != null)
		{
			KEYPAD[10].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// verify that the password length is greater than 0
					if (PASSWORD.length() > 1)
					{
						// remove the character at the end of the string
						PASSWORD = PASSWORD.substring(0, (PASSWORD.length() - 1));
					}
					else if (PASSWORD.length() > 0)
					{
						// reset the password string
						PASSWORD = "";
					}
					
					updateKeyField();
				}
			});
		}
		
		// verify that the button is valid (clear button)
		if (KEYPAD[11] != null)
		{
			KEYPAD[11].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// verify that the password length is greater than 0
					if (PASSWORD.length() > 0)
					{
						// reset the password string
						PASSWORD = "";
						
						updateKeyField();
					}
				}
			});
		}
		
	}
	
	// password field update method
	public void updateKeyField()
	{
		ApplicationDefaults defaults = new ApplicationDefaults(this);
		
		if (defaults != null)
		{	
			// get the nav type, returns true if no data is set
			defaultNavType = defaults.getData().getBoolean("defaultNavType", true);
			
			// check if mockAccess is enabled, returns true if no data is set
			mockAccess = defaults.getData().getBoolean("mockAccess", true);
			
			// check if privateMode is enabled, returns false if no data is set
			privateMode = defaults.getData().getBoolean("privateMode", false);
		}
		
		if (PASSWORD_FIELD != null)
		{
			PASSWORD_FIELD.setText(PASSWORD);
			
			// check if the password code has reached required length
			if (PASSWORD.length() == 6)
			{
				// run method to check if password is correct
				boolean passIsValid = true;
				
				// the main activity intent
				Intent startMain = null;
				
				// check what type of navigation is to be used
				if (defaultNavType == true)
				{
					// pagerView is default nav type
					startMain = new Intent(getApplicationContext(), PagerFragmentActivity.class);
				}
				else if (defaultNavType == false)
				{
					// drawerlayout is functional nav type
					startMain = new Intent(getApplicationContext(), DrawerFragmentActivity.class);
				}
				
				// verify that the intent is valid
				if (startMain != null)
				{
					// check if private mode is enabled
					if (privateMode)
					{
						// set the flag to exclude from recent menu
						startMain.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					}
					
					if (passIsValid)
					{
						// set the password validation args to true
						startMain.putExtra("passwordIsValid", true);
						
						// start the activity
						startActivity(startMain);
					}
					else if (!passIsValid && mockAccess == true)
					{
						// set the password validation args to false
						startMain.putExtra("passwordIsValid", false);
						
						// start the activity
						startActivity(startMain);
					}
					else
					{
						// incorrect password clear field
						PASSWORD_FIELD.setText("");
						PASSWORD = "";
						
						// incorrect password prompt
						Toast msg = Toast.makeText(this, "Incorrect Pin, try again", Toast.LENGTH_SHORT);
						msg.show();
					}
					
				}
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		// reset the password data upon resuming activity
		PASSWORD_FIELD.setText("");
		PASSWORD = "";
	}
	
	@Override
	public void onBackPressed()
	{
		
	}
}
