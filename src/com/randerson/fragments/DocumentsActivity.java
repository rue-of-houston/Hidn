package com.randerson.fragments;

import java.io.File;
import libs.ApplicationDefaults;
import libs.ApplicationManager;
import libs.UniArray;

import com.randerson.activities.AddDocumentsActivity;
import com.randerson.hidn.R;
import com.randerson.interfaces.Constants;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.interfaces.ViewHandler;
import com.randerson.support.ActionManager;
import com.randerson.support.DataManager;
import com.randerson.support.ListViewAdapter;
import com.randerson.support.ThemeMaster;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

@SuppressLint("DefaultLocale")
public class DocumentsActivity extends android.support.v4.app.Fragment implements FragmentSetup, Constants {

	public static final String TITLE = "Documents";
	public ViewHandler parentView;
	public boolean defaultNavType;
	public String theme;
	public String themeB;
	public View root;
	public String[] documentNames;
	public DataManager dataManager;
	public String[] docPaths;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		root = inflater.inflate(R.layout.activity_documents, container, false);
		
		new Thread(new Runnable() {
			
			@Override
			public void run()
			{
				// turns on options menu in fragment
				setHasOptionsMenu(true);

				// load the application settings
				loadApplicationSettings();
				
				if (parentView != null && parentView.hasValidPin())
				{
					
					// initialize the object
					dataManager = new DataManager(getActivity());
					
					if (dataManager != null)
					{
						// get the documents data object
						UniArray documents = (UniArray) dataManager.load(DataManager.DOCUMENT_DATA);
						
						if (documents !=  null)
						{
							// get the document object keys
							documentNames = documents.getAllObjectKeys();

						}
					}
					
					// create the listview from ref xml
					ListView documentList = (ListView) root.findViewById(R.id.documentList);
					
					if (documentList != null && documentNames != null)
					{
						// create the adapter
						ListViewAdapter adapter = new ListViewAdapter(getActivity(), R.layout.document_list_item, R.id.documentListItem, documentNames);
						
						// check if the adapter is valid
						if (adapter != null)
						{
							documentList.setAdapter(adapter);
						}
						
						// set the drawable for the listView bg
						int color = ThemeMaster.getThemeId(theme)[2];
						documentList.setBackgroundColor(color);
						
						// setup the single click listeners
						documentList.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view,
									int position, long id)
							{
								
								// create the intent to launch the detail view activity and the bundle for passing
								// the activity details upon loading
								Intent detailView = new Intent(Intent.ACTION_VIEW);
								
								// the path for the file
								String path = null;
								
								// verify the intent is valid, if so pass in the args and load it up
								if (detailView != null)
								{
									if (dataManager != null)
									{
										// get the documents data object
										UniArray documents = (UniArray) dataManager.load(DataManager.DOCUMENT_DATA);
										
										if (documents !=  null)
										{
											// get the selected document
											UniArray doc = (UniArray) documents.getObject(documentNames[position]);
											
											if (doc != null)
											{
												// get the document path
												path = doc.getString("hidnPath");
											}

										}
									}
									
									// disable the passLock
									parentView.setDisablePassLock(true);
									
									// set the intent params
									detailView.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
									detailView.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									
									// try and guess the doc mime type
									String extension = MimeTypeMap.getFileExtensionFromUrl(path);
									String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
									
									if (mime != null)
									{
										// set the data and mime type
										detailView.setDataAndTypeAndNormalize(Uri.fromFile(new File(path)), mime);
									}
									else
									{
										// set only the data uri
										detailView.setDataAndNormalize(Uri.fromFile(new File(path)));
										
									}
									
									// check for an activity to handle the dat
									boolean result = ApplicationManager.verifyIntent(getActivity(), detailView);
									
									if (result)
									{
										// start the activity
										startActivity(Intent.createChooser(detailView, "Choose App To View Document"));
									}
									else
									{
										// show error message
										ActionManager.showMessage(getActivity(), "No Activity Found to View Document");
									}
								}
							}
						});
						
						// setup the long click listener
						documentList.setOnItemLongClickListener(new OnItemLongClickListener() {

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
				
			}
		}).run();
		
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
		LinearLayout layoutBg = (LinearLayout) root.findViewById(R.id.documentsBg);
		
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
		
		inflater.inflate(R.menu.documents_menu, menu);
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
				parentView.passFragmentToParent(this, DOCUMENTS);
			}
		}
	}


	@Override
	public void onActionBarItemClicked(int itemId)
	{
		// verify the id matches and the pin was valid
		if (itemId == R.id.documents_add_doc && parentView.hasValidPin())
		{
			Intent importDocs = new Intent(getActivity(), AddDocumentsActivity.class);
			
			if (importDocs != null)
			{
				startActivity(importDocs);
			}
		}
	}
	
}
