package com.randerson.entities;

import com.randerson.fragmentviews.BrowserActivity;
import com.randerson.fragmentviews.ContactsActivity;
import com.randerson.fragmentviews.DocumentsActivity;
import com.randerson.fragmentviews.HomeActivity;
import com.randerson.fragmentviews.NotesActivity;
import com.randerson.fragmentviews.PhotosActivity;
import com.randerson.fragmentviews.VideosActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class FragmentAdapter extends FragmentStatePagerAdapter{

	public FragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int viewId) {
		
		// the fragment to return based onthe viewId arg
		Fragment fragmentView = null;
		
		switch(viewId)
		{
		case 0:
			// inflate the home screen
			fragmentView = new HomeActivity();
			break;
		
		case 1:
			// inflate the contacts screen
			fragmentView = new ContactsActivity();
			break;
			
		case 2:
			// inflate the browser screen
			fragmentView = new BrowserActivity();
			break;
			
		case 3:
			// inflate the notes screen
			fragmentView = new NotesActivity();
			break;
			
		case 4:
			// inflate the photos screen
			fragmentView = new PhotosActivity();
			break;
			
		case 5:
			// inflate the videos screen
			fragmentView = new VideosActivity();
			break;
			
		case 6:
			// inflate the docs screen
			fragmentView = new DocumentsActivity();
			break;
			
			default:
				break;
		}
		
		return fragmentView;
	}

	@Override
	public int getCount() {
		
		// the number of pages in pagerview
		return 7;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		
		// the tab title string to be returned based on the position arg
		String title = "";
		
		switch(position)
		{
		case 0:
			// set the title for the home tab
			title = HomeActivity.TITLE;
			break;
			
		case 1:
			//set the title for the contacts tab
			title = ContactsActivity.TITLE;
			break;
			
		case 2:
			// set the title for the browser tab
			title = BrowserActivity.TITLE;
			break;
		
		case 3:
			// set the title for the notes tab
			title = NotesActivity.TITLE;
			break;
			
		case 4:
			// set the title for the photo tab
			title = PhotosActivity.TITLE;
			break;
			
		case 5:
			// set the title for the video tab
			title = VideosActivity.TITLE;
			break;
			
		case 6:
			// set the title for the docs tab
			title = DocumentsActivity.TITLE;
			break;
			
			default:
				break;
		}
		
		return title;
	}

}
