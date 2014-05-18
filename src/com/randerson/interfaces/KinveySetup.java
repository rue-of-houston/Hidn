package com.randerson.interfaces;

import android.os.Handler;

import com.kinvey.java.Query;
import com.randerson.kinvey.AccountsEntity;

public interface KinveySetup
{
	public final int SIGN_UP_RESULT = 1;
	public final int SIGN_IN_AND_SYNC_RESULT = 2;
	public final int SIGN_IN_NO_SYNC_RESULT = 3;
	public final int UPDATING_ACCOUNT = 4;
	public final int FETCHING_ACCOUNT = 5;
	
	public void signIn(String username, String password, int resultCode, Handler handler);
	public void signUp(String username, String password, String firstName, String lastName, Handler handler);
	public void updateAccountData(AccountsEntity entity, Handler handler);
	public void getAccountData(Query query, Handler handler);
	public Query createQuery(String key, Object value);
	public Query createQuery(String[] keys, Object[] values);
	public void signOff();
}
