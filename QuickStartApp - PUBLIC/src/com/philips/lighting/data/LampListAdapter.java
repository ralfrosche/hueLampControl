package com.philips.lighting.data;

import java.util.ArrayList;
import java.util.List;


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
    private Context context;
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
        private TextView lampId;
        private TextView lampType;
        private SeekBar seekBar;
		private int progress;
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
        this.context = context;
        this.bridge = bridge;
    }

    /**
     * Get a View that displays the data at the specified position in the data set.
     * 
     * @param position      The row index.
     * @param convertView   The row view.
     * @param parent        The view group.
     * @return              A View corresponding to the data at the specified position.
     */
    @SuppressLint("NewApi")
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
            item.lampId = (TextView) convertView.findViewById(R.id.lamp_id);
            item.lampType = (TextView) convertView.findViewById(R.id.lamp_type);
            item.seekBar = (SeekBar) convertView.findViewById(R.id.seekBar1);
            item.seekBar.setOnSeekBarChangeListener(this);
           convertView.setTag(item);
            
           
    }else{
        item = (LampListItem) convertView.getTag();
    }


       PHLight light = allLights.get(position);
        //String[] light = allLights.get(position);
       // Log.d(TAG,"light:"+light );
       item.lampName.setTextColor(Color.BLACK);
        item.lampName.setText(light.getName());
       // item.lampName.setText(light[0]);
        item.lampType.setTextColor(Color.DKGRAY);
        item.lampType.setText(light.getModelNumber());
       // item.lampType.setText(light[2]);
        item.lampId.setTextColor(Color.DKGRAY);
        item.lampId.setText(light.getIdentifier());
        //item.lampId.setText(light[1]);
        item.seekBar.setId(position);
        item.seekBar.setProgress(PV.get(position));

        
        return convertView;
    }

    /**
     * Get the row id associated with the specified position in the list.
     * 
     * @param position  The row index.
     * @return          The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     * 
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return allLights.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     * 
     * @param position      Position of the item whose data we want within the adapter's data set.
     * @return              The data at the specified position.
     */
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
		// TODO Auto-generated method stub
		int id = seekBar.getId();
		int value = PV.get(id);
		Log.d(TAG, "id:"+seekBar.getId()+" value:"+ value);
		PHLight light = allLights.get(id);
       	PHLightState lightState = new PHLightState();
        lightState.setOn(true);
        lightState.setBrightness(value);
        bridge.updateLightState(light, lightState);
		
	}

    /**
     * Update date of the list view and refresh listview.
     * 
     * @param accessPoints      An array list of {@link PHAccessPoint} objects.
     */
    
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