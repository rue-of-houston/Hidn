package com.randerson.fragments;

import libs.ApplicationDefaults;
import libs.RegExManager;
import libs.UniArray;

import com.randerson.activities.ViewBookmarks;
import com.randerson.hidn.R;
import com.randerson.interfaces.Constants;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.interfaces.ViewHandler;
import com.randerson.support.DataManager;
import com.randerson.support.HidNWebClient;
import com.randerson.support.ThemeMaster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

@SuppressLint({ "SetJavaScriptEnabled", "DefaultLocale" })
public class BrowserActivity extends android.support.v4.app.Fragment implements FragmentSetup, Constants {

	public static final String TITLE = "Browser";
	public ViewHandler parentView;
	public boolean defaultNavType;
	public String theme;
	public String themeB;
	public View root;
	public EditText urlField;
	public AlertDialog alertDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		root = inflater.inflate(R.layout.activity_browser, container, false);
	
		// turns on options menu in fragment
		setHasOptionsMenu(true);
		
		// load the application settings
		loadApplicationSettings();
			
		if (parentView != null && parentView.hasValidPin())
		{
			urlField = (EditText) root.findViewById(R.id.webAddressField);
			
			LinearLayout browserBar = (LinearLayout) root.findViewById(R.id.browserBar);
			
			if (browserBar != null)
			{
				// set the drawable for the layout bg
				int color = ThemeMaster.getThemeId(theme)[2];
				browserBar.setBackgroundColor(color);
			}
			
			if (urlField != null)
			{
				urlField.setOnFocusChangeListener(new OnFocusChangeListener() {
					
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						
						if (hasFocus)
						{
							// clear the text
							urlField.setText("https://");
						}
					}
				});
			}
			
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
			
			if (alertBuilder != null)
			{
				
				// inflate the xml resource in the view
				View view = inflater.inflate(R.layout.browser_alert_dialog, null);
				
				// create the input field and button from res
				final EditText alertInputField = (EditText) view.findViewById(R.id.alertField);
				
				// set the builder params
				alertBuilder.setCancelable(false);
				alertBuilder.setView(view);
				alertBuilder.setTitle("New Bookmark");
				
				alertBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which)
					{
						if (alertInputField != null && urlField != null)
						{
							// get the text field value
							String name = alertInputField.getText().toString();
							String url = urlField.getText().toString();
							
							if (name.length() >= 3)
							{
								// set the field to the bookmark
								addBookmark(name, url);
								
								// show the message
								showMessage("Bookmark added");
								
								// dismiss the dialog
								dialog.dismiss();
							}
							else
							{
								// show the message
								showMessage("Bookmark must be at least 3 characters long");
							}
						}
					}

				});
				
				alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						// show the message
						showMessage("Bookmark not added");
			
						// cancel the dialog
						dialog.cancel();
					}
				});
				
				// create the dialog alert
				alertDialog = alertBuilder.create();
			}
			
			// create webview from layout xml
			final WebView browser = (WebView) root.findViewById(R.id.browser);
			
			// verify the webview is created properly
			if (browser != null)
			{
				// create the webClient and pass a reference to the url field 
				HidNWebClient webClient = new HidNWebClient(urlField);
				
				// set the webview to allow the use of javascript
				WebSettings settings = browser.getSettings();
				settings.setJavaScriptEnabled(true);
				
				browser.setWebViewClient(webClient);
			}
			
			// get the button from xml ref
			ImageView goBtn = (ImageView) root.findViewById(R.id.goBtn);
			
			// verify the button is created and setup the click listeners
			if (goBtn != null)
			{
				goBtn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						if (urlField != null)
						{
							String URL = urlField.getText().toString();
							
							if (URL != null && URL != "")
							{
								if (RegExManager.checkPattern(URL, RegExManager.URL_PATTERN))
								{
									// the url is fully qualified and ready to be loaded
									browser.loadUrl(URL);
								}
								else if (RegExManager.checkPattern(URL, RegExManager.IMPROPER_URL_PATTERN))
								{
									// the url seems valid but is not proper url, try prefixing 'http://'
									URL = "http://" + URL;
									
									if (RegExManager.checkPattern(URL, RegExManager.URL_PATTERN))
									{
										// the url is NOW fully qualified and ready to be loaded
										browser.loadUrl(URL);
									}
									else
									{
										Toast msg = Toast.makeText(getActivity(), "Improper URL or formatting", Toast.LENGTH_SHORT);
										
										// show the message if the toast is valid
										if (msg != null)
										{
											msg.show();
										}
									}
								}
								else
								{
									Toast msg = Toast.makeText(getActivity(), "Improper URL or formatting", Toast.LENGTH_SHORT);
									
									// show the message if the toast is valid
									if (msg != null)
									{
										msg.show();
									}
								}
								
							}
						}
						
					}
				});
			}
		}
		
		return root;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public void setupActionBar() {
		
		int color = ThemeMaster.getThemeId(theme)[0];
		
		// set the actionBar styling
		getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(color));
		
		if (defaultNavType == false)
		{	
			// set the title to appear for the drawerlist view
			getActivity().getActionBar().setTitle(TITLE);
		}
		else if (defaultNavType)
		{
			// remove the title for pagerView
			getActivity().getActionBar().setTitle("");
		}
		
		int themeBId = ThemeMaster.getThemeId(themeB.toLowerCase())[0];
		
		// set the background styling
		LinearLayout layoutBg = (LinearLayout) root.findViewById(R.id.browserBg);
		
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
		ApplicationDefaults defaults = new ApplicationDefaults(getActivity());
		
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
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.browser_menu, menu);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		if (activity instanceof ViewHandler)
		{
			parentView = (ViewHandler) activity;
			
			if (parentView != null)
			{
				// pass a reference to this fragment to the parent activity
				parentView.passFragmentToParent(this, BROWSER);
			}
		}
	}

	@Override
	public void onActionBarItemClicked(int itemId)
	{
		// verify the id matches and the pin was valid
		if (parentView.hasValidPin())
		{
			if (itemId == R.id.browser_new_bookmark)
			{
				if (alertDialog != null)
				{
					// show the dialog
					alertDialog.show();
				}
			}
			else if (itemId == R.id.browser_view_bookmark)
			{
				// create the intent to launch the bookmark activity
				Intent viewBookmarks = new Intent(getActivity(), ViewBookmarks.class);
				
				if (viewBookmarks != null)
				{
					viewBookmarks.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					
					// start the bookmark viewing activity
					startActivityForResult(viewBookmarks, 100);
				}
			}
		}
	}
	
	// method for showing a message
	public void showMessage(String text)
	{
		Toast msg = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
		
		if (msg !=  null)
		{
			msg.show();
		}
	}
	
public void addBookmark(String name, String bookmark)
	{
		DataManager dataManager = new DataManager(getActivity());
		
		if (dataManager != null)
		{
			
			// create a new bookmark item
			UniArray bookmarkItem = dataManager.createBookmarkItem(name, bookmark);
			
			if (bookmarkItem != null)
			{
				// add the bookmark
				dataManager.saveItem(DataManager.BROWSER_DATA, bookmarkItem);
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		// verify the result and request codes
		if (requestCode == 100 && resultCode == Activity.RESULT_OK)
		{
			// check for non null data object
			if (data != null)
			{
				Bundle extras = data.getExtras();
				
				if (extras != null)
				{
					// get the returned url
					String url = extras.getString("url");
					
					if (url != null)
					{
						// set the urlField to the url
						urlField.setText(url);
					}
				}
			}
		}
	}
	
}
