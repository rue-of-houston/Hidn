package com.randerson.kinvey;

import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.android.callback.KinveyPingCallback;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.Query;
import com.kinvey.java.User;
import com.kinvey.java.core.KinveyClientCallback;
import com.randerson.interfaces.KinveySetup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class KinveyManager implements KinveySetup {
	
	private final String SECRET_KEY = "5d94e66e8e8542638292a658c141c996";
	private final String APP_KEY = "kid_TVrbNTNGlO";
	private Client kinvey;
	private Toast prompt;
	public boolean RESULT = false;
	
	@SuppressLint("ShowToast")
	public KinveyManager(Activity context)
	{
		
		// setup the kinvey client
		kinvey = new Client.Builder(APP_KEY, SECRET_KEY, context.getApplicationContext()).build();
		
		if (kinvey != null)
		{
			kinvey.enableDebugLogging();
			kinvey.ping(new KinveyPingCallback() {
				
				@Override
				public void onSuccess(Boolean result) {
					
					Log.i("Client Setup", result.toString());
				}
				
				@Override
				public void onFailure(Throwable err) {
					
					Log.i("Client Setup Error", err.getLocalizedMessage());
					err.printStackTrace();
				}
			});
		}
		
		// create the toast object
		prompt = Toast.makeText(context, "", Toast.LENGTH_SHORT);
	}
	
	@Override
	// method for signing a user in
	public void signIn(String username, String password, final int resultCode, final Handler handler)
	{
		// setup the sign in callback
		KinveyUserCallback callback = new KinveyUserCallback() {
			
			@SuppressWarnings("rawtypes")
			@Override
			public void onSuccess(User user)
			{
				// create the toast message
				String message = "Logged in as " + user.getUsername();
				showMessage(message);
				
				if (handler != null)
				{
					// create the messenger and message objects
					Messenger messenger = new Messenger(handler);
					Message msg = Message.obtain();
					
					if (msg != null && messenger != null)
					{
						msg.arg1 = Activity.RESULT_OK;
						msg.arg2 = resultCode;
						msg.obj = user;
								
						try {
							
							// sends the message through handler
							messenger.send(msg);
							
						} catch (RemoteException e)
						{
							e.printStackTrace();
							
							Log.i("Kinvey Manager Exception", "Handler Error Signing In");
						}
					}
				}
				
				Log.i("Sign In Success", user.toString());
				
				// set the result to true since account successfully logged in
				RESULT = true;
			}
			
			@Override
			public void onFailure(Throwable err)
			{
				// create the toast message
				String message = "Incorrect username or password";
				showMessage(message);
				
				Log.i("Sign In Error", err.getLocalizedMessage());
				err.printStackTrace();
				
				// set the result to false since account failed to login
				RESULT = false;
			}
		};
		
		// log in any active users out
		signOff();
		
		// attempt to sign the user in
		kinvey.user().login(username, password, callback);
	
	}
	
	@Override
	// method for signing a user up
	public void signUp(final String username, final String password, final String firstName, final String lastName, final Handler handler)
	{
		// setup the sign up callbacks
		final KinveyUserCallback callbackB = new KinveyUserCallback() {
			
			@SuppressWarnings("rawtypes")
			@Override
			public void onSuccess(User user)
			{
				// create the toast message
				String message = "Setup complete";
				showMessage(message);
				
				if (handler != null)
				{
					// create the messenger and message objects
					Messenger messenger = new Messenger(handler);
					Message msg = Message.obtain();
					
					if (msg != null && messenger != null)
					{
						msg.arg1 = Activity.RESULT_OK;
						msg.arg2 = SIGN_UP_RESULT;
						msg.obj = user;
								
						try {
							
							// sends the message through handler
							messenger.send(msg);
							
						} catch (RemoteException e)
						{
							e.printStackTrace();
							
							Log.i("Kinvey Manager Exception", "Handler Error Signing Up");
						}
					}
				}
				
				Log.i("Sign Up Setup Success", user.toString());
			}
			
			@Override
			public void onFailure(Throwable err) 
			{
				// create the toast message
				String message = "Setup did not finalize";
				showMessage(err.getLocalizedMessage());
				
				Log.i("Sign Up Setup Error", message);
				err.printStackTrace();
			}
		};
		
		final KinveyUserCallback callback = new KinveyUserCallback() {
			
			@SuppressWarnings("rawtypes")
			@Override
			public void onSuccess(User user)
			{
				// create the toast message
				String message = "Account created";
				showMessage(message);
				
				Log.i("Sign Up Success", user.toString());
				
				// set the additional user data
				kinvey.user().put("email", username);
				kinvey.user().put("first_name", firstName);
				kinvey.user().put("last_name", lastName);
				
				// update the data on the backend
				kinvey.user().update(callbackB);
				
				// set the result to true since account successfully created
				RESULT = true;
			}
			
			@Override
			public void onFailure(Throwable err)
			{
				// create the toast message
				String message = "Account creation failed";
				showMessage(err.getLocalizedMessage());
				
				Log.i("Sign Up Error", message);
				err.printStackTrace();
				
				// set the result to false since account creation was unsuccessful
				RESULT = false;
				
			}
		};
		
		// log in any active users out
		signOff();
		
		// attempt to sign the user up
		kinvey.user().create(username, password, callback);
	}
	
	@Override
	// method for signing user out
	public void signOff()
	{
		// check for a logged in user
		boolean isLoggedIn = kinvey.user().isUserLoggedIn();
		
		if (isLoggedIn)
		{
			// log the user out and remove its data from system
			kinvey.user().logout().execute();
		}
	}
	
	@Override
	// method for saving account data
	public void updateAccountData(AccountsEntity entity, final Handler handler)
	{
		// create the app data for the associated entity
		AsyncAppData<AccountsEntity> accounts = kinvey.appData("AccountData", AccountsEntity.class);
		
		// create the save callback
		KinveyClientCallback<AccountsEntity> callback = new KinveyClientCallback<AccountsEntity>() {
			
			@Override
			public void onSuccess(AccountsEntity entity)
			{
				// create the toast message
				String message = "User data saved";
				showMessage(message);
				
				if (handler != null)
				{
					// create the messenger and message objects
					Messenger messenger = new Messenger(handler);
					Message msg = Message.obtain();
					
					if (msg != null && messenger != null)
					{
						msg.arg1 = Activity.RESULT_OK;
						msg.arg2 = UPDATING_ACCOUNT;
						msg.obj = entity;
								
						try {
							
							// sends the message through handler
							messenger.send(msg);
							
						} catch (RemoteException e)
						{
							e.printStackTrace();
							
							Log.i("Kinvey Manager Exception", "Handler Error Updating Account");
						}
					}
				}
				
				Log.i("Account Update Success", entity.toString());
				
				// capture the result
				RESULT = true;
			}
			
			@Override
			public void onFailure(Throwable err)
			{
				// create the toast message
				String message = "User data not saved";
				showMessage(err.getLocalizedMessage());
				
				Log.i("Account Update Error", message);
				err.printStackTrace();
				
				
				// capture the result
				RESULT = false;
			}
		};
		
		if (accounts != null)
		{
			accounts.save(entity, callback);
		}
		
	}
	
	@Override
	public void getAccountData(Query query, final Handler handler)
	{
		// create the app data for the associated entity
		AsyncAppData<AccountsEntity> accounts = kinvey.appData("AccountData", AccountsEntity.class);
		
		// create the callback
		KinveyListCallback<AccountsEntity> callback = new KinveyListCallback<AccountsEntity>() {
			
			@Override
			public void onSuccess(AccountsEntity[] entity)
			{
				if (handler != null)
				{
					// create the messenger and message objects
					Messenger messenger = new Messenger(handler);
					Message msg = Message.obtain();
					
					if (msg != null && messenger != null)
					{
						msg.arg1 = Activity.RESULT_OK;
						msg.arg2 = FETCHING_ACCOUNT;
						msg.obj = entity;
								
						try {
							
							// sends the message through handler
							messenger.send(msg);
							
						} catch (RemoteException e)
						{
							e.printStackTrace();
							
							Log.i("Kinvey Manager Exception", "Handler Error Retrieving Account");
						}
					}
				}
				
				Log.i("Account Data", "Queried " + entity.length + " result(s)");
				Log.i("Account Data Success", entity.toString());
			}
			
			@Override
			public void onFailure(Throwable err)
			{
				showMessage(err.getLocalizedMessage());
				Log.i("Account Data", "Failed to retrieve account data");
				Log.i("Account Data Error", err.getLocalizedMessage());
				err.printStackTrace();
			}
		};
		
		if (accounts != null)
		{
			// initiate a query
			accounts.get(query, callback);
		}
		
	}
	
	// method for displaing toasts
	private void showMessage(String message)
	{
		if (prompt != null)
		{
			prompt.setText(message);
			prompt.show();
		}
	}
	
	@Override
	// method for creating a query with a single item
	public Query createQuery(String key, Object value)
	{
		Query query = kinvey.query();
		
		if (query != null)
		{
			query.equals(key, value);
			
			Log.i("Query Set", query.toString());
		}
		
		return query;
	}
	
	@Override
	public Query createQuery(String[] keys, Object[] values)
	{
		Query query = kinvey.query();
		
		for (int i = 0; i < keys.length; i++)
		{
			query.equals(keys[i], values[i]);
		}
		
		return query;
	}
	
	public Client getClient()
	{
		return kinvey;
	}

	@Override
	public void getNewsData(Query query, final Handler handler)
	{
		// create the app data for the associated entity
				AsyncAppData<NewsEntity> accounts = kinvey.appData("News", NewsEntity.class);
				
				// create the callback
				KinveyListCallback<NewsEntity> callback = new KinveyListCallback<NewsEntity>() {
					
					@Override
					public void onSuccess(NewsEntity[] entity)
					{
						if (handler != null)
						{
							// create the messenger and message objects
							Messenger messenger = new Messenger(handler);
							Message msg = Message.obtain();
							
							if (msg != null && messenger != null)
							{
								msg.arg1 = Activity.RESULT_OK;
								msg.arg2 = FETCHING_ACCOUNT;
								msg.obj = entity;
										
								try {
									
									// sends the message through handler
									messenger.send(msg);
									
								} catch (RemoteException e)
								{
									e.printStackTrace();
									
									Log.i("Kinvey Manager Exception", "Handler Error Retrieving News");
								}
							}
						}
						
						Log.i("News Data", "Queried " + entity.length + " result(s)");
						Log.i("News Data Success", entity.toString());
					}
					
					@Override
					public void onFailure(Throwable err)
					{
						showMessage(err.getLocalizedMessage());
						Log.i("News Data", "Failed to retrieve news data");
						Log.i("News Data Error", err.getLocalizedMessage());
						err.printStackTrace();
					}
				};
				
				if (accounts != null)
				{
					// initiate a query
					accounts.get(query, callback);
				}
	}
}
