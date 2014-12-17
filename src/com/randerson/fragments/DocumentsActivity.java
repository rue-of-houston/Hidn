package com.randerson.fragments;

import java.io.File;

import libs.ApplicationDefaults;
import libs.ApplicationManager;
import libs.UniArray;

import com.randerson.activities.AddDocumentsActivity;
import com.randerson.activities.DrawerFragmentActivity;
import com.randerson.activities.PagerFragmentActivity;
import com.randerson.hidn.R;
import com.randerson.interfaces.Constants;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.interfaces.Refresher;
import com.randerson.interfaces.ViewHandler;
import com.randerson.support.ActionManager;
import com.randerson.support.DataManager;
import com.randerson.support.FileRestorer;
import com.randerson.support.ListViewAdapter;
import com.randerson.support.ThemeMaster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

@SuppressLint("DefaultLocale")
public class DocumentsActivity extends android.support.v4.app.Fragment implements FragmentSetup, Constants, Refresher {

	public static final String TITLE = "Documents";
	public ViewHandler parentView;
	public boolean defaultNavType;
	public String theme;
	public String themeB;
	public boolean privateMode = false;
	public View root;
	public String[] documentNames;
	public DataManager dataManager;
	public String[] docPaths;
	public ListView documentList;
	public boolean isDeleting = false;
	public boolean isRestoring = false;
	
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
					documentList = (ListView) root.findViewById(R.id.documentList);
					
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
								
