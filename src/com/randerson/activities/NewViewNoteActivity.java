package com.randerson.activities;

import com.randerson.hidn.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class NewViewNoteActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		// set the activity to full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// inflate the view
		setContentView(R.layout.activity_edit_note);
	}
	
	
}
