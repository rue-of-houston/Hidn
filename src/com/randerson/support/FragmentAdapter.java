package com.randerson.support;

import com.randerson.fragments.BrowserActivity;
import com.randerson.fragments.ContactsActivity;
import com.randerson.fragments.DocumentsActivity;
import com.randerson.fragments.HomeActivity;
import com.randerson.fragments.NotesActivity;
import com.randerson.fragments.PhotosActivity;
import com.randerson.fragments.VideosActivity;
import com.randerson.interfaces.Constants;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class FragmentAdapter extends FragmentStatePagerAdapter implements Constants {

	FragmentManager FRAG_MAN;
	HomeActivity HOME_ACTIVITY = new HomeActivity();
	BrowserActivity BROWSER_ACTIVITY = new BrowserActivity();
	ContactsActivity CONTACTS_ACTIVITY = new ContactsActivity();
	PhotosActivity PHOTOS_ACTIVITY = new PhotosActivity();
	VideosActivity VIDEOS_ACTIVITY = new VideosActivity();
	NotesActivity NOTES_ACTIVITY = new NotesActivity();
	DocumentsActivity DOCUMENTS_ACTIVITY = new DocumentsActivity();
	
	public FragmentAdapter(FragmentManager fm) {
		super(fm);
		
		FRAG_MAN = fm;
	}
	
	@Override
	public Fragment getItem(int viewId) {
		
		// the fragment to return based on the viewId arg
		Fragment fragmentView = null;
		
		switch(viewId)
		{
		case HOME:
			// inflate the home screen
			fragmentView = HOME_ACTIVITY;
			break;
			
		case BROWSER:
			// inflate the browser screen
			fragmentView = BROWSER_ACTIVITY;
			break;
			
		case CONTACTS:
			// inflate the contacts screen
			fragmentView = CONTACTS_ACTIVITY;
			break;
				
		case PHOTOS:
			// inflate the photos screen
			fragmentView = PHOTOS_ACTIVITY;
			break;
			
		case VIDEOS:
			// inflate the videos screen
			fragmentView = VIDEOS_ACTIVITY;
			break;
			
		case NOTES:
			// inflate the notes screen
			fragmentView = NOTES_ACTIVITY;
			break;
			
		case DOCUMENTS:
			// inflate the docs screen
			fragmentView = DOCUMENTS_ACTIVITY;
			break;
			
			default:
				break;
		}
		
		FRAG_MAN.beginTransaction().commitAllowingStateLoss();
		
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
		case HOME:
			// set the title for the home tab
			title = HomeActivity.TITLE;
			break;
			
		case CONTACTS:
			//set the title for the contacts tab
			title = ContactsActivity.TITLE;
			break;
			
		case BROWSER:
			// set the title for the browser tab
			title = BrowserActivity.TITLE;
			break;
		
		case NOTES:
			// set the title for the notes tab
			title = NotesActivity.TITLE;
			break;
			
		case PHOTOS:
			// set the title for the photo tab
			title = PhotosActivity.TITLE;
			break;
			
		case VIDEOS:
			// set the title for the video tab
			title = VideosActivity.TITLE;
			break;
			
		case DOCUMENTS:
			// set the title for the docs tab
			title = DocumentsActivity.TITLE;
			break;
			
			default:
				break;
		}
		
		return title;
	}
	
	public Fragment getFragment(int fragmentId)
	{
		Fragment currentFragment = null;
		
		// set the current fragment
		switch(fragmentId)
		{
		case HOME:
			
			currentFragment = HOME_ACTIVITY;
			break;
			
		case BROWSER:
			
			currentFragment = BROWSER_ACTIVITY;
			break;
			
		case CONTACTS:
			
			currentFragment = CONTACTS_ACTIVITY;
			break;
				
		case PHOTOS:
			
			currentFragment = PHOTOS_ACTIVITY;
			break;
			
		case VIDEOS:
			
			currentFragment = VIDEOS_ACTIVITY;
			break;
			
		case NOTES:
			
			currentFragment = NOTES_ACTIVITY;
			break;
			
		case DOCUMENTS:
			
			currentFragment = DOCUMENTS_ACTIVITY;
			break;
			
			default:
				break;
		}
		
		return currentFragment;
	}
	

}
