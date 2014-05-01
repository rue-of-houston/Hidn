package com.randerson.fragmentviews;

import com.randerson.entities.HidNWebClient;
import com.randerson.hidn.R;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.interfaces.MenuHandler;

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
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class BrowserActivity extends android.support.v4.app.Fragment implements FragmentSetup {

	public static final String TITLE = "Browser";
	MenuHandler parentView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		final View root = inflater.inflate(R.layout.activity_browser, container, false);
		
		// method for setting the actionBar
		setupActionBar();
		
		final WebView browser = (WebView) root.findViewById(R.id.browser);
		
		if (browser != null)
		{
			HidNWebClient webClient = new HidNWebClient();
			
			// set the webview to allow the use of javascript
			WebSettings settings = browser.getSettings();
			settings.setJavaScriptEnabled(true);
			
			browser.setWebViewClient(webClient);
		}
		
		
		Button bookmarkBtn = (Button) root.findViewById(R.id.bookmarksBtn);
		Button goBtn = (Button) root.findViewById(R.id.goBtn);
		
		if (goBtn != null)
		{
			goBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					EditText urlField = (EditText) root.findViewById(R.id.webAddressField);
					
					if (urlField != null)
					{
						String URL = urlField.getText().toString();
						
						if (URL != null && URL != "")
						{
							browser.loadUrl(URL);
						}
					}
					
				}
			});
		}
		
		if (bookmarkBtn != null)
		{
			bookmarkBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast msg = Toast.makeText(getActivity(), "This Button Will Add / Manage Bookamrks", Toast.LENGTH_SHORT);
					
					if (msg != null)
					{
						msg.show();
					}
				}
			});
		}
		
		return root;
	}

	@Override
	public void setupActionBar() {
		
		if (parentView != null)
		{
			// get the parent menu
			Menu menu = parentView.getParentMenu(this);
			
			// setup the menu options
			MenuInflater mInflater = new MenuInflater(getActivity());
			mInflater.inflate(R.menu.browser_menu, menu);
		}
		
		// set the actionBar styling
		getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.theme_1_3));
		
	}
	
	@Override
	public void loadApplicationSettings() {
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		if (activity instanceof MenuHandler)
		{
			parentView = (MenuHandler) activity;
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
