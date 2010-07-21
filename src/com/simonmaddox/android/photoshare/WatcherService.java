package com.simonmaddox.android.photoshare;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.simonmaddox.android.photoshare.PhotoWatcher;

public class WatcherService extends Service {
	
	PhotoWatcher watcher;
	public boolean running;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onStart( Intent intent, int startId ) {
		super.onStart( intent, startId );
		Log.e( "SERVICE", "Started" );
		this.watcher = new PhotoWatcher(getBaseContext(), this.getString(R.string.watchedPath));
		this.watcher.startWatching();
		running = true;
	}
	
	public void onDestroy()
	{
		this.watcher.stopWatching();
		Log.e("SERVICE", "Destroyed");
		running = false;
		super.onDestroy();
	}
}
