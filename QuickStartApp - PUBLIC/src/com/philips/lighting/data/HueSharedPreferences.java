package com.philips.lighting.data;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class HueSharedPreferences {
    private static final String HUE_SHARED_PREFERENCES_STORE = "HueSharedPrefs";
    private static final String LAST_CONNECTED_USERNAME      = "LastConnectedUsername";
    private static final String LAST_CONNECTED_IP            = "LastConnectedIP";
    private static final String LIGHTS            = "Lights";
   // private  List<String> lightList = new ArrayList<String>();
    private static HueSharedPreferences instance = null;
    private SharedPreferences mSharedPreferences = null;
    
    private Editor mSharedPreferencesEditor = null;
    
    
    public void create() {
      
    }
    
    public static HueSharedPreferences getInstance(Context ctx) {
        if (instance == null) {
            instance = new HueSharedPreferences(ctx);
        }
        return instance;
    }
    
    private HueSharedPreferences(Context appContext) {
        mSharedPreferences = appContext.getSharedPreferences(HUE_SHARED_PREFERENCES_STORE, 0); // 0 - for private mode
        mSharedPreferencesEditor = mSharedPreferences.edit();
    }
    
    public String getLights() {
        String lights = mSharedPreferences.getString(LIGHTS, "");
   	 	return lights;
	} 
    public String getUsername() {
         String username = mSharedPreferences.getString(LAST_CONNECTED_USERNAME, "");
    	 return username;
	}

	public boolean setUsername(String username) {
        mSharedPreferencesEditor.putString(LAST_CONNECTED_USERNAME, username);
        return (mSharedPreferencesEditor.commit());
	}
	
	public boolean setLights(List<String> lightList) {
		String light = "";
		for (int i=0; i<lightList.size(); i++) {
			light = light + "," + lightList.get(i);
		}
		for (String licht : lightList) {
			
		}
		
		mSharedPreferencesEditor.putString(LIGHTS, light);
        return (mSharedPreferencesEditor.commit());
	}
    
    public String getLastConnectedIPAddress() {
        return mSharedPreferences.getString(LAST_CONNECTED_IP, "");
   }

   public boolean setLastConnectedIPAddress(String ipAddress) {
       mSharedPreferencesEditor.putString(LAST_CONNECTED_IP, ipAddress);
       return (mSharedPreferencesEditor.commit());
   }
}
