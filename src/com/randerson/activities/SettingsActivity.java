package com.randerson.activities;

import com.randerson.hidn.R;
import com.randerson.support.ThemeMaster;

import libs.ApplicationDefaults;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Switch;

@SuppressLint("DefaultLocale")
public class SettingsActivity extends Activity {
	
	Spinner themeSpinner;
	Spinner themeSpinnerB;
	boolean mockAccess = true;
	boolean privateMode = false;
	boolean useThemes = true;
	boolean swipeNav = true;
	boolean previousNavStyle = true;
	String theme = "4_3";
	String themeB = "Dark";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// set the activity to full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	
		setContentView(R.layout.activity_settings);
		
		// load the application options
		loadAppSettings();
		
		// the theme spinner string array for adapter
		final String[] themes = new String[]{"Theme 1.1", "Theme 1.2", "Theme 1.3",
										     "Theme 2.1", "Theme 2.2", "Theme 2.3",
										     "Theme 3.1", "Theme 3.2", "Theme 3.3",
										     "Theme 4.1", "Theme 4.2", "Theme 4.3"};
		
		// the theme spinner string array for adapter
		final String[] themesB = new String[]{"Crimson", "Clay", "Pastel",
										     "Wood", "Light", "Dark"};
		
		// determine the spinner theme string and set default spinner position
		String themeStr = "Theme " + theme.replace("_", ".");
		int spinnerPosition = 0;
		
		// determine spinner theme position
		for (int i = 0; i < themes.length; i++)
		{
			if (themeStr.equals(themes[i]))
			{
				// set position on match
				spinnerPosition = i;
				
				break;
			}
		}
		
		// determine the spinnerB theme string and set default spinner position
		int spinnerPositionB = 0;
		
		// determine spinner theme position
		for (int n = 0; n < themesB.length; n++)
		{
			if (themeB.equals(themesB[n]))
			{
				// set position on match
				spinnerPositionB = n;
				
				break;
			}
		}
		
		// get a ref to the xml layout resources
		Switch mockSwitch = (Switch) findViewById(R.id.settingsMockSwitch);
		Switch navSwitch = (Switch) findViewById(R.id.settingsNavSwitch);
		Switch privateSwitch = (Switch) findViewById(R.id.settingsPrivateSwitch);
		Switch themeSwitch = (Switch) findViewById(R.id.settingsThemeSwitch);
		themeSpinner = (Spinner) findViewById(R.id.settingsSpinner);
		themeSpinnerB = (Spinner) findViewById(R.id.settingsSpinnerB);
		
		// verify the spinner is created
		if (themeSpinner != null)
		{
			// create the spinner adapter
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, themes);
			
			// verify the adapter is valid
			if (adapter !=  null)
			{
				themeSpinner.setAdapter(adapter);
				
				// set selected position
				themeSpinner.setSelection(spinnerPosition);
			}
			
