package com.randerson.support;

import java.util.ArrayList;

import libs.ApplicationDefaults;
import libs.ApplicationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CallLog;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ActionManager {

	/** Method for removing call log entries of a single number or clearing the entire list if number is null*/
	public static boolean removeCallLogNumber(Context context, String number)
	{
		boolean success = false;
		
		// create a null where for deletion of all entries
		String where = null;
		
		// check if the number is provided or null
		// if it is provided create a where clause
		if (number != null)
		{
			// formulate the where clause
			where = CallLog.Calls.NUMBER + "='" + number + "'";
		}
		
		// remove the call log entries of the selected number
		int numRemoved = context.getContentResolver().delete(CallLog.Calls.CONTENT_URI, where, null);
		
		// check if there were any rows deleted
		if (numRemoved > 0)
		{
			// set the success to true
			success = true;
		}
		
		return success;
	}
	
	public static void launchMap(Context context, String address)
	{
		// create a new geo formatted address string
		address = "geo:0,0?q=" + address.replace(" ", "+");
		
		// create the map launch intent
		Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
		
		if (mapIntent != null)
		{
			// verify that the intent is safe
			boolean result = ApplicationManager.verifyIntent(context, mapIntent);
			
			if (result)
			{
				// start the map activity
				mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(mapIntent);
			}
			else 
			{
				// show error message
				showMessage(context, "No Activities To Handle Action");
			}
		}
	}
	
	public static void sendEmail(Context context, String email)
	{
		// create the email intent
		Intent emailMaker = new Intent(Intent.ACTION_SEND);
		
		if (emailMaker != null)
		{
			// set the email intent parameters
			emailMaker.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			emailMaker.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
			emailMaker.setType("message/rfc822");
			
			// check if the intent can be handled
			boolean result = ApplicationManager.verifyIntent(context, emailMaker);
			
			if (result)
			{
				// create the chooser for activity
				context.startActivity(emailMaker);
			}
			else
			{
				// show error message
				showMessage(context, "No Activities To Handle Action");
			}
			
		}
	}
	
	public static void makeCall(Context context, String number)
	{
		Intent callMaker = new Intent(Intent.ACTION_CALL);
		
		if (callMaker != null)
		{
			// set the intent data
			callMaker.setData(Uri.parse("tel:" + number));
			callMaker.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			// start the call activity
			context.startActivity(callMaker);
		}
	}
	
	public static boolean sendText(String number, String text)
	{
		boolean result = true;
		
		final int MAX_SIZE = 160;
		
		// get instance of the sms manager
		SmsManager sms = SmsManager.getDefault();
		
		if (sms != null)
		{
			// check if the text is within the character limit
			if (text.length() > MAX_SIZE)
			{
				try {
					
					// send the text message
					sms.sendTextMessage(number, null, text, null, null);
					
				} catch (Exception e) {
					e.printStackTrace();
					
					Log.i("Telephony Error", "Error sending single text message");
					
					result = false;
				}
			}
			else
			{
				// divide up the text message if larger than maximum allowed sms length
				ArrayList<String> messages = sms.divideMessage(text);
				
				if (messages != null)
				{
					try {
						
						// send the split text messages
						sms.sendMultipartTextMessage(number, null, messages, null, null);
						
					} catch (Exception e) {
						e.printStackTrace();
						
						Log.i("Telephony Error", "Error sending multi-part text message");
						
						result = false;
					}
				}
			}
			
		}
		
		return result;
	}
	
	public static void showMessage(Context context, String message)
	{
		Toast msg = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		
		if (msg != null)
		{
			msg.show();
		}
	}
	
	public static int getBgColor(TextView view)
	{
		return view.getSolidColor();
	}
	
	public static void setHighlighted(Context context, TextView view)
	{
		if (view != null)
		{
			view.setBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_dark));
		}
	}
	
	public static void restoreBgColor(Context context, TextView view, int position)
	{
		ApplicationDefaults defaults = new ApplicationDefaults(context);
		
		if (defaults != null)
		{
			int themeListA = defaults.getData().getInt("themeListA", ThemeMaster.getColor("#9FA6A4"));
			int themeListB = ThemeMaster.getColor("#FFFFFF");
			
			if (view != null)
			{
				// check if the position is even or odd and set the background to appropriate color
				if (position % 2 == 0)
				{
					view.setBackgroundColor(themeListB);
				}
				else
				{
					view.setBackgroundColor(themeListA);
				}
			}
		}
	}
	
}
