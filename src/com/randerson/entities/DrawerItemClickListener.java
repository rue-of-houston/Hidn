package com.randerson.entities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.randerson.fragmentviews.BrowserActivity;
import com.randerson.fragmentviews.ContactsActivity;
import com.randerson.fragmentviews.DocumentsActivity;
import com.randerson.fragmentviews.HomeActivity;
import com.randerson.fragmentviews.NotesActivity;
import com.randerson.fragmentviews.PhotosActivity;
import com.randerson.fragmentviews.VideosActivity;
import com.randerson.hidn.R;

public class DrawerItemClickListener implements ListView.OnItemClickListener{

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
		case 0:
			// set the home screen fragment
			fragmentView = new HomeActivity();
			title = HomeActivity.TITLE;
			
			break;
		
		case 1:
			// set the contacts screen fragment
			fragmentView = new ContactsActivity();
			title = ContactsActivity.TITLE;
			
			break;
			
		case 2:
			// set the browser screen fragment
			fragmentView = new BrowserActivity();
			title = BrowserActivity.TITLE;
			
			break;
			
		case 3:
			// set the notes screen fragment
			fragmentView = new NotesActivity();
			title = NotesActivity.TITLE;
			
			break;
			
		case 4:
			// set the photos screen fragment
			fragmentView = new PhotosActivity();
			title = PhotosActivity.TITLE;
			
			break;
			
		case 5:
			// set the videos screen fragment
			fragmentView = new VideosActivity();
			title = VideosActivity.TITLE;
			
			break;
			
		case 6:
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
