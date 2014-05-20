package com.randerson.support;

import com.randerson.hidn.R;

import android.content.Context;
import android.graphics.Color;
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
	public static int[] getThemeId(String theme)
	{
		int themeId[] = null;
		
		if (theme.equals("1_1"))
		{
			themeId = new int[]{R.drawable.theme_1_1, getColor("#3CC0E5"), getColor("#113540")};
		}
		else if (theme.equals("1_2"))
		{
			themeId = new int[]{R.drawable.theme_1_2, getColor("#498AE5"), getColor("#142640")};
		}
		else if (theme.equals("1_3"))
		{
			themeId = new int[]{R.drawable.theme_1_3, getColor("#3941E5"), getColor("#101240")};
		}
		else if (theme.equals("2_1"))
		{
			themeId = new int[]{R.drawable.theme_2_1, getColor("#A8218D"), getColor("#681558")};
		}
		else if (theme.equals("2_2"))
		{
			themeId = new int[]{R.drawable.theme_2_2, getColor("#E53484"), getColor("#400E25")};
		}
		else if (theme.equals("2_3"))
		{
			themeId = new int[]{R.drawable.theme_2_3, getColor("#E53124"), getColor("#400E0A")};
		}
		else if (theme.equals("3_1"))
		{
			themeId = new int[]{R.drawable.theme_3_1, getColor("#16E569"), getColor("#06401D")};
		}
		else if (theme.equals("3_2"))
		{
			themeId = new int[]{R.drawable.theme_3_2, getColor("#22E5BE"), getColor("#094035")};
		}
		else if (theme.equals("3_3"))
		{
			themeId = new int[]{R.drawable.theme_3_3, getColor("#14CECE"), getColor("#042929")};
		}
		else if (theme.equals("4_1"))
		{
			themeId = new int[]{R.drawable.theme_4_1, getColor("#3C93CE"), getColor("#0C1D29")};
		}
		else if (theme.equals("4_2"))
		{
			themeId = new int[]{R.drawable.theme_4_2, getColor("#CEA973"), getColor("#292117")};
		}
		else if (theme.equals("4_3"))
		{
			themeId = new int[]{R.drawable.theme_4_3, getColor("#9FA6A4"), getColor("#000000")};
		}
		else if (theme.equals("crimson"))
		{
			themeId = new int[]{R.drawable.theme_crimson, getColor("#8C0000"), getColor("#4C0000")};
		}
		else if (theme.equals("light"))
		{
			themeId = new int[]{R.drawable.theme_light, getColor("#DCE5E4"), getColor("#3D403F")};
		}
		else if (theme.equals("dark"))
		{
			themeId = new int[]{R.drawable.theme_dark, getColor("#292B2A"), getColor("#141515")};
		}
		else if (theme.equals("clay"))
		{
			themeId = new int[]{R.drawable.theme_clay, getColor("#E58B5C"), getColor("#402619")};
		}
		else if (theme.equals("pastel"))
		{
			themeId = new int[]{R.drawable.theme_pastel, getColor("#CED9E5"), getColor("#393C40")};
		}
		else if (theme.equals("wood"))
		{
			themeId = new int[]{R.drawable.theme_wood, getColor("#2B1F15"), getColor("#15100A")};
		}
		
		
		return themeId;
	}
	
	public static int getColor(String color)
	{
		return Color.parseColor(color);
	}
}
