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

public class DocumentsActivity extends android.support.v4.app.Fragment implements FragmentSetup {

	public static final String TITLE = "Documents";
	public MenuHandler parentView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View root = inflater.inflate(R.layout.activity_documents, container, false);
		
		// method for setting the actionBar
		setupActionBar();
		
		Button addDocBtn = (Button) root.findViewById(R.id.addDocumentBtn);
		Button restoreDocBtn = (Button) root.findViewById(R.id.restoreDocumentBtn);
		Button trashDocBtn = (Button) root.findViewById(R.id.trashDocumentBtn);
		
		if (addDocBtn != null)
		{
			addDocBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast msg = Toast.makeText(getActivity(), "This Button Will Import Files To Hide", Toast.LENGTH_SHORT);
					msg.show();
				}
			});
		}
		
		if (restoreDocBtn != null)
		{
				restoreDocBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast msg = Toast.makeText(getActivity(), "This Button Will Restore Files Back To System", Toast.LENGTH_SHORT);
					msg.show();
				}
			});
		}
		
		if (trashDocBtn != null)
		{
				trashDocBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast msg = Toast.makeText(getActivity(), "This Button Will Delete Files", Toast.LENGTH_SHORT);
					msg.show();
				}
			});
		}
		
		
		String[] videoNames = new String[]{"DOC_4373", "DOC_8448", "Doc_338239", "Doc_94412"};
		
		TableLayout table = (TableLayout) root.findViewById(R.id.documentTable);
		
		if (table != null)
		{
			table.removeAllViews();
			
			for (int i = 0; i < videoNames.length; i++)
			{
				TableRow row = new TableRow(getActivity());
				
				if (row != null)
				{
					
					// create the drawable icon
					Drawable image = getResources().getDrawable(R.drawable.video_icon);
					image.setBounds(0, 0, 100, 100);
					
					// create the text label
					TextView text = new TextView(getActivity());
					text.setText(videoNames[i]);
					
					// attach the drawable to the text
					text.setCompoundDrawables(image, null, null, null);
					
					// attach the text to the table row
					row.addView(text);
					
					// set the padding for the table row
					row.setPadding(10, 10, 0, 5);
					
					row.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Toast msg = Toast.makeText(getActivity(), "Tapping A File Will Allow For Viewing", Toast.LENGTH_SHORT);
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
			mInflater.inflate(R.menu.documents_menu, menu);
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
