package com.randerson.fragments;

import libs.ApplicationDefaults;
import libs.RegExManager;

import com.randerson.hidn.R;
import com.randerson.interfaces.Constants;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.interfaces.ViewHandler;
import com.randerson.support.HidNWebClient;
import com.randerson.support.ThemeMaster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		root = inflater.inflate(R.layout.activity_browser, container, false);
		
		urlField = (EditText) root.findViewById(R.id.webAddressField);
		
		// load the application settings
		loadApplicationSettings();
		
		// method for setting the actionBar
		setupActionBar();
		
		// turns on options menu in fragment
		setHasOptionsMenu(true);
		
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
		Button goBtn = (Button) root.findViewById(R.id.goBtn);
		
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
		
		return root;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public void setupActionBar() {
		
		int color = ThemeMaster.getThemeId(theme);
		
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
		
		int themeBId = ThemeMaster.getThemeId(themeB.toLowerCase());
		
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
		switch(itemId)
		{
		case R.id.browser_new_bookmark:
			
			break;
			
		case R.id.browser_view_bookmark:
			break;
			
			default:
				break;
		}
	}
	
}