			// set the spinner item selected listener
			themeSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
			{

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					
					// set the selected theme based on spinner item position
					String selectedTheme = themes[position];
					
					// set the new theme and replace the period with underscore
					theme = selectedTheme.replace(".", "_").replace("Theme ", "");

					// update the styling
					setupActionBar();
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					
				}
				
			});
		}
		
		// verify the spinner is created
		if (themeSpinnerB != null)
		{
			// create the spinner adapter
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, themesB);
			
			// verify the adapter is valid
			if (adapter !=  null)
			{
				themeSpinnerB.setAdapter(adapter);
				
				// set selected position
				themeSpinnerB.setSelection(spinnerPositionB);
			}
			
			// set the spinner item selected listener
			themeSpinnerB.setOnItemSelectedListener(new OnItemSelectedListener()
			{

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					
					// set the selected theme based on spinner item position
					themeB = themesB[position];
					
					// update the styling
					setupActionBar();
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					
				}
				
			});
		}
		
		// verify the switch is created
		if (mockSwitch != null)
		{
			// if mockAccess is true, set the switch to on
			if (mockAccess)
			{
				mockSwitch.setChecked(true);
			}
			else
			{
				mockSwitch.setChecked(false);
			}
			
			// set the button change listener
			mockSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
					// check the input state and update the boolean tracker associated with switch
					if (isChecked)
					{
						mockAccess = true;
					}
					else
					{
						mockAccess = false;
					}
				}
			});
		}
		
		// verify the switch is created
		if (privateSwitch != null)
		{
			// if privateMode is true, set the switch to on
			if (privateMode)
			{
				privateSwitch.setChecked(true);
			}
			else
			{
				privateSwitch.setChecked(false);
			}
			
			// set the button change listener
			privateSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
					// check the input state and update the boolean tracker associated with switch
					if (isChecked)
					{
						privateMode = true;
					}
					else
					{
						privateMode = false;
					}
				}
			});
		}
		
		// verify the switch is created
		if (navSwitch != null)
		{
			// if swipeNav is true, set the switch to on
			if (swipeNav)
			{
				navSwitch.setChecked(true);
			}
			else
			{
				navSwitch.setChecked(false);
			}
			
			// set the button change listener
			navSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
					// check the input state and update the boolean tracker associated with switch
					if (isChecked)
					{
						swipeNav = true;
					}
					else
					{
						swipeNav = false;
					}
				}
			});
		}
		
		// verify the switch is created
		if (themeSwitch != null)
		{
			// if useing themes is true, set the switch to on
			if (useThemes)
			{
				themeSwitch.setChecked(true);
				
				// set the spinners to be visible
				themeSpinner.setVisibility(Spinner.VISIBLE);
				themeSpinnerB.setVisibility(Spinner.VISIBLE);
			}
			else
			{
				themeSwitch.setChecked(false);
				
				// set the spinners to be invisible
				themeSpinner.setVisibility(Spinner.INVISIBLE);
				themeSpinnerB.setVisibility(Spinner.INVISIBLE);
				
				
			}
			
			// set the button change listener
			themeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
					// check the input state and update the boolean tracker associated with switch
					if (isChecked)
					{
						useThemes = true;
						
						// set the spinner to be visible
						themeSpinner.setVisibility(Spinner.VISIBLE);
						themeSpinnerB.setVisibility(Spinner.VISIBLE);
					}
					else
					{
						useThemes = false;
						
						// set the spinner to be invisible
						themeSpinner.setVisibility(Spinner.INVISIBLE);
						themeSpinnerB.setVisibility(Spinner.INVISIBLE);
						
						// reset the defaults
						theme = "4_3";
						themeB = "Dark";
						
						// update the actionbar
						setupActionBar();
					}
				}
			});
		}
	}
	
	
	public void loadAppSettings()
	{
		ApplicationDefaults defaults = new ApplicationDefaults(this);
		
		// verify the defaults object is valid
		if (defaults != null)
		{
			// load stored values and assign to app vars
			mockAccess = defaults.getData().getBoolean("mockAccess", true);
			privateMode = defaults.getData().getBoolean("privateMode", false);
			swipeNav = defaults.getData().getBoolean("defaultNavType", true);
			previousNavStyle = swipeNav;
			theme = defaults.getData().getString("theme", "4_3");
			themeB = defaults.getData().getString("themeB", "Dark");
		}
		
		// update actionbar styling
		setupActionBar();
	}
	
	public void saveAppSettings()
	{
		ApplicationDefaults defaults = new ApplicationDefaults(this);
		
		// verify the defaults object is valid
		if (defaults != null)
		{
			// save the changes (if any)
			defaults.set("mockAccess", mockAccess);
			defaults.set("privateMode", privateMode);
			defaults.set("defaultNavType", swipeNav);
			defaults.set("theme", theme);
			defaults.set("themeB", themeB);
		}
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
		
		// save changes
		saveAppSettings();
		
		finish();
	}
	
	@Override
	public void onBackPressed()
	{	
		// save app settings
		saveAppSettings();
		
		ApplicationDefaults defaults = new ApplicationDefaults(this);
		
		if (defaults != null)
		{
			// set the app to reload the last view upon restart
			defaults.set("loadLastView", true);
		}
		
		Intent navStyle = null;
		
		// create intent on navStyle that is selected
		if (swipeNav)
		{
			// pagerview swipe nav
			navStyle = new Intent(this, PagerFragmentActivity.class);
		}
		else if (!swipeNav)
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
			startActivity(navStyle);
		}
	}
	
	public void setupActionBar()
	{
		int themeId = ThemeMaster.getThemeId(theme);
		
		// set the actionBar styling
		getActionBar().setBackgroundDrawable(getResources().getDrawable(themeId));
		
		int themeBId = ThemeMaster.getThemeId(themeB.toLowerCase());
		
		// set the background styling
		ScrollView settingsBg = (ScrollView) findViewById(R.id.settingsBg);
		
		// verify the view is valid first
		if (settingsBg != null)
		{
			settingsBg.setBackground(getResources().getDrawable(themeBId));
		}
	}
	
}
