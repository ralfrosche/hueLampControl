package com.philips.lighting.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;


import android.annotation.SuppressLint;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.Switch;

import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;
import com.philips.lighting.quickstart.R;

/**
 * This class provides adapter view for a list of Found Bridges.
 * 
 * @author SteveyO.
 */
public class LampListAdapter extends BaseAdapter implements OnSeekBarChangeListener, OnCheckedChangeListener{
    private static final String TAG = "QuickStart";
	private LayoutInflater mInflater;
    private List<PHLight> allLights;
    private PHBridge bridge;
    private List<Integer> aR = new ArrayList<Integer>(); 
    private List<Integer> PV = new ArrayList<Integer>(); 

    class LampListItem {
        private TextView lampName;
        //private TextView lampId;
        private TextView lampType;
        private SeekBar seekBar;
        private Switch switchPlug;
    }
         
    public LampListAdapter(Context context, List<PHLight> allLights, PHBridge bridge) {
        mInflater = LayoutInflater.from(context);
        this.allLights = allLights;
        this.bridge = bridge;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LampListItem item;
        if (!aR.contains(position)) {
        	aR.add(position);
        	PV.add(0);
        }
        
        if (convertView == null || aR.contains(position)){
        	
        	PHLight light = allLights.get(position);
        	String modelNumber = light.getModelNumber();
        	if (modelNumber.contains("Plug")){
        		convertView = mInflater.inflate(R.layout.plug_item, null);
                item = new LampListItem();
                item.lampName = (TextView) convertView.findViewById(R.id.plug_name);
                item.lampType = (TextView) convertView.findViewById(R.id.plug_type);
                item.switchPlug = (Switch) convertView.findViewById(R.id.switchPlug);
                item.switchPlug.setOnCheckedChangeListener(this);
               convertView.setTag(item);
        	
        	} else {
        		convertView = mInflater.inflate(R.layout.lamp_item, null);
                item = new LampListItem();
                item.lampName = (TextView) convertView.findViewById(R.id.lamp_name);
                //item.lampId = (TextView) convertView.findViewById(R.id.lamp_id);
                item.lampType = (TextView) convertView.findViewById(R.id.lamp_type);
                item.seekBar = (SeekBar) convertView.findViewById(R.id.seekBar1);
                item.seekBar.setOnSeekBarChangeListener(this);
                convertView.setTag(item);
        		
        	}
         
    }else{
        item = (LampListItem) convertView.getTag();
    }
       PHLight light = allLights.get(position);
       item.lampName.setTextColor(Color.BLACK);
       item.lampName.setText(light.getName());
       item.lampType.setTextColor(Color.DKGRAY);
       item.lampType.setText(light.getModelNumber());
   	   String modelNumber = light.getModelNumber();
       if (modelNumber.contains("Plug")){
    	   item.switchPlug.setId(position);
       } else {
    	   item.seekBar.setId(position);
         }
       
       int brightness = 0;
       int localBrightness = PV.get(position);
       try {
    	   PHLightState lightState = new PHLightState();
    	   lightState = light.getLastKnownLightState();
    	   brightness = lightState.getBrightness();
           
       }
       catch( Exception e){
    	   e.printStackTrace();
       }
       if (localBrightness > brightness) {
    	   brightness = localBrightness;
       }
       Log.e(TAG, "getViewsetBrigthness:"+brightness);
       if (modelNumber.contains("Plug")){
    	   // item.switchPlug.setChecked(false);
    	   // TODO: restore state of Plug type lamps  
    	   
       } else {
           item.seekBar.setProgress(brightness);
       }

       try{
    	   PHLightState lightStateOld = new PHLightState();
    	   modelNumber = light.getModelNumber();

	  		if (!modelNumber.contains("Plug")){
	  			if (brightness >=10){
	  				lightStateOld.setOn(true);
	  				lightStateOld.setBrightness(brightness);
	    	    } else {
	    	    	lightStateOld.setOn(false);
	    	    	lightStateOld.setBrightness(0);
	    	   }
		    	bridge.updateLightState(light, lightStateOld);
	  		}

       }
       catch(Exception e){
    	   e.printStackTrace();
    	   
       }
       return convertView;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return allLights.size();
    }

    @Override
    public Object getItem(int position) {
        return allLights.get(position);
    }

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		int id = seekBar.getId();
		PV.set(id,progress);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		int id = seekBar.getId();
		int value = PV.get(id);
		PHLight light = allLights.get(id);
       	PHLightState lightState = new PHLightState();
       	if (value < 10){
       		lightState.setBrightness(0);
           	lightState.setOn(false);
       	}else {
           	lightState.setOn(true);
            lightState.setBrightness(value);
       	}
        bridge.updateLightState(light, lightState);
		
	}
   
    public void updateData(List<PHLight> allLights) {
        this.allLights = allLights;
        notifyDataSetChanged();
    }


	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int id = buttonView.getId();
		Log.e(TAG, "Plug: "+isChecked + " id:" +id);
		PHLight light = allLights.get(id);
	    PHLightState lightState = new PHLightState();
	    if (isChecked == true) {
	        // lightState.setBrightness(254);
	    	lightState.setOn(true);
	    	Log.e(TAG, "Plug: " + id + " turn on");

 	    } else {
	       // lightState.setBrightness(0);
		    lightState.setOn(false);
		    Log.e(TAG, "Plug: " + id + " turn off");

	    }
        bridge.updateLightState(light, lightState);
		
	}
    
    
	/*
    public void updateData(List<String[]> allLights) {
        this.allLights = allLights;
        notifyDataSetChanged();
    }
    */

}