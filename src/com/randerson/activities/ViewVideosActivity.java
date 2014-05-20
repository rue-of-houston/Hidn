package com.randerson.activities;
import java.io.File;

import libs.ApplicationDefaults;

import com.randerson.hidn.R;
import com.randerson.interfaces.FragmentSetup;
import com.randerson.support.ThemeMaster;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.VideoView;

public class ViewVideosActivity extends Activity implements FragmentSetup {

	public String TITLE = "Video Viewer";
	public String theme;
	public String themeB;
	public boolean defaultNavType;
	public String filePath;
	public String fileName;
	public AlertDialog alert;
	public String key;
	public VideoView videoPlayer;
	public ProgressBar elapsedTime;
	public boolean isPaused = false;
	public int duration;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// set the activity to full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// inflate the view
		setContentView(R.layout.activity_view_videos);
		
		// get the extras
		Intent data = getIntent();
		
		if (data != null)
		{
			// get the bundle of extras
			Bundle extras = data.getExtras();
			
			if (extras != null)
			{
				// set the filepath
				filePath = extras.getString("filePath");
				fileName = extras.getString("fileName");
				key = extras.getString("key");
				
				boolean exists = new File(filePath).exists();
				
				if (exists)
				{
					Toast msg = Toast.makeText(this, "File found", Toast.LENGTH_SHORT);
					msg.show();
				}
			}
		}

		// load the application settings
		loadApplicationSettings();
		
		// create the view from layout res
		videoPlayer = (VideoView) findViewById(R.id.videoPlayer);
		//elapsedTime = (ProgressBar) findViewById(R.id.videoProgress);
		Button pause = (Button) findViewById(R.id.pauseBtn);
		Button play = (Button) findViewById(R.id.playBtn);
		Button rewind = (Button) findViewById(R.id.rewindBtn);
		Button fastForward = (Button) findViewById(R.id.fastForwardBtn);
		
		if (videoPlayer != null)
		{
			// set the video file path
			videoPlayer.setVideoPath(new File(filePath).getAbsolutePath());
			
			videoPlayer.requestFocus();
		
			MediaController controller = new MediaController(getApplicationContext());
			
			if (controller != null)
			{
				controller.setAnchorView(videoPlayer);
				videoPlayer.setMediaController(controller);
			}

			
			videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					
					videoPlayer.requestFocus();
					
					// play the file
					videoPlayer.start();
					
					// get the video duration
					duration = videoPlayer.getDuration();
					
					Log.i("Duration: ", "" + duration);
				}
			});
		}
		
		if (elapsedTime != null)
		{
			elapsedTime.setMax(duration);
		}
		
		if (pause != null)
		{
			pause.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v)
				{
					// check if the video player is playing, if so pause it
					if (videoPlayer.isPlaying() && videoPlayer.canPause())
					{
						videoPlayer.pause();
						
						// set the pause bool
						isPaused = true;
					}
					else if (videoPlayer.isPlaying() == false && isPaused == true)
					{
						// resume the playing
						videoPlayer.resume();
						
						// reset the pause bool
						isPaused = false;
					}
				}
			});
		}
		
		if (play != null)
		{
			play.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v)
				{
					// check if the video player is playing, if not start it
					if (videoPlayer.isPlaying() == false && isPaused == false)
					{
						videoPlayer.requestFocus();
						videoPlayer.start();
						
					/*	Timer timer = new Timer();
						
						if (timer != null)
						{
							timer.scheduleAtFixedRate(new TimerTask() {
								
								@Override
								public void run() {
									
									// increment the progress
									elapsedTime.setProgress(videoPlayer.getCurrentPosition());
								}
							}, 0, 1000);
						}*/
					}
					else if (videoPlayer.isPlaying() == false && isPaused == true)
					{
						// resume the video playback
						videoPlayer.resume();
						
						// reset the pause bool
						isPaused = false;
					}
				}
			});
		}
		
		if (rewind != null)
		{
			rewind.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) 
				{
					// check if the video player is playing, if so begin back seeking
					if (videoPlayer.isPlaying() && videoPlayer.canSeekBackward())
					{
						int position = videoPlayer.getCurrentPosition();
						int seekAmount = 10000;
						
						// check if there is less than 10 seconds passed
						if (position < seekAmount)
						{
							// check if there has passed less than 1 second
							if (position < 1000)
							{
								// seek the entire elapsed time
								seekAmount = position;
							}
							else
							{
								// set the seek to 1 second
								seekAmount = 1000;
							}
						}
						
						// back seek the video
						videoPlayer.seekTo(position - seekAmount);
					}
				}
			});
		}
		
		if (fastForward != null)
		{
			fastForward.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v)
				{
					if (videoPlayer.isPlaying() && videoPlayer.canSeekForward())
					{
						int position = videoPlayer.getCurrentPosition();
						int seekAmount = 10000;
						
						// check if there is less than 10 seconds left
						if ((seekAmount + position) > duration)
						{
							// check if there is less than 1 sec left
							if ((1000 + position) > duration)
							{
								// seek the entire elapsed time
								seekAmount = duration;
							}
							else
							{
								// set the seek to 1 second
								seekAmount = 1000;
							}
						}
						
						// forward seek the video
						videoPlayer.seekTo(position + seekAmount);
					}
				}
			});
		}
	}
	
	@Override
	public void setupActionBar() {
		
		int color = ThemeMaster.getThemeId(theme)[0];
		
		// set the actionBar styling
		getActionBar().setBackgroundDrawable(getResources().getDrawable(color));
		
		// set the title to appear for the drawerlist view
		getActionBar().setTitle(TITLE);
		
		int themeBId = ThemeMaster.getThemeId(themeB.toLowerCase())[0];
		
		// set the background styling
		ScrollView layoutBg = (ScrollView) findViewById(R.id.viewVideosBg);
		
		// verify the view is valid first
		if (layoutBg != null)
		{
			layoutBg.setBackground(getResources().getDrawable(themeBId));
		}
	}

	@Override
	public void loadApplicationSettings()
	{
		// create a application defaults object to load app settings
		ApplicationDefaults defaults = new ApplicationDefaults(this);
		
		if (defaults != null)
		{
			defaultNavType = defaults.getData().getBoolean("defaultNavType", true);
			theme = defaults.getData().getString("theme", "4_3");
			themeB = defaults.getData().getString("themeB", "Dark");
		}
		
		// method for setting the actionBar
		setupActionBar();
	}

	@Override
	public void onActionBarItemClicked(int itemId) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		ApplicationDefaults defaults = new ApplicationDefaults(this);
		
		if (defaults != null)
		{
			// set the app to reload the last view upon restart
			defaults.set("loadLastView", true);
		}
		
		finish();
	}
	
}
