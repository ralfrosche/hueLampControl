package com.philips.lighting.data;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.quickstart.R;

public class LampSelectionAdapter extends BaseAdapter implements OnCheckedChangeListener {
	private LayoutInflater mInflater;
    private List<PHLight> allLights;
   //private List<String[]> allLights;
   public static final String TAG = "QuickStart";
    private List<Integer> selectedLamps = new ArrayList<Integer>(); 
    private List<String> selectedLampsString = new ArrayList<String>(); 
    private HueSharedPreferences prefs;
    private Context context;
    private String lightList;

    class LampListItem {
        private TextView lampName;
        private CheckBox checkbox;

    }
    public LampSelectionAdapter(Context context, List<PHLight> allLights, PHBridge bridge) {
   	// public LampSelectionAdapter(Context context, List<String[]> allLights) {
        mInflater = LayoutInflater.from(context);
        this.allLights = allLights;
        this.context = context;

		prefs = HueSharedPreferences.getInstance(this.context);
        this.lightList = prefs.getLights();
        String[] temp = this.lightList.split(",");
        for (String lamp: temp) {
        	if (!lamp.equals(""))
        	   selectedLampsString.add(lamp);
       }
      }
	public View getView(final int position, View convertView, ViewGroup parent) {

        LampListItem item;
        if (!selectedLamps.contains(position)) {
        	selectedLamps.add(position);
        }

        if (convertView == null || selectedLamps.contains(position)){
            convertView = mInflater.inflate(R.layout.lamp_item_for_selection, null);
            item = new LampListItem();
            item.lampName = (TextView) convertView.findViewById(R.id.lampSelection);
            item.checkbox = (CheckBox) convertView.findViewById(R.id.checkBox1);
            item.checkbox.setOnCheckedChangeListener(this);
             convertView.setTag(item);
            
           
    }else{
        item = (LampListItem) convertView.getTag();
    }
        //String[] light = allLights.get(position);
        PHLight light = allLights.get(position);
        item.lampName.setTextColor(Color.BLACK);
       // item.lampName.setText(light[0]);
        item.lampName.setText(light.getName());
        item.checkbox.setId(position);
        item.checkbox.setTextColor(Color.BLACK);
        if (selectedLampsString.contains(item.lampName.getText())){
        	item.checkbox.setChecked(true);
        }
    
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

    @Override
    public Object getItem(int position) {
        return allLights.get(position);
    }


    public void updateData(List<PHLight> allLights) {
        this.allLights = allLights;
        notifyDataSetChanged();
    }
 
    @Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		  int id = buttonView.getId();
		  PHLight light = allLights.get(id);
		  String name = light.getName();

		  if (buttonView.isChecked()) {
			  if (!selectedLampsString.contains(name)) {
				  selectedLampsString.add(name);
			  }			  
		  } else {
			  if (selectedLampsString.contains(name)) {
				  selectedLampsString.remove(name);
			  }		
		  }
		  if (selectedLampsString.size()>0) {
			  prefs = HueSharedPreferences.getInstance(this.context);
			  prefs.setLights(selectedLampsString);
		  }
	
	}

}