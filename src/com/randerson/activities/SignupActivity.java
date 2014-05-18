package com.randerson.activities;

import com.kinvey.java.Query;
import com.randerson.hidn.R;
import com.randerson.interfaces.Constants;
import com.randerson.interfaces.EncryptionSetup;
import com.randerson.interfaces.KinveySetup;
import com.randerson.kinvey.AccountsEntity;
import com.randerson.kinvey.KinveyManager;
import com.randerson.support.DataManager;
import com.randerson.support.HidNCipher;

import libs.ApplicationDefaults;
import libs.NetManager;
import libs.RegExManager;
import libs.UniArray;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;

@SuppressLint("HandlerLeak")
public class SignupActivity extends Activity implements Constants {
	
	public String firstName = null;
	public String lastName = null;
	public String password = null;
	public String email = null;
	public String pin = null;
	
	KinveyManager kvManager;
	Button submit;
	Switch accountSwitch;
	EditText pinField;
	EditText emailField;
	EditText firstNameField;
	EditText lastNameField;
	EditText passwordField;
	TextView firstNameTitle;
	TextView lastNameTitle;
	TextView pinTitle;
	
	boolean returningUser = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// remove the title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// set the activity to full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_signup);
		
		// instantiate the kinvey manager class
		kvManager = new KinveyManager(this);
		
		// reference the resource items
		pinField = (EditText) findViewById(R.id.pin);
		emailField = (EditText) findViewById(R.id.email);
		firstNameField = (EditText) findViewById(R.id.firstName);
		lastNameField = (EditText) findViewById(R.id.lastName);
		passwordField = (EditText) findViewById(R.id.password);
		firstNameTitle = (TextView) findViewById(R.id.firstNameTitle);
		lastNameTitle = (TextView) findViewById(R.id.lastNameTitle);
		pinTitle = (TextView) findViewById(R.id.pinTitle);
		
		accountSwitch = (Switch) findViewById(R.id.accountSwitch);
		submit = (Button) findViewById(R.id.submitBtn);
		
		// setup the button click listener if valid
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
						ApplicationDefaults defaults = new ApplicationDefaults(getApplicationContext());
						
						if (defaults != null)
						{
							// set the user data from form
							defaults.set("firstName", firstName);
							defaults.set("lastName", lastName);
							defaults.set("pin", pin);
							defaults.set("password", password);
							defaults.set("email", email);
						}
						
						// verify that the network is up
						boolean hasNetworkEnabled = NetManager.getConnectionStatus(getApplicationContext());
						
