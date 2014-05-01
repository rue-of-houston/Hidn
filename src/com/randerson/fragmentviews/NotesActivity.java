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

public class NotesActivity extends android.support.v4.app.Fragment implements FragmentSetup {

	public static final String TITLE = "Notes";
	public MenuHandler parentView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View root = inflater.inflate(R.layout.activity_notes, container, false);
		
		// method for setting the actionBar
		setupActionBar();
		
		Button addNoteBtn = (Button) root.findViewById(R.id.addNoteBtn);
		Button trashNoteBtn = (Button) root.findViewById(R.id.trashNoteBtn);
		
		if (addNoteBtn != null)
		{
			addNoteBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast msg = Toast.makeText(getActivity(), "This Button Will Allow For Creating New Notes", Toast.LENGTH_SHORT);
					msg.show();
				}
			});
		}
		
		if (trashNoteBtn != null)
		{
				trashNoteBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast msg = Toast.makeText(getActivity(), "This Button Will Delete Notes", Toast.LENGTH_SHORT);
					msg.show();
				}
			});
		}
		
		
		String[] notesNames = new String[]{"Camping Supplies", "Note to Self", "Regimen", "Recipies"};
		
		TableLayout table = (TableLayout) root.findViewById(R.id.noteTable);
		
		if (table != null)
		{
			table.removeAllViews();
			
			for (int i = 0; i < notesNames.length; i++)
			{
				TableRow row = new TableRow(getActivity());
				
				if (row != null)
				{
					
					// create the drawable icon
					Drawable image = getResources().getDrawable(R.drawable.note_icon);
					image.setBounds(0, 0, 100, 100);
					
					// create the text label
					TextView text = new TextView(getActivity());
					text.setText(notesNames[i]);
					
					// attach the drawable to the text
					text.setCompoundDrawables(image, null, null, null);
					
					// attach the text to the table row
					row.addView(text);
					
					// set the padding for the table row
					row.setPadding(10, 10, 0, 5);
					
					row.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Toast msg = Toast.makeText(getActivity(), "Tapping A Note Will Allow For Viewing Details", Toast.LENGTH_SHORT);
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
			mInflater.inflate(R.menu.notes_menu, menu);
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
