package com.randerson.hidn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class NewViewContactActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// remove the title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// set the activity to full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_edit_contact);
	}
	
	@Override
	public void onBackPressed() {
		Intent goBack = new Intent(this, PagerFragmentActivity.class);
		
		if (goBack != null)
		{
			startActivity(goBack);
		}
	}
	
}
