package com.randerson.entities;

import com.randerson.hidn.R;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class ThemeMaster {

	Context CONTEXT;
	Drawable BUTTON_GRADIENT = getDrawable(R.drawable.button_gradient);
	Drawable CONTACTS_GRADIENT = getDrawable(R.drawable.theme_1_3);
	
	public ThemeMaster(Context context)
	{
		CONTEXT = context;
	}
	
	// method for returning a drawable from resource id
	public Drawable getDrawable(int resId)
	{
		return CONTEXT.getResources().getDrawable(resId);
	}
}
