package com.simonmaddox.android.photoshare;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.FileObserver;
import android.util.Log;

import android.app.NotificationManager;
import android.app.Notification;
import android.app.PendingIntent;

public class PhotoWatcher extends FileObserver {
	String watchedPath;
	
	public static final String PREFS = "PhotoSharePreferences";
	private Context context;
	
	private int APP_ID = 6789063;
	
	public PhotoWatcher(Context theContext, String path)
	{
		super(path, CREATE);
		this.watchedPath = path;
		this.context = theContext;
		Log.e("WATCHER", "Path: " + this.watchedPath);
	}
	
	@Override
	public void onEvent(int event, String filename) {
		if (filename == null){
			return;
		}
		Log.e("WATCHER", "Got: " + this.watchedPath + filename);
		
		SharedPreferences preferences = context.getSharedPreferences(PREFS, 0);

		String email = preferences.getString("email", "");
		
		File image = new File(this.watchedPath + filename);
		Log.e("WATCHER", "Uploading");
		
		String encodedFilename = URLEncoder.encode(filename);
		String encodedEmail = URLEncoder.encode(email);
		
		String url = "http://photoshare.heroku.com/send/"+encodedFilename+"/" + encodedEmail;
		
		Log.e("WATCHER", "Posting to " + url);
		
		Intent intent = new Intent();
		
		NotificationManager notificationManager = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Notification notification = new Notification(R.drawable.icon, "Sharing " + filename + " with " + email, System.currentTimeMillis());
		notification.setLatestEventInfo(this.context,"PhotoShare","Uploading " + filename, PendingIntent.getActivity(this.context, 0, intent,PendingIntent.FLAG_CANCEL_CURRENT));
		
		notificationManager.notify(APP_ID, notification);
		
		int response = this.postPage(url, image);
		
		Log.e("WATCHER", "Response: " + response);
		
		notificationManager.cancel(APP_ID);
	}

	public int postPage(String url, File data) {

	    HttpClient httpclient = new DefaultHttpClient();
	    
	    httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);

	    HttpPost httpPost = new HttpPost(url);
	    HttpResponse response = null;

	    FileEntity tmp = null;       

	    tmp = new FileEntity(data,"UTF-8");

	    httpPost.setEntity(tmp);

	    try {
	        response = httpclient.execute(httpPost);
	    } catch (ClientProtocolException e) {
	    	Log.e("WATCHER", "HTTPHelp : ClientProtocolException : "+e);
	    } catch (IOException e) {
	    	Log.e("WATCHER", "HTTPHelp : IOException : "+e);
	    }

        return response.getStatusLine().getStatusCode();
	}
}
