package com.randerson.support;
import com.randerson.services.BitmapDecoderService;

import libs.ApplicationDefaults;
import libs.UniArray;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class PhotoAdapter extends ArrayAdapter<String> {


	Context CONTEXT;
	String[] LIST_DATA = null;
	String[] LIST_DATA_PATHS = null;
	int LAYOUT_ID = 0;
	int TEXT_VIEW_ID = 0;
	DataManager DATA_MANAGER = null;
	int themeListA = 0;
	int themeListB = 0;
	
	public PhotoAdapter(Context context, int resource, int textViewResourceId,
			String[] objects) 
	{
		super(context, resource, textViewResourceId, objects);
		
		CONTEXT = context;
		LIST_DATA = objects;
		LAYOUT_ID = resource;
		TEXT_VIEW_ID = textViewResourceId;
		
		DATA_MANAGER = new DataManager(context);
		
		ApplicationDefaults defaults = new ApplicationDefaults(CONTEXT);
		
		if (defaults != null)
		{
			themeListA = defaults.getData().getInt("themeListA", ThemeMaster.getColor("#9FA6A4"));
			themeListB = ThemeMaster.getColor("#FFFFFF");
		}
	}

	@Override
	public int getCount() {
		return LIST_DATA.length;
	}

	@Override
	public String getItem(int position)
	{	
		String title = null;
		
		if (LIST_DATA != null)
		{
			title = LIST_DATA[position];
		}
		
		return title;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent)
	{
		LinearLayout layout = (LinearLayout) super.getView(position, convertView, parent);
		 
		// get the hidn photo paths
		LIST_DATA_PATHS = getPaths();
		
		
		final TextView textView = (TextView) layout.findViewById(TEXT_VIEW_ID);
			
				if (textView != null)
				{
					if (LIST_DATA != null)
					{
						// set the text view text
						textView.setText(LIST_DATA[position]);
						
						
						// check if the position is even or odd and set the background to appropriate drawable
						if (position % 2 == 0)
						{
							textView.setBackgroundColor(themeListB);
							textView.setTextColor(ThemeMaster.getColor("#000000"));
						}
						else
						{
							textView.setBackgroundColor(themeListA);
						}
					}
					
					if (LIST_DATA_PATHS != null)
					{
						// get a random number for the id
						final int serviceCode = (int) (Math.random() * 1000000);
						
						Handler handler = new Handler()
						{
							@Override
							public void handleMessage(Message msg) {
								super.handleMessage(msg);
								
								// check the message results
								if (msg.arg1 == Activity.RESULT_OK && msg.arg2 == serviceCode)
								{
									if (msg.obj != null)
									{
										// retrieve the Drawable from the message
										Drawable image = (Drawable) msg.obj;
										
										// set the image bounds for the compound drawable state
										image.setBounds(0, 0, 72, 72);
								
										// set the compound drawable
										textView.setCompoundDrawables(image, null, null, null);
									}
								}
							}
						}; 
						
						// instantiate the decoding service
						Intent decoderService = new Intent(CONTEXT, BitmapDecoderService.class);
						
						if (decoderService != null)
						{
							// create the messenger for the handler
							Messenger messenger = new Messenger(handler);
							
							if (messenger != null)
							{
								// get the filepath for the selected item
								String filePath = LIST_DATA_PATHS[position];
								
								// add the service args
								decoderService.putExtra("filePath", filePath);
								decoderService.putExtra("messenger", messenger);
								decoderService.putExtra("serviceCode", serviceCode);
								
								// start the service
								CONTEXT.startService(decoderService);
							}
						}
					}
				}
						
		return layout;
	}
	
	
	@Override
	public boolean hasStableIds()
	{
		return true;
	}
	
	private String[] getPaths()
	{
		String[] photoPaths = null;
		
		if (DATA_MANAGER != null)
		{
			// get the photos uniarray
			UniArray photos = (UniArray) DATA_MANAGER.load(DataManager.PHOTO_DATA);
			
			if (photos !=  null)
			{
				// create a string array to hold the paths
				photoPaths = new String[LIST_DATA.length];
				
				if (photoPaths != null)
				{
					for (int i = 0; i < LIST_DATA.length; i++)
					{
						UniArray item = (UniArray) photos.getObject(LIST_DATA[i]);
						
						if (item != null)
						{
							photoPaths[i] = item.getString("hidnPath");
						}
					}
				}
			}
		}
		
		return photoPaths;
	}

}
