package com.randerson.interfaces;

import android.support.v4.app.Fragment;
import android.view.Menu;

public interface MenuHandler {

	// method allows the active fragment to pass a reference to itself
	// to the parent activity and receives a reference to the parent actionbar menu in return
	public Menu getParentMenu(Fragment fragment);
}
