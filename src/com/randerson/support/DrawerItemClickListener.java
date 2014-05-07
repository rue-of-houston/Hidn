package com.randerson.support;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.randerson.fragments.BrowserActivity;
import com.randerson.fragments.ContactsActivity;
import com.randerson.fragments.DocumentsActivity;
import com.randerson.fragments.HomeActivity;
import com.randerson.fragments.NotesActivity;
import com.randerson.fragments.PhotosActivity;
import com.randerson.fragments.VideosActivity;
import com.randerson.hidn.R;
import com.randerson.interfaces.Constants;

public class DrawerItemClickListener implements ListView.OnItemClickListener, Constants{

	FragmentManager Frag_Manager;
	ListView List;
	DrawerLayout Drawer;
	
	public DrawerItemClickListener(FragmentManager fm, ListView list, DrawerLayout drawer)
	{
		Frag_Manager = fm;
		List = list;
		Drawer = drawer;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		// updates the fragment view
		selectView(position);
		
		// update list and close the drawer
		List.setItemChecked(position, true);
		Drawer.closeDrawer(List);
	}

	
	public void selectView(int position)
	{
		String title = "";
		Fragment fragmentView = null;
		
		switch(position)
		{
		case HOME:
			// set the home screen fragment
			fragmentView = new HomeActivity();
			title = HomeActivity.TITLE;
			
			break;
		
		case CONTACTS:
			// set the contacts screen fragment
			fragmentView = new ContactsActivity();
			title = ContactsActivity.TITLE;
			
			break;
			
		case BROWSER:
			// set the browser screen fragment
			fragmentView = new BrowserActivity();
			title = BrowserActivity.TITLE;
			
			break;
			
		case NOTES:
			// set the notes screen fragment
			fragmentView = new NotesActivity();
			title = NotesActivity.TITLE;
			
			break;
			
		case PHOTOS:
			// set the photos screen fragment
			fragmentView = new PhotosActivity();
			title = PhotosActivity.TITLE;
			
			break;
			
		case VIDEOS:
			// set the videos screen fragment
			fragmentView = new VideosActivity();
			title = VideosActivity.TITLE;
			
			break;
			
		case DOCUMENTS:
			// set the docs screen fragment
			fragmentView = new DocumentsActivity();
			title = DocumentsActivity.TITLE;
			
			break;
			
			default:
				break;
		}
		
		if (fragmentView != null)
		{
			// set fragment args
			Bundle args = new Bundle();
			args.putString("title", title);
			fragmentView.setArguments(args);
			
			// replace the content fragment with the view selected
			Frag_Manager.beginTransaction().replace(R.id.drawerContent, fragmentView).commit();
		}
	}
	
}