						if (hasNetworkEnabled)
						{
							// verify the kinvey manager class is valid
							if (kvManager != null)
							{
								Handler handler = new Handler()
								{
									@Override
									public void handleMessage(Message msg)
									{
										super.handleMessage(msg);
										
										if (msg != null && msg.arg1 == Activity.RESULT_OK)
										{
											// check the resultCode
											if (msg.arg2 == KinveyManager.SIGN_UP_RESULT)
											{
												// user signed up, the app needs to be setup
												setupEncryption();
											}
											else if (msg.arg2 == KinveySetup.SIGN_IN_AND_SYNC_RESULT)
											{
												// user signed in, the app data should be setup and synced
												syncData();
											}
											
										}
									}
								};
								
								// determine which action to take
								if (returningUser)
								{
									// the user has an account made previously and should be logged in and data synchronized
									kvManager.signIn(email.toLowerCase(), password.toLowerCase(), KinveySetup.SIGN_IN_AND_SYNC_RESULT, handler);
									
								}
								else
								{
									// the user is new to the app and needs a new account
									kvManager.signUp(email.toLowerCase(), password.toLowerCase(), firstName, lastName, handler);
								}
							}
						}
						else
						{
							// no network found error toast
							Toast msg = Toast.makeText(getApplicationContext(), "Network connection required", Toast.LENGTH_SHORT);
							
							if (msg != null)
							{
								msg.show();
							}
						}
					}
					else
					{
						// improper form submission error toast
						Toast msg = Toast.makeText(getApplicationContext(), "Missing fields or invalid formatting", Toast.LENGTH_SHORT);
						
						if (msg != null)
						{
							msg.show();
						}
					}
				}
			});
		}
		
		// setup the switch
		if (accountSwitch != null)
		{
			accountSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					// check the state of the switch
					// true signals a returning user, false a new user
					if (isChecked)
					{
						// hide the pin, first & last name fields
						pinField.setVisibility(EditText.GONE);
						firstNameField.setVisibility(EditText.GONE);
						lastNameField.setVisibility(EditText.GONE);
						lastNameTitle.setVisibility(EditText.GONE);
						firstNameTitle.setVisibility(EditText.GONE);
						pinTitle.setVisibility(EditText.GONE);
						
						// set the returning user boolean
						returningUser = true;
					}
					else
					{
						// show the pin, first & last name fields
						pinField.setVisibility(EditText.VISIBLE);
						firstNameField.setVisibility(EditText.VISIBLE);
						lastNameField.setVisibility(EditText.VISIBLE);
						lastNameTitle.setVisibility(EditText.VISIBLE);
						firstNameTitle.setVisibility(EditText.VISIBLE);
						pinTitle.setVisibility(EditText.VISIBLE);
						
						// set the returningUser boolean
						returningUser = false;
					}
				}
			});
		}
	}
	
	@Override
	public void onBackPressed() {
		
	}
	
	
	// method for validating form fields
	public boolean validateForm()
	{
		// integer for tracking the number of errors
		int infractions = 0;
		
		// boolean for returning result of form validation
		boolean isValid = true;
	
		// get the edit text fields data
		if (emailField != null)
		{
			email = emailField.getText().toString();
		}
		
		// get the edit text fields data
		if (passwordField != null)
		{
			password = passwordField.getText().toString();
		}
		
		// get the edit text fields data
		if (pinField != null)
		{
			pin = pinField.getText().toString();
		}
		
		// get the edit text fields data
		if (firstNameField != null)
		{
			firstName = firstNameField.getText().toString();
		}
		
		// get the edit text fields data
		if (lastNameField != null)
		{
			lastName = lastNameField.getText().toString();
		}
		
		// check against reg expressions
		if (RegExManager.checkPattern(email, RegExManager.EMAIL_PATTERN) == false)
		{
			infractions++;
		}
		
		// check against reg expressions
		if (RegExManager.checkPattern(password, "^[a-zA-Z0-9!$?]{6,16}") == false)
		{
			infractions++;
		}
		
		// only consider these fields if a new user is signing up
		if (!returningUser)
		{
			// check against reg expressions
			if (RegExManager.checkPattern(pin, "^[0-9]{6,6}") == false)
			{
				infractions++;
			}
			
			// check against reg expressions
			if (RegExManager.checkPattern(firstName, "^[a-zA-Z]{1,16}") == false)
			{
				infractions++;
			}
			
			// check against reg expressions
			if (RegExManager.checkPattern(lastName, "^[a-zA-Z]{1,16}") == false)
			{
				infractions++;
			}
			
			
			// check if there were any errors
			// if so, invalidate the form
			if (infractions > 0)
			{
				isValid = false;
			}
		}
		
		return isValid;
	}
	
	public void setupEncryption()
	{
		// create the encryption keys by instantiating an HidNCipher
		new HidNCipher(this, EncryptionSetup.AES_ALGORITHM);
		
		// init the storage managing class
		DataManager keyMaster = new DataManager(this);
		
		if (keyMaster != null)
		{
			// load the data container for the encryption keys
			UniArray keyData = (UniArray) keyMaster.load(DataManager.ENCRYPTION_DATA).getObject(DataManager.ENCRYPTION_KEY);
			
			if (keyData != null)
			{
				// load the secret key data that was created and saved
				String encodedKey = (String) keyData.getObject(DataManager.SECRET_KEY);
				
				if (encodedKey != null)
				{
					// create an instance of the accounts entity
					AccountsEntity account = new AccountsEntity();
					
					if (account != null)
					{
						// set the data to the entity class
						account.firstName = firstName;
						account.lastName = lastName;
						account.id = email;
						account.pin = pin;
						account.secretKey = encodedKey;
						
						
						// push the data to the backend
						kvManager.updateAccountData(account, new Handler()
						{
							@Override
							public void handleMessage(Message msg) {
								super.handleMessage(msg);
								
								if (msg != null)
								{
									if (msg.arg2 == KinveySetup.UPDATING_ACCOUNT)
									{
										// create an intent to start security activity
										Intent securityScreen = new Intent(getApplicationContext(), PasswordActivity.class);
										
										if (securityScreen != null)
										{
											startActivity(securityScreen);
										}
									}
								}
							}
						});
					}
				}
				
			}
		}
	}
	
	public void syncData()
	{
		// init the storage managing class
		final DataManager keyMaster = new DataManager(this);
		
		if (keyMaster != null)
		{
			
				// create a new query object to retrieve user data
				Query query = kvManager.createQuery("_id", email.toLowerCase());
				
				// get the account data for the user
				// since an array is returned the first index is what is required
				kvManager.getAccountData(query, new Handler()
				{
					@Override
					public void handleMessage(Message msg) 
					{
						super.handleMessage(msg);
						
						if (msg != null)
						{
							if (msg.arg2 == KinveySetup.FETCHING_ACCOUNT)
							{
								AccountsEntity[] account = (AccountsEntity[]) msg.obj;
								
								if (account != null)
								{
								
									// create the uniarray of key value pairs
									UniArray keyItems = null;
									
									// get the encoded keys object
									 String encodedKey = account[0].secretKey;
									 
									ApplicationDefaults defaults = new ApplicationDefaults(getApplicationContext());
									
									if (defaults != null)
									{
										// get and set the user data
										defaults.set("firstName", account[0].firstName);
										defaults.set("lastName", account[0].lastName);
										defaults.set("pin", account[0].pin);
									}
									
									// set the keyItems
									keyItems = keyMaster.createKeysItem(DataManager.SECRET_KEY, encodedKey);
									
									if (keyItems != null)
									{
										// save the downloaded keys
										keyMaster.saveItem(DataManager.ENCRYPTION_DATA, keyItems);
										
										// create an intent to start security activity
										Intent securityScreen = new Intent(getApplicationContext(), PasswordActivity.class);
										
											if (securityScreen != null)
											{
												startActivity(securityScreen);
											}
										}
								}
							}
						}
					}
				});
		}
	}
	
}