								if (isDeleting == false && isRestoring == false)
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
										detailView.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										
										if (privateMode)
										{
											detailView.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
										}
										
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
								else
								{
									// get the textView
									TextView textView = (TextView) view.findViewById(R.id.documentListItem);
									
									if (textView != null)
									{
										
										// check if the backgrounds match as being selected
										if (textView.isSelected() == true)
										{
											// set the view as unselected
											textView.setSelected(false);
											
											// restore the color
											ActionManager.restoreBgColor(getActivity(), textView, position);
										}
										else if (textView.isSelected() == false)
										{
											// set the view as selected
											textView.setSelected(true);
											
											// set the view to be highlighted
											ActionManager.setHighlighted(getActivity(), textView);
										}
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
			
			// get the private boolean
			privateMode = defaults.getData().getBoolean("privateMode", false);
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
				if (privateMode)
				{
					importDocs.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				}
				
				startActivity(importDocs);
			}
		}
		else if (itemId == R.id.document_remove_document && parentView.hasValidPin() && isRestoring == false)
		{
			if (isDeleting)
			{
				// create an alert
				AlertDialog alert = null;
				
				// get the number of items checked
				long[] checkedItemIds = documentList.getCheckedItemIds();
				
				// verify that at least 1 item is checked
				if (checkedItemIds.length > 0)
				{
					// build the alert
					AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
					
					if (alertBuilder != null)
					{
						
						// set the builder params
						alertBuilder.setCancelable(false);
						alertBuilder.setTitle("Confirm Delete");
						
						alertBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which)
							{
								// initiate the delete
								removeItems();
								
								// dismiss the dialog
								dialog.dismiss();
							}

						});
						
						alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								// show the message
								ActionManager.showMessage(getActivity(), "Delete Mode Disabled");
								
								// put the list in multiple choice mode
								documentList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
										
								// take the list out of delete mode
								//isDeleting = false;
					
								// cancel the dialog
								dialog.cancel();
							}
						});
						
						// set the dialog alert
						alert = alertBuilder.create();
					}
					
					// show the alert
					alert.show();
				}
				else
				{
					// show the message
					ActionManager.showMessage(getActivity(), "Delete Mode Disabled");
					
					// put the list in multiple choice mode
					documentList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
							
					// take the list out of delete mode
					isDeleting = false;
				}
				
			}
			else
			{
				// put the list in multiple choice mode
				documentList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
				
				// put the list in delete mode
				isDeleting = true;
				
				ActionManager.showMessage(getActivity(), "Delete Mode Enabled");
			}
		}
		else if (itemId == R.id.document_restore_document && parentView.hasValidPin() && isDeleting == false)
		{
			if (isRestoring)
			{
				// create an alert
				AlertDialog alert = null;
				
				// get the number of items checked
				long[] checkedItemIds = documentList.getCheckedItemIds();
				
				// verify that at least 1 item is checked
				if (checkedItemIds.length > 0)
				{
					// build the alert
					AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
					
					if (alertBuilder != null)
					{
						
						// set the builder params
						alertBuilder.setCancelable(false);
						alertBuilder.setTitle("Confirm Restore");
						
						alertBuilder.setPositiveButton("Restore", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which)
							{
								// initiate the delete
								restoreItems();
								
								// dismiss the dialog
								dialog.dismiss();
							}

						});
						
						alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								// show the message
								ActionManager.showMessage(getActivity(), "Restore Mode Disabled");
								
								// put the list in multiple choice mode
								documentList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
										
								// take the list out of restore mode
								//isRestoring = false;
					
								// cancel the dialog
								dialog.cancel();
							}
						});
						
						// set the dialog alert
						alert = alertBuilder.create();
					}
					
					// show the alert
					alert.show();
				}
				else
				{
					// show the message
					ActionManager.showMessage(getActivity(), "Restore Mode Disabled");
					
					// put the list in multiple choice mode
					documentList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
							
					// take the list out of restore mode
					isRestoring = false;
				}
				
			}
			else
			{
				// put the list in multiple choice mode
				documentList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
				
				// put the list in restore mode
				isRestoring = true;
				
				ActionManager.showMessage(getActivity(), "Restore Mode Enabled");
			}
		}
		else if (parentView.hasValidPin())
		{
			// error message for double operation attempts
			
			String mode = "different";
			
			if (isDeleting)
			{
				mode = "Delete";
			}
			else if (isRestoring)
			{
				mode = "Restore";
			}
			
			ActionManager.showMessage(getActivity(), "Cannot be completed, already in " + mode + " mode");
		}
	}
	
	public void restoreItems()
	{
		boolean didRestore = false;
		long[] checkedItemIds = documentList.getCheckedItemIds();
		
		// verify that there is at least 1 item checked
		if (checkedItemIds.length > 0)
		{
			DataManager dataManager = new DataManager(getActivity());
			
			if (dataManager != null)
			{
				UniArray container = dataManager.load(DataManager.DOCUMENT_DATA);
				
				if (container != null)
				{
					// get a list of all the keys
					String[] keys = container.getAllObjectKeys();
					
					// iterate over the length of the items checked
					for (int i = 0; i < checkedItemIds.length; i++)
					{
						// set the key to the key returned for checkedItem position returned for the value of i
						int keyIndex = (int) checkedItemIds[i];
						String key = keys[keyIndex];
						
						// get the item container
						UniArray item = (UniArray) container.getObject(key);
						
						if (item != null)
						{
							// try to remove the item at the key
							FileRestorer.restoreMediaItem(getActivity(), item, DataManager.DOCUMENT_DATA);
						}
					}
				}
				
				// show the result toast
				ActionManager.showMessage(getActivity(), checkedItemIds.length + " items restored");
				
				// to refresh the parent
				didRestore = true;
			}
		}
		else
		{
			// show the result toast
			ActionManager.showMessage(getActivity(), " No items selected / restored");
		}
		
		// put the list in multiple choice mode
		documentList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				
		// take the list out of restore mode
		isRestoring = false;
		
		if (didRestore)
		{
			restartParent();
		}
	}
	
	public void removeItems()
	{
		boolean didDelete = false;
		long[] checkedItemIds = documentList.getCheckedItemIds();
		
		// verify that there is at least 1 item checked
		if (checkedItemIds.length > 0)
		{
			DataManager dataManager = new DataManager(getActivity());
			
			if (dataManager != null)
			{
				UniArray container = dataManager.load(DataManager.DOCUMENT_DATA);
				
				if (container != null)
				{
					// get a list of all the keys
					String[] keys = container.getAllObjectKeys();
					
					// iterate over the length of the items checked
					for (int i = 0; i < checkedItemIds.length; i++)
					{
						// set the key to the key returned for checkedItem position returned for the value of i
						int keyIndex = (int) checkedItemIds[i];
						String key = keys[keyIndex];
						
						// get the item container
						UniArray item = (UniArray) container.getObject(key);
						
						if (item != null)
						{
							// try to remove the item at the key
							FileRestorer.deleteMediaItem(getActivity(), item, DataManager.DOCUMENT_DATA);
						}
					}
				}
				
				// show the result toast
				ActionManager.showMessage(getActivity(), checkedItemIds.length + " items removed");
				
				// to refresh the parent
				didDelete = true;
			}
		}
		else
		{
			// show the result toast
			ActionManager.showMessage(getActivity(), " No items selected / removed");
		}
		
		// put the list in multiple choice mode
		documentList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				
		// take the list out of delete mode
		isDeleting = false;
		
		if (didDelete)
		{
			restartParent();
		}
	}
	
	@Override
	public void restartParent()
	{
		ApplicationDefaults defaults = new ApplicationDefaults(getActivity());
		
		if (defaults != null)
		{
			// set the app to reload the last view upon restart
			defaults.set("loadLastView", true);
		}
		
		Intent navStyle = null;
		
		// create intent on navStyle that is selected
		if (defaultNavType)
		{
			// pagerview swipe nav
			navStyle = new Intent(getActivity(), PagerFragmentActivity.class);
		}
		else if (!defaultNavType)
		{
			// drawerlist nav
			navStyle = new Intent(getActivity(), DrawerFragmentActivity.class);
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
			
			// set the password validation arg
			navStyle.putExtra("passwordIsValid", parentView.hasValidPin());
			
			// restart the parent
			startActivity(navStyle);
		}
	}
	
}
