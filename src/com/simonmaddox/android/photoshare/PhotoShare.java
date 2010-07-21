package com.simonmaddox.android.photoshare;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PhotoShare extends Activity {
	
	Intent i;
	
	public static final String PREFS = "PhotoSharePreferences";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button button = (Button)findViewById(R.id.enable);
        // Register the onClick listener with the implementation above
        button.setOnClickListener(enableDisableService);
    }
    
    private OnClickListener enableDisableService = new OnClickListener() {
		public void onClick(View v) {
			SharedPreferences settings = getSharedPreferences(PREFS, 0);
			SharedPreferences.Editor editor = settings.edit();
			
			if (i == null || !settings.getBoolean("enabled", false)){
				i = new Intent();
				i.setClassName("com.simonmaddox.android.photoshare", "com.simonmaddox.android.photoshare.WatcherService");
		        startService(i);
		        
		        editor.putString("email", ((EditText) findViewById(R.id.email)).getText().toString());
		        editor.putBoolean("enabled", true);
		        editor.commit();
		        
		        SharedPreferences preferences = getSharedPreferences(PREFS, 0);
	
				String email = preferences.getString("email", "");
				
				Log.e("SAVING", "Email: " + email);
	
		        
		        Button button = (Button)findViewById(R.id.enable);
		        button.setText(R.string.disable);
			} else {
				stopService(i);
				editor.putBoolean("enabled", false);
				editor.commit();
				
				Button button = (Button)findViewById(R.id.enable);
		        button.setText(R.string.enable);
			}
		}
    };
    
    @Override
    public void onStart()
    {
    	super.onStart();
    }
    
    @Override
    public void onPause()
    {
    	super.onPause();
    	Log.e("PHOTO", "PAUSE");
    }
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    }
}