package com.randerson.fragments;

import libs.ApplicationDefaults;
import libs.UniArray;

import com.randerson.activities.AddNotesActivity;
import com.randerson.hidn.R;
import com.randerson.interfaces.Constants;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.interfaces.ViewHandler;
import com.randerson.support.DataManager;
import com.randerson.support.ListViewAdapter;
import com.randerson.support.ThemeMaster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

@SuppressLint("DefaultLocale")
public class NotesActivity extends android.support.v4.app.Fragment implements FragmentSetup, Constants {

	public static final String TITLE = "Notes";
	public ViewHandler parentView;
	public String theme;
	public String themeB;
	public boolean defaultNavType;
	public View root;
	public String[] noteNames;
	public String[] noteTitles;
	public DataManager dataManager;
	public String[] notePaths;
	public ListView notesList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		root = inflater.inflate(R.layout.activity_notes, container, false);
		
		// turns on options menu in fragment
		setHasOptionsMenu(true);
		
		// load the application settings
		loadApplicationSettings();
		
		if (parentView != null && parentView.hasValidPin())
		{
			
			// init the dataManager object
			dataManager = new DataManager(getActivity());
			
			if (dataManager != null)
			{
				// get the notes data object
				UniArray notes = (UniArray) dataManager.load(DataManager.NOTE_DATA);
				
				if (notes !=  null)
				{
					// get the note object keys
					noteNames = notes.getAllObjectKeys();
					
					// create an array to hold the note titles
					noteTitles = new String[noteNames.length];
					
					// iterate over the noteNames array to extract the actual note titles
					// for display inside the listview
					for (int x = 0; x < noteNames.length; x++)
					{
						UniArray item = (UniArray) notes.getObject(noteNames[x]);
						
						if (item != null)
						{
							// set the note titles
							noteTitles[x] = item.getString("title");
						}
					}

				}
			}
			
			
			// create the listview from layout file
			notesList = (ListView) root.findViewById(R.id.noteList);
			
			if (notesList != null && noteNames != null)
			{
				// create the adapter
				ListViewAdapter adapter = new ListViewAdapter(getActivity(), R.layout.note_list_item, R.id.noteListItem, noteTitles);
				
				// check if the adapter is valid
				if (adapter != null)
				{
					notesList.setAdapter(adapter);
				}
				
				// set the drawable for the listView bg
				int color = ThemeMaster.getThemeId(theme)[2];
				notesList.setBackgroundColor(color);
				
				// setup the single click listeners
				notesList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id)
					{
						
						// create the intent to launch the view/edit note activity
						Intent viewNote = new Intent(getActivity(), AddNotesActivity.class);
						
						if (viewNote != null)
						{
							// disable the passLock
							parentView.setDisablePassLock(true);
							
							// add the new note parameter to false
							viewNote.putExtra("isNew", false);
							
							// pass in the current selected note item key
							viewNote.putExtra("key", noteNames[position]);
							
							viewNote.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
							
							startActivity(viewNote);
						}
					}
				});
				
				// setup the long click listener
				notesList.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id)
					{
						
						switch(position)
						{
							case 0:
								break;
							
								default:
									break;
						}
						
						// returns false when no click event is consumed
						return false;
					}
				});
			}
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
		LinearLayout layoutBg = (LinearLayout) root.findViewById(R.id.notesBg);
		
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
		
		inflater.inflate(R.menu.notes_menu, menu);
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
				parentView.passFragmentToParent(this, NOTES);
			}
		}
	}


	@Override
	public void onActionBarItemClicked(int itemId)
	{
		
		// verify the id matches and the pin was valid
		if (itemId == R.id.notes_add_note && parentView.hasValidPin())
		{
			Intent addNote = new Intent(getActivity(), AddNotesActivity.class);
			
			if (addNote != null)
			{
				// add the new note parameter to true
				addNote.putExtra("isNew", true);
				
				addNote.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				
				startActivity(addNote);
			}
		}
	}
}
