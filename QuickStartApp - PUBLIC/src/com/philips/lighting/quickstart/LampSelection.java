package com.philips.lighting.quickstart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ListView;


import com.philips.lighting.data.LampListAdapter;
import com.philips.lighting.data.LampSelectionAdapter;
import com.philips.lighting.hue.listener.PHLightListener;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

/**
 * MyApplicationActivity - The starting point for creating your own Hue App.  
 * Currently contains a simple view with a button to change your lights to random colours.  Remove this and add your own app implementation here! Have fun!
 * 
 * @author SteveyO
 *
 */
public class LampSelection extends Activity{
    private PHHueSDK phHueSDK;
    private static final int MAX_HUE=65535;
    public static final String TAG = "QuickStart";
    private LampListAdapter adapter;
    private LampSelectionAdapter adapterSelection;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.lampselectionlinear);
        phHueSDK = PHHueSDK.create();
        chooseLights();
    }

 
    public void listLights(){
    	PHBridge bridge = phHueSDK.getSelectedBridge();
    	
  
    	// Log.d(TAG, "lamps:"+allLights);
    	List<PHLight> allLights = new ArrayList<PHLight>();
    	/*for (PHLight light : allLightsTemp) {
    		String modelNumber = light.getModelNumber();
    		if (!modelNumber.contains("Plug")){
       			allLights.add(light);
    		}
        
        }
    	 adapter = new LampListAdapter(getApplicationContext(), allLights, bridge);
         ListView LampList = (ListView) findViewById(R.id.lamp_list);
         LampList.setAdapter(adapter);
         */
    	
    }
     
    @Override
    protected void onDestroy() {
       
            super.onDestroy();
        
    }
   
 
    private void chooseLights(){
    	PHBridge bridge = phHueSDK.getSelectedBridge();
    	List<PHLight> allLights = bridge.getResourceCache().getAllLights();
      	/*
    	List<String[]> allLights = new ArrayList<String[]>();
    	String[] light = new String[3];
    	String[] light1 = new String[3];
    	String[] light2 = new String[3];
    	String[] light3 = new String[3];
     	String[] light4 = new String[3];
    	String[] light5 = new String[3];
    	String[] light6 = new String[3];
    	String[] light7 = new String[3];
    	String[] light8 = new String[3];

    	light[0] = "test1";
    	light[1] = "id1";
    	light[2] = "type1";
    	allLights.add(light);

    	light1[0] = "test2";
    	light1[1] = "id2";
    	light1[2] = "type2";
    	allLights.add(light1);
    	light2[0] = "test3";
    	light2[1] = "id3";
    	light2[2] = "type3";
    	allLights.add(light2);
    	
    	light3[0] = "test4";
    	light3[1] = "id4";
    	light3[2] = "type4";
    	allLights.add(light3);
    	light4[0] = "test5";
    	light4[1] = "id5";
    	light4[2] = "type5";
    	allLights.add(light4);
    	
    	light5[0] = "test6";
    	light5[1] = "id6";
    	light5[2] = "type6";
    	allLights.add(light5);
    	light6[0] = "test7";
    	light6[1] = "id7";
    	light6[2] = "type7";
    	allLights.add(light6);
    	
    	light7[0] = "test8";
    	light7[1] = "id8";
    	light7[2] = "type8";
    	allLights.add(light7);
    	light8[0] = "test9";
    	light8[1] = "id9";
    	light8[2] = "type9";
    	allLights.add(light8);
    	*/
    	adapterSelection = new LampSelectionAdapter(getApplicationContext(), allLights, bridge);
        ListView LampList = (ListView) findViewById(R.id.lamp_selection);
        LampList.setAdapter(adapterSelection);
  
    	
    	// Log.d(TAG, "lamps:"+allLights);
    }

}
