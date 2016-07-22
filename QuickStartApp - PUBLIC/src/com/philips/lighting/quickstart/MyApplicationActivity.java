package com.philips.lighting.quickstart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ListView;


import com.philips.lighting.data.LampListAdapter;
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
public class MyApplicationActivity extends Activity implements OnItemClickListener {
    private PHHueSDK phHueSDK;
    private static final int MAX_HUE=65535;
    public static final String TAG = "QuickStart";
    private LampListAdapter adapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        
        // setContentView(R.layout.activity_main);
        setContentView(R.layout.lamplistlinear);
        phHueSDK = PHHueSDK.create();
        listLights();
       /*
        Button randomButton;
        randomButton = (Button) findViewById(R.id.buttonRand);
        randomButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                randomLights();
            }

        });
        */

    }

 
    public void listLights(){
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
    	// Log.d(TAG, "lamps:"+allLights);
    	
    	 adapter = new LampListAdapter(getApplicationContext(), allLights, bridge);
    
         ListView LampList = (ListView) findViewById(R.id.lamp_list);
         LampList.setAdapter(adapter);
         
    	
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	PHBridge bridge = phHueSDK.getSelectedBridge();
       	PHLight Light = (PHLight) adapter.getItem(position);
       	PHLightState lightState = new PHLightState();
        lightState.setHue(MAX_HUE);
        bridge.updateLightState(Light, lightState, listener);
       	
    	//Toast.makeText(this, "Position: " + position, Toast.LENGTH_SHORT).show();
      
      
    
    }
    
    public void randomLights() {
        PHBridge bridge = phHueSDK.getSelectedBridge();

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        Random rand = new Random();
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        for (PHLight light : allLights) {
            PHLightState lightState = new PHLightState();
            lightState.setHue(rand.nextInt(MAX_HUE));
            // To validate your lightstate is valid (before sending to the bridge) you can use:  
            // String validState = lightState.validateState();
            bridge.updateLightState(light, lightState, listener);
            //  bridge.updateLightState(light, lightState);   // If no bridge response is required then use this simpler form.
        }
    }
    // If you want to handle the response from the bridge, create a PHLightListener object.
    PHLightListener listener = new PHLightListener() {
        
        @Override
        public void onSuccess() {  
        }
        
        @Override
        public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
           Log.w(TAG, "Light has updated");
        }
        
        @Override
        public void onError(int arg0, String arg1) {}

        @Override
        public void onReceivingLightDetails(PHLight arg0) {}

        @Override
        public void onReceivingLights(List<PHBridgeResource> arg0) {}

        @Override
        public void onSearchComplete() {}
    };
    
    @Override
    protected void onDestroy() {
        PHBridge bridge = phHueSDK.getSelectedBridge();
        if (bridge != null) {
            
            if (phHueSDK.isHeartbeatEnabled(bridge)) {
                phHueSDK.disableHeartbeat(bridge);
            }
            
            phHueSDK.disconnect(bridge);
            super.onDestroy();
        }
    }

}
