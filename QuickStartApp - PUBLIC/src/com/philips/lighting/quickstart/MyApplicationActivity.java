package com.philips.lighting.quickstart;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.philips.lighting.data.HueSharedPreferences;
import com.philips.lighting.data.LampListAdapter;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

public class MyApplicationActivity extends Activity {
    private PHHueSDK phHueSDK;
    public static final String TAG = "QuickStart";
    private LampListAdapter adapter;
    private HueSharedPreferences prefs;
    private String lightList;
    private List<String> selectedLampsString = new ArrayList<String>(); 
    public int CALLBACK_REQUEST = 1616;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.lamplistlinear);
        phHueSDK = PHHueSDK.create();
        prefs = HueSharedPreferences.getInstance(this);
        this.lightList = prefs.getLights();
        String[] temp = this.lightList.split(",");
        for (String lamp: temp) {
        	if (!lamp.equals(""))
        	   selectedLampsString.add(lamp);
       }
        Log.e(TAG, "selectedLampsString:"+selectedLampsString.toString());
        listLights();
    }
    @Override
    public void onResume() {
    	super.onResume();
    	selectedLampsString.clear();
    	   prefs = HueSharedPreferences.getInstance(this);
           this.lightList = prefs.getLights();
           String[] temp = this.lightList.split(",");
           for (String lamp: temp) {
           	if (!lamp.equals(""))
           	   selectedLampsString.add(lamp);
          }
           Log.e(TAG, "onResume:"+selectedLampsString.toString());
          
    	
    }

 
    public void listLights(){
    	 Log.e(TAG, "listLights");
    	PHBridge bridge = phHueSDK.getSelectedBridge();
      	List<PHLight> allLightsTemp = bridge.getResourceCache().getAllLights();
      	List<PHLight> allLights = new ArrayList<PHLight>();
    	for (PHLight light : allLightsTemp) {
    		String modelNumber = light.getModelNumber();
    		String name = light.getName();
    		if (!modelNumber.contains("Plug") && selectedLampsString.contains(name)){
       			allLights.add(light);
    		}
        
        }
    	 adapter = new LampListAdapter(getApplicationContext(), allLights, bridge);
         ListView LampList = (ListView) findViewById(R.id.lamp_list);
         LampList.setAdapter(adapter);
         
    	
    }
    
 
    @Override
    protected void onDestroy() {
    	 super.onDestroy();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        if (bridge != null) {
            
            if (phHueSDK.isHeartbeatEnabled(bridge)) {
                phHueSDK.disableHeartbeat(bridge);
            }
            
            phHueSDK.disconnect(bridge);
           
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.w(TAG, "Inflating lamp menu");
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.allOff:
            doAllOff();
            break;
        
	    case R.id.selectLamps:
	        doASelectLamps();
	        break;
	    }
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CALLBACK_REQUEST) {
            if (resultCode == RESULT_OK) {
            	selectedLampsString.clear();
         	    prefs = HueSharedPreferences.getInstance(this);
                this.lightList = prefs.getLights();
                String[] temp = this.lightList.split(",");
                for (String lamp: temp) {
                	if (!lamp.equals(""))
                	   selectedLampsString.add(lamp);
               }
                Log.e(TAG, "onActivityResult:"+selectedLampsString.toString());
                listLights();
            	
            }
        }
    }
    private void doASelectLamps(){
    	   Intent intent = new Intent(getApplicationContext(), LampSelection.class);
    	   startActivityForResult(intent, CALLBACK_REQUEST);
          // startActivity(intent);
    	
    }
    private void doAllOff(){
   	   PHBridge bridge = phHueSDK.getSelectedBridge();
       List<PHLight> allLights = bridge.getResourceCache().getAllLights();
       for (PHLight light : allLights) {
           PHLightState lightState = new PHLightState();
           lightState.setOn(false);
           lightState.setBrightness(0);
           try {
			Thread.sleep(50);
           } catch (InterruptedException e) {
			e.printStackTrace();
		}
           bridge.updateLightState(light, lightState);
       }
    }
}
