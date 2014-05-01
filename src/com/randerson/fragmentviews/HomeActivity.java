package com.randerson.fragmentviews;

import com.randerson.hidn.R;
import com.randerson.hidn.SettingsActivity;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.interfaces.MenuHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeActivity extends android.support.v4.app.Fragment implements FragmentSetup {

	public static final String TITLE = "Home";
	public MenuHandler parentView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.activity_home, container, false);
		
		// method for setting the actionBar
		setupActionBar();
		
		Button photoBtn = (Button) root.findViewById(R.id.photoBtn);
		Button videoBtn = (Button) root.findViewById(R.id.videoBtn);
		Button docBtn = (Button) root.findViewById(R.id.docsBtn);
		Button noteBtn = (Button) root.findViewById(R.id.noteBtn);
		Button optsBtn = (Button) root.findViewById(R.id.settingsBtn);
		
		if (optsBtn != null)
		{
			optsBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent settingsActivity = new Intent(getActivity(), SettingsActivity.class);
					
					if (settingsActivity != null)
					{
						startActivity(settingsActivity);
					}
					
				}
			});
		}
		
		if (photoBtn != null)
		{
			photoBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent photosActivity = new Intent(getActivity(), PhotosActivity.class);
					
					if (photosActivity != null)
					{
						startActivity(photosActivity);
					}
				}
			});
		}
		
		if (videoBtn != null)
		{
			videoBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent videosActivity = new Intent(getActivity(), VideosActivity.class);
					
					if (videosActivity != null)
					{
						startActivity(videosActivity);
					}
				}
			});
		}
		
		if (docBtn != null)
		{
			docBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent docsActivity = new Intent(getActivity(), DocumentsActivity.class);
					
					if (docsActivity != null)
					{
						startActivity(docsActivity);
					}
				}
			});
		}
		
		if (noteBtn != null)
		{
			noteBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent notesActivity = new Intent(getActivity(), NotesActivity.class);
					
					if (notesActivity != null)
					{
						startActivity(notesActivity);
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
			//MenuInflater mInflater = new MenuInflater(getActivity());
			//mInflater.inflate(R.menu.home_menu, menu);
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
	public void onActionBarItemClicked(int itemId) {
		// TODO Auto-generated method stub
		
	}
	
}
