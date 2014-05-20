package com.randerson.activities;

import libs.ApplicationDefaults;
import libs.RegExManager;
import libs.UniArray;

import com.randerson.hidn.R;
import com.randerson.interfaces.EncryptionSetup;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.support.DataManager;
import com.randerson.support.HidNCipher;
import com.randerson.support.ThemeMaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class AddNotesActivity extends Activity implements FragmentSetup {

	public String TITLE = "Note Viewer";
	public String theme;
	public String themeB;
	public boolean defaultNavType;
	boolean isNewNote = false;
	String key = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		// set the activity to full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		
		setContentView(R.layout.activity_add_note);
		
		// get the activity's arguments
		Intent data = getIntent();
		
		if (data != null)
		{
			// get the extras and verify that the extras are not null
			Bundle extras = data.getExtras();
			
			if (extras != null)
			{
				// set the isNewNote bool, the value supplied 
				// depends on how this activity was called
				isNewNote = extras.getBoolean("isNew");
				
				if (isNewNote)
				{
					// if this is a new note set a new title
					TITLE = "Note Composer";
				}
				else
				{
					// set the instructions
					TextView noteInstructions = (TextView) findViewById(R.id.noteInstructions);
					
					if (noteInstructions != null)
					{
						noteInstructions.setText("You can edit your note here. A title is required to save.");
					}
				}
				
				if (extras.containsKey("key"))
				{
					// get the key for the current note being viewed
					key = extras.getString("key");
				}
			}
		}
		
		// load the application settings
		loadApplicationSettings();
		
		// create the done button from layout res
		Button doneBtn = (Button) findViewById(R.id.noteSubmitBtn);
		
		// create the editText from layout res
		final EditText noteTitle = (EditText) findViewById(R.id.noteTitle);
		final EditText textArea = (EditText) findViewById(R.id.noteContent);
		
		if (isNewNote == false)
		{
			// get instance of the data manager
			DataManager dataManager = new DataManager(getApplicationContext());
			
			if (dataManager != null)
			{
				// retrieve the notes data object
				UniArray notes = dataManager.load(DataManager.NOTE_DATA);
				
				if (notes != null)
				{
					// get the individual note item
					UniArray note = (UniArray) notes.getObject(key);
					
					if (note != null)
					{
						// get the note data
						String title =  note.getString("title");
						String codedContent = note.getString("encodedContent");
						
						if (title != null)
						{
							noteTitle.setText(title);
						}
						
						if (codedContent != null)
						{
							// get an instance of the cipher
							HidNCipher cipher = new HidNCipher(this, EncryptionSetup.AES_ALGORITHM);
							
							if (cipher != null)
							{
								byte[] noteData = cipher.decodeData(codedContent);
								
								if (noteData != null)
								{
									// create the content string out of the data bytes array
									String content = new String(noteData);
									
									// set the content
									textArea.setText(content);
								}
							}
						}
					}
				}
			}
		}
		
		
		if (doneBtn != null)
		{
			// setup the click events
			doneBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) 
				{
					// set the note properties
					String note = null;
					String title = null;
					
					if (textArea != null)
					{
						// capture the note text
						note = textArea.getText().toString();
						
						// check for null
						if (note == null)
						{
							note = "";
						}
					}
					
					if (noteTitle != null)
					{
						// capture the note title
						title = noteTitle.getText().toString();
						
						// check for null
						if (title == null)
						{
							title = "";
						}
					}
					
					// check for valid note
					if (title.length() >= 3 && RegExManager.checkPattern(title, "^[a-zA-Z0-9]{3,}"))
					{
						DataManager dataManager = new DataManager(getApplicationContext());
						HidNCipher cipher = new HidNCipher(getApplicationContext(), EncryptionSetup.AES_ALGORITHM);
						
						if (dataManager != null && cipher != null)
						{
							// encode the note
							String encodedContent = cipher.encodeData(note.getBytes());
							
							// create a new note item
							UniArray noteItem = dataManager.createNoteItem(title, encodedContent);
							
							if (noteItem != null)
							{
								// save the item
								boolean success = dataManager.saveItem(DataManager.NOTE_DATA, noteItem);
								
								if (success)
								{
									showMessage("Note Saved");
									
									if (isNewNote == false)
									{
										// verify that the object is valid
										if (dataManager != null)
										{
											// remove the old item
											boolean didDelete = dataManager.removeItem(DataManager.NOTE_DATA, key);
											
											// log a result message
											if (didDelete)
											{
												Log.i("Note Updated", "Removed previous item for key: " + key);
											}
											else
											{
												Log.i("Note Updated", "Item for key: " + key + " failed to remove");
											}
										}
									}
									
									// close the activity
									finish();
								}
							}
						}
					}
					else
					{
						// display error msg
						showMessage("No Title or invalid format");
					}
				}
			});
		}
	}
	
	
	public void showMessage(String message)
	{
		Toast msg = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
		
		if (msg != null)
		{
			msg.show();
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
		ScrollView layoutBg = (ScrollView) findViewById(R.id.viewNoteBg);
		
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
