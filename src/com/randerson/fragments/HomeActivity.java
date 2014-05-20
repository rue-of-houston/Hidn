package com.randerson.fragments;

import libs.ApplicationDefaults;

import com.randerson.hidn.R;
import com.randerson.interfaces.Constants;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.interfaces.ViewHandler;
import com.randerson.support.ThemeMaster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

@SuppressLint("DefaultLocale")
public class HomeActivity extends android.support.v4.app.Fragment implements FragmentSetup, Constants {

	public static final String TITLE = "Home";
	public ViewHandler parentView;
	public String theme;
	public String themeB;
	public boolean defaultNavType;
	public int lastView = 0;
	public boolean loadLastView;
	public View root;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		root = inflater.inflate(R.layout.activity_home, container, false);
		
		// load the application settings
		loadApplicationSettings();
		
		// turns on options menu in fragment
		setHasOptionsMenu(true);
		
		LinearLayout layout = (LinearLayout) root.findViewById(R.id.homeContentWrapper);
		
		if (layout != null)
		{
			// set the drawable for the listView bg
			int color = ThemeMaster.getThemeId(theme)[2];
			layout.setBackgroundColor(color);
		}
		
		return root;
	}

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
		ScrollView layoutBg = (ScrollView) root.findViewById(R.id.homeBg);
		
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
			
			// load the last view vars (only for this view)
			// if the view id is not for home inflate the last view for the user
			lastView = defaults.getData().getInt("lastView", HOME);
			loadLastView = defaults.getData().getBoolean("loadLastView", false);
			
			if (lastView != HOME && loadLastView == true)
			{
				// set the app to not load the last view again upon reloading home
				defaults.set("loadLastView", false);
				
				// pass in the layout id to the fragment parent
				parentView.loadPreviousView(lastView);
			}
			else
			{
				// set the lastView id
				lastView = HOME;
				
				// save the updated lastView
				defaults.set("lastView", lastView);
			}
		}
		
		// method for setting the actionBar
		setupActionBar();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.home_menu, menu);
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
				parentView.passFragmentToParent(this, HOME);
			}
		}
	}


	@Override
	public void onActionBarItemClicked(int itemId) {
		// TODO Auto-generated method stub
		
	}
	
}
