package com.cordova.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.widget.Toast;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Nearable;
import java.util.List;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;


/**
 * This class echoes a string called from JavaScript.
 */
public class AndroidNearables extends CordovaPlugin {
    private BeaconManager beaconManager;
    private List nearableList;
    public Timer timer = new Timer();


    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("getNearables")) {
            beaconManager = new BeaconManager(webView.getContext());
            this.getNearables(callbackContext);
            return true;
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            Toast.makeText(
                    webView.getContext(),
                    message,
                    Toast.LENGTH_LONG
                  ).show();
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private void getNearables(final CallbackContext callbackContext) {
        final JSONArray nearableArray = new JSONArray();

        beaconManager.setNearableListener(new BeaconManager.NearableListener() {
            @Override public void onNearablesDiscovered(List<Nearable> nearables) {
                for(Nearable nearable : nearables) {
                    try {
                        JSONObject nearableObj = new JSONObject();
                        nearableObj.put("identifier", nearable.identifier);
                        if(!nearableArray.toString().contains("\"identifier\":\""+nearable.identifier+"\"")) {
                            nearableArray.put(nearableObj);
                        }
                    } catch(JSONException e) {
                        Log.e("AndroidNearables", "Invalid JSON String: " + e);
                    }
                }
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        beaconManager.disconnect();
                        callbackContext.success(nearableArray);
                    }
                }, 4000);

            }
        });

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override public void onServiceReady() {
                beaconManager.startNearableDiscovery();
            }
        });
    }
}
