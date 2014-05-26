package com.randerson.support;

import com.randerson.activities.PasswordActivity;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class PhoneListener extends PhoneStateListener {
	
	private boolean isCallActive = false;
	public Context CONTEXT;
	
	public PhoneListener(Context context)
	{
		CONTEXT = context;
	};
	
	
	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		super.onCallStateChanged(state, incomingNumber);
		
		if (state == TelephonyManager.CALL_STATE_OFFHOOK)
		{
			// sets the call tracker bool to true
			isCallActive = true;
		}
		else if (state == TelephonyManager.CALL_STATE_RINGING)
		{
			
		}
		else if (state == TelephonyManager.CALL_STATE_IDLE)
		{
			// check if the phone was just active
			if (isCallActive)
			{
				// reset the tracker bool
				isCallActive = false;
				
				// create the security activity intent to launch
				Intent securityIntent = new Intent(CONTEXT, PasswordActivity.class);
					
				if (securityIntent != null)
				{
					// start the activity
					CONTEXT.startActivity(securityIntent);
				}
			}
		}
	}
	
}
