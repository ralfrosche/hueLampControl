package com.philips.lighting.quickstart;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    public int CALLBACK_REQUEST_LAMPS = 1616;
    public int CALLBACK_REQUEST_SCENES = 1617;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.lamplistlinear);
        phHueSDK = PHHueSDK.create();
        prefs = HueSharedPreferences.getInstance(this);
        
        
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean showplugs = sharedPref.getBoolean("showPlugs", false);

 
        
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
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean showplugs = sharedPref.getBoolean("showPlugs", false);
    	for (PHLight light : allLightsTemp) {
    		String modelNumber = light.getModelNumber();
    		String name = light.getName();
    		if ((!modelNumber.contains("Plug") || showplugs == true) && selectedLampsString.contains(name)){
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
	   case R.id.help:
		 launchHelp();
		     break;
	   case R.id.prefs:
		   Intent intent = new Intent(this, PreferencesActivity.class);
			startActivity(intent);
			break;
	   case R.id.selectScene:
		   doASelectScenes();
		   break;
		   
        }
        return true;
    }
    

	private void launchHelp() {
		Intent intent = new Intent(this, help.class);
		startActivity(intent);
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CALLBACK_REQUEST_LAMPS) {
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
            	
            } else {
            	Log.e(TAG, "onActivityResultError:"+resultCode);
    	
            }
        }else if (requestCode == CALLBACK_REQUEST_SCENES) {
        	
        	Log.e(TAG, "onActivityResult: CALLBACK_REQUEST_SCENES");
        	// do something after scene selection
        } else {
        	Log.e(TAG, "onActivityRequestError:"+requestCode);
        }
    }
    private void doASelectLamps(){
    	   Intent intent = new Intent(getApplicationContext(), LampSelection.class);
    	   startActivityForResult(intent, CALLBACK_REQUEST_LAMPS);
          // startActivity(intent);
    	
    }
    private void doASelectScenes(){
 	   Intent intent = new Intent(getApplicationContext(), SceneSelection.class);
 	   startActivityForResult(intent, CALLBACK_REQUEST_SCENES);
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
       listLights();
    }
}
