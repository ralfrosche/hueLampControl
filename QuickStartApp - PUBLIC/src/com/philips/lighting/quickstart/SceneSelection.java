package com.philips.lighting.quickstart;


import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import com.philips.lighting.data.SceneSelectionAdapter;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHScene;

public class SceneSelection extends Activity{
    private PHHueSDK phHueSDK;
    public static final String TAG = "QuickStart";
    private SceneSelectionAdapter adapterSelection;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.scenelistlinear);
        phHueSDK = PHHueSDK.create();
        Intent intent = new Intent ();
        setResult (RESULT_OK, intent);
        chooseScenes();
    }
 
    @Override
    protected void onDestroy() {
            super.onDestroy();
    }

    private void chooseScenes(){
    	PHBridge bridge = phHueSDK.getSelectedBridge();
    	List<PHScene> allScenes = bridge.getResourceCache().getAllScenes();
    	adapterSelection = new SceneSelectionAdapter(getApplicationContext(), allScenes, bridge);
        ListView SceneList = (ListView) findViewById(R.id.scene_selection);
        SceneList.setAdapter(adapterSelection);
    }

}
