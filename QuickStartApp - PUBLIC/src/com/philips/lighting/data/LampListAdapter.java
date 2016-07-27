package com.philips.lighting.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;


import android.annotation.SuppressLint;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;

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
public class LampListAdapter extends BaseAdapter implements OnSeekBarChangeListener {
    private static final String TAG = "QuickStart";
	private LayoutInflater mInflater;
    private List<PHLight> allLights;
   // private List<String[]> allLights;
    private PHBridge bridge;
    private List<Integer> aR = new ArrayList<Integer>(); 
    private List<Integer> PV = new ArrayList<Integer>(); 
    /**
     * View holder class for access point list.
     * 
     * @author SteveyO.
     */
    class LampListItem {
        private TextView lampName;
        //private TextView lampId;
        private TextView lampType;
        private SeekBar seekBar;
    }

    /**
     * creates instance of {@link LampListAdapter} class.
     * 
     * @param context           the Context object.
     * @param accessPoints      an array list of {@link PHAccessPoint} object to display.
     */
        
    public LampListAdapter(Context context, List<PHLight> allLights, PHBridge bridge) {
   //	 public LampListAdapter(Context context, List<String[]> allLights, PHBridge bridge) {
        // Cache the LayoutInflate to avoid asking for a new one each time.
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
            convertView = mInflater.inflate(R.layout.lamp_item, null);
            item = new LampListItem();
            item.lampName = (TextView) convertView.findViewById(R.id.lamp_name);
            //item.lampId = (TextView) convertView.findViewById(R.id.lamp_id);
            item.lampType = (TextView) convertView.findViewById(R.id.lamp_type);
            item.seekBar = (SeekBar) convertView.findViewById(R.id.seekBar1);
            item.seekBar.setOnSeekBarChangeListener(this);
           convertView.setTag(item);
            
           
    }else{
        item = (LampListItem) convertView.getTag();
    }


       PHLight light = allLights.get(position);
       item.lampName.setTextColor(Color.BLACK);
       item.lampName.setText(light.getName());
       item.lampType.setTextColor(Color.DKGRAY);
       item.lampType.setText(light.getModelNumber());
       item.seekBar.setId(position);
       Log.e(TAG, "getViewBefore LigthState:"+position);
       
       int brightness = 0;
       int localBrightness = PV.get(position);
       try {
    	   PHLightState lightState = new PHLightState();
    	   lightState = allLights.get(position).getLastKnownLightState();
    	   brightness = lightState.getBrightness();
           
       }
       catch( Exception e){
    	   e.printStackTrace();
       }
       if (localBrightness > brightness) {
    	   brightness = localBrightness;
       }
       Log.e(TAG, "getViewsetBrigthness:"+brightness);
       item.seekBar.setProgress(brightness);
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
    
    
	/*
    public void updateData(List<String[]> allLights) {
        this.allLights = allLights;
        notifyDataSetChanged();
    }
    */

}