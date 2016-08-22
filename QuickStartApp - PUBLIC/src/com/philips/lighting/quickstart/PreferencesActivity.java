package com.philips.lighting.quickstart;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class PreferencesActivity extends PreferenceActivity {
	 public static String showPlugs;

	@Override
	    protected void onCreate(final Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
	    }

	    public static class MyPreferenceFragment extends PreferenceFragment
	    {
	        @Override
	        public void onCreate(final Bundle savedInstanceState)
	        {
	            super.onCreate(savedInstanceState);
	            addPreferencesFromResource(R.xml.preferencies);
	        }
	    }
}

