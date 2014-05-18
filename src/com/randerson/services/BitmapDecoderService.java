package com.randerson.services;

import com.randerson.support.HidNExplorer;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class BitmapDecoderService extends IntentService {

	
	public BitmapDecoderService() {
		super("BitmapDecoderService");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		Log.i("Decoder Service", "Service started");
		
		String filePath = null;
		Messenger messenger = null;
		int serviceCode = 0;
		
		// get the passed in intent data
		if (intent != null)
		{
			// get the bundle of extras
			Bundle extras = intent.getExtras();
			
			if (extras != null)
			{
				// get the filepath
				filePath = (String) extras.get("filePath");
				
				// get the mesenger
				messenger = (Messenger) extras.get("messenger");
				
				// get the service id
				serviceCode = extras.getInt("serviceCode");
			}
		}
		
		// get the bitmap sample size
		int sampleSize = HidNExplorer.calulateBitmapSampleSize(filePath, 30, 30);
		
		// create and set the bitmap factory options
		BitmapFactory.Options options = new BitmapFactory.Options();
		
		if (options != null)
		{
			options.inPurgeable = true;
			options.inTempStorage = new byte[1024];
			options.inSampleSize = sampleSize;
		}
		
		// create the bitmap
		Drawable image = null;
		
		// decode the file into a bitmap
		Bitmap baseImage = BitmapFactory.decodeFile(filePath, options);
		
		if (baseImage != null)
		{
			// convert the bitmap into a drawable
			image = new BitmapDrawable(getApplication().getResources(), baseImage);
		}
		
		// get a message
		Message msg = Message.obtain();
		
		if (msg != null)
		{
			// set the message parameters
			msg.arg1 = Activity.RESULT_OK;
			msg.arg2 = serviceCode;
			msg.obj = image;
			
			try {
				
				// send the message
				messenger.send(msg);
				
			} catch (RemoteException e) {
				e.printStackTrace();
				
				Log.i("Decode Service Error", e.getLocalizedMessage());
			}
		}
	}

}
