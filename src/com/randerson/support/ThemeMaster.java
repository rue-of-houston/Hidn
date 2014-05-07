package com.randerson.support;

import com.randerson.hidn.R;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class ThemeMaster {

	Context CONTEXT;
	
	public ThemeMaster(Context context)
	{
		CONTEXT = context;
	}
	
	// method for returning a drawable from resource id
	public Drawable getDrawable(int resId)
	{
		return CONTEXT.getResources().getDrawable(resId);
	}
	
	// method for getting theme ids
	public static int getThemeId(String theme)
	{
		int themeId = 0;
		
		if (theme.equals("1_1"))
		{
			themeId = R.drawable.theme_1_1;
		}
		else if (theme.equals("1_2"))
		{
			themeId = R.drawable.theme_1_2;
		}
		else if (theme.equals("1_3"))
		{
			themeId = R.drawable.theme_1_3;
		}
		else if (theme.equals("2_1"))
		{
			themeId = R.drawable.theme_2_1;
		}
		else if (theme.equals("2_2"))
		{
			themeId = R.drawable.theme_2_2;
		}
		else if (theme.equals("2_3"))
		{
			themeId = R.drawable.theme_2_3;
		}
		else if (theme.equals("3_1"))
		{
			themeId = R.drawable.theme_3_1;
		}
		else if (theme.equals("3_2"))
		{
			themeId = R.drawable.theme_3_2;
		}
		else if (theme.equals("3_3"))
		{
			themeId = R.drawable.theme_3_3;
		}
		else if (theme.equals("4_1"))
		{
			themeId = R.drawable.theme_4_1;
		}
		else if (theme.equals("4_2"))
		{
			themeId = R.drawable.theme_4_2;
		}
		else if (theme.equals("4_3"))
		{
			themeId = R.drawable.theme_4_3;
		}
		else if (theme.equals("crimson"))
		{
			themeId = R.drawable.theme_crimson;
		}
		else if (theme.equals("light"))
		{
			themeId = R.drawable.theme_light;
		}
		else if (theme.equals("dark"))
		{
			themeId = R.drawable.theme_dark;
		}
		else if (theme.equals("clay"))
		{
			themeId = R.drawable.theme_clay;
		}
		else if (theme.equals("pastel"))
		{
			themeId = R.drawable.theme_pastel;
		}
		else if (theme.equals("wood"))
		{
			themeId = R.drawable.theme_wood;
		}
		
		
		return themeId;
	}
}
