package com.randerson.fragmentviews;


import com.randerson.hidn.R;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.interfaces.MenuHandler;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class PhotosActivity extends android.support.v4.app.Fragment implements FragmentSetup {

	public static final String TITLE = "Photos";
	public MenuHandler parentView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{

		View root = inflater.inflate(R.layout.activity_photos, container, false);
		
		// method for setting the actionBar
		setupActionBar();
		
		Button addPhotoBtn = (Button) root.findViewById(R.id.addPhotoBtn);
		Button restorePhotoBtn = (Button) root.findViewById(R.id.restorePhotoBtn);
		Button trashPhotoBtn = (Button) root.findViewById(R.id.trashPhotoBtn);
		
		if (addPhotoBtn != null)
		{
			addPhotoBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast msg = Toast.makeText(getActivity(), "This Button Will Import Photos To Hide", Toast.LENGTH_SHORT);
					msg.show();
				}
			});
		}
		
		if (restorePhotoBtn != null)
		{
				restorePhotoBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast msg = Toast.makeText(getActivity(), "This Button Will Restore Photos Back To Gallery", Toast.LENGTH_SHORT);
					msg.show();
				}
			});
		}
		
		if (trashPhotoBtn != null)
		{
				trashPhotoBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast msg = Toast.makeText(getActivity(), "This Button Will Delete Photos", Toast.LENGTH_SHORT);
					msg.show();
				}
			});
		}
		
		
		String[] photoNames = new String[]{"IMG_28847373", "IMG_20183848", "IMG_1182839", "IMG_5882112"};
		
		TableLayout table = (TableLayout) root.findViewById(R.id.photoTable);
		
		if (table != null)
		{
			table.removeAllViews();
			
			for (int i = 0; i < photoNames.length; i++)
			{
				TableRow row = new TableRow(getActivity());
				
				if (row != null)
				{
					
					// create the drawable icon
					Drawable image = getResources().getDrawable(R.drawable.photo_icon);
					image.setBounds(0, 0, 100, 100);
					
					// create the text label
					TextView text = new TextView(getActivity());
					text.setText(photoNames[i]);
					
					// attach the drawable to the text
					text.setCompoundDrawables(image, null, null, null);
					
					// attach the text to the table row
					row.addView(text);
					
					// set the padding for the table row
					row.setPadding(10, 10, 0, 5);
					
					row.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Toast msg = Toast.makeText(getActivity(), "Tapping An Image Will Allow Larger Viewing", Toast.LENGTH_SHORT);
							msg.show();
						}
					});
					
					// attach the row to the table
					table.addView(row);
					
				}
				
			}
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
			mInflater.inflate(R.menu.photos_menu, menu);
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
