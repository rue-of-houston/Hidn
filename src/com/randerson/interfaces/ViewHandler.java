package com.randerson.interfaces;

import android.support.v4.app.Fragment;

public interface ViewHandler {

	public void loadPreviousView(int layoutId);
	
	public void passFragmentToParent(Fragment fragment, int lastViewId);
	
}
