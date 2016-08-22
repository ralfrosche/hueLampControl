package com.philips.lighting.data;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.Button;

import android.widget.TextView;

import com.philips.lighting.hue.listener.PHSceneListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHScene.PHSceneActiveState;

import com.philips.lighting.model.PHScene;
import com.philips.lighting.quickstart.R;

public class SceneSelectionAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
    private List<PHScene> allScenes;
    public static final String TAG = "QuickStart";
    private List<Integer> selectedLScenes = new ArrayList<Integer>(); 
    private PHBridge bridge;
    public static Button testButton;

    class SceneListItem {
        private TextView sceneName;
        private Button sceneButton;

    }
    public SceneSelectionAdapter(Context context, List<PHScene> allScenes, PHBridge bridge) {
        mInflater = LayoutInflater.from(context);
        this.allScenes = allScenes;
        this.bridge = bridge;
      }
	@SuppressLint("InflateParams")
	public View getView(final int position, View convertView, ViewGroup parent) {
        SceneListItem item;
        if (!selectedLScenes.contains(position)) {
        	selectedLScenes.add(position);
        }

        if (convertView == null || selectedLScenes.contains(position)){
            convertView = mInflater.inflate(R.layout.scene_item, null);
            item = new SceneListItem();
            item.sceneName = (TextView) convertView.findViewById(R.id.scene_selection);
            item.sceneButton = (Button) convertView.findViewById(R.id.scene_button);
            testButton =  item.sceneButton;
            item.sceneButton.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					int id = v.getId();
					Log.e(TAG, "sceneId: "+id);
					PHScene scene = allScenes.get(id);
					String sceneId = scene.getSceneIdentifier();
					Log.e(TAG, "sceneId: "+sceneId);
					try{
					bridge.activateScene(sceneId, "0", new PHSceneListener() {

						@Override
						public void onError(int arg0, String arg1) {
							// TODO Auto-generated method stub
							Log.e(TAG, "onError: "+arg1);
						}

						@Override
						public void onStateUpdate(Map<String, String> arg0,
								List<PHHueError> arg1) {
							Log.e(TAG, "onStateUpdate: "+arg1);
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							Log.e(TAG, "onSuccess: ");
							
						}

						@Override
						public void onSceneReceived(PHScene arg0) {
							// TODO Auto-generated method stub
							Log.e(TAG, "onSceneReceived: "+arg0);
							
						}

						@Override
						public void onScenesReceived(List<PHScene> arg0) {
							Log.e(TAG, "onScenesReceived: "+arg0);
							// TODO Auto-generated method stub
							
						}});

					} catch( Exception e){
				    	   e.printStackTrace();
				    }
					//testButton.setText("");
					// Button sceneButton = (Button) convertView.findViewById(R.id.scene_button);
					
					// item.sceneButton.setTextColor(Color.RED);

				
				}
            });
            convertView.setTag(item);
        } else{
        	item = (SceneListItem) convertView.getTag();
        }
        
        PHScene scene = allScenes.get(position);
        item.sceneName.setTextColor(Color.BLACK);
        item.sceneName.setText(scene.getName());
        item.sceneButton.setId(position);
        item.sceneButton.setTextColor(Color.BLACK);
        return convertView;
    }
	
	@Override
    public long getItemId(int position) {
        return position;
    }
	
    @Override
    public int getCount() {
        return allScenes.size();
    }
    
    @Override
    public Object getItem(int position) {
        return allScenes.get(position);
    }

    public void updateData(List<PHScene> allScenes) {
        this.allScenes = allScenes;
        notifyDataSetChanged();
    }
 }