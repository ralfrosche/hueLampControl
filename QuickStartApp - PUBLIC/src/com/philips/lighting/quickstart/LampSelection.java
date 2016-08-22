package com.philips.lighting.quickstart;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;
import com.philips.lighting.data.LampSelectionAdapter;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;

public class LampSelection extends Activity{
    private PHHueSDK phHueSDK;
    public static final String TAG = "QuickStart";
    private LampSelectionAdapter adapterSelection;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.lampselectionlinear);
        phHueSDK = PHHueSDK.create();
        Intent intent = new Intent ();
        setResult (RESULT_OK, intent);
        chooseLights();
    }

    @Override
    protected void onDestroy() {
             super.onDestroy();
    }
   
    private void chooseLights(){
    	PHBridge bridge = phHueSDK.getSelectedBridge();
    	List<PHLight> allLights = new ArrayList<PHLight>();
    	List<PHLight> allLightsTemp = bridge.getResourceCache().getAllLights();
    	for (PHLight light : allLightsTemp) {
    		String modelNumber = light.getModelNumber();
    		 SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    	        Boolean showplugs = sharedPref.getBoolean("showPlugs", false);
    		if (!modelNumber.contains("Plug") || showplugs == true){
       			allLights.add(light);
    		}
         }
    	adapterSelection = new LampSelectionAdapter(getApplicationContext(), allLights, bridge);
        ListView LampList = (ListView) findViewById(R.id.lamp_selection);
        LampList.setAdapter(adapterSelection);
      }

}
