package com.randerson.services;

import java.io.File;

import com.randerson.support.HidNExplorer;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class VideoPlayerService extends IntentService {

	public VideoPlayerService() {
		super("VideoPlayerService");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		String fileName = null;
		String filePath = null;
		String returnPath = null;
		int resultCode = 0;
		Messenger messenger = null;
		Message msg = Message.obtain();
		
		// verify the intent is not null
		if (intent != null)
		{
			// verify it has extras
			Bundle extras = intent.getExtras();
			
			if (extras != null)
			{
				// set the extra data
				fileName = extras.getString("fileName");
				filePath = extras.getString("filePath");
				messenger = (Messenger) extras.get("messenger");
				resultCode = extras.getInt("requestCode");
			}
		}
		
		// verify that the file name & path are non null
		if (fileName != null && filePath != null)
		{
			// create the path string for the public folder
			String publicDir = Environment.getExternalStorageDirectory() + "/HidN/";
			
			// create the temp file
			File tempFile = new File(publicDir);
			
			// create the source file
			File sourceFile = new File(filePath);
			
			if (tempFile != null)
			{
				// create an explorer instance to move the file publicly
				HidNExplorer explorer = new HidNExplorer(getApplicationContext());
				
				if (explorer != null)
				{
					// attempt to move the file data and capture the result
					boolean didMove = explorer.moveFile(sourceFile, tempFile, false);
					
					if (didMove)
					{
						Log.i("Temp File Relocation", "Success");
						
						// set the abs path for the temp file
						returnPath = (new File(tempFile, fileName)).getAbsolutePath();
					}
				}
			}
		}
		
		// verify the messenger is valid
		if (messenger != null)
		{
			// verify the message object
			if (msg != null)
			{
				try {
					
					// set the message return parameters
					msg.arg1 = Activity.RESULT_OK;
					msg.arg2 = resultCode;
					msg.obj = returnPath;
					
					// try to send the msg back
					messenger.send(msg);
					
				} catch (RemoteException e) {
					e.printStackTrace();
					
					Log.i("VideoPlayerService", e.getLocalizedMessage());
				}
			}
		}
	}

}
