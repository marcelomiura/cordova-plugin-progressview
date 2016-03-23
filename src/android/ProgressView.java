/****************************************
 *
 *  ProgressView.java
 *  Cordova ProgressView
 *
 *  Created by Sidney Bofah on 2014-11-20.
 *
 ****************************************/

package de.neofonie.cordova.plugin.progressview;

import android.app.ProgressDialog;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

class ProgressViewData {

     public String message;
     public Integer theme;
     public int shape;
     public boolean indeterminate;

     public ProgressViewData() {

        this.message = null;
        this.theme = 5; // ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT;
        this.shape = ProgressDialog.STYLE_HORIZONTAL;
        this.indeterminate = false;
     }   
}

public class ProgressView extends CordovaPlugin {

    private static String LOG_TAG = "ProgressView";
    private static ProgressDialog progressViewObj = null;

    private String lastMessage = "";
    private Integer lastTheme = null;
    private CallbackContext callback = null;

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action          {String}  The action to execute.
     * @param args            {String} The exec() arguments in JSON form.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return Whether the action was valid.
     */
    @Override
    public boolean execute(String action, String args, CallbackContext callbackContext) {

        Log.d(LOG_TAG, "executing a action..." + action);

        /*
         * Don't run any of these if the current activity is finishing in order
         * to avoid android.view.WindowManager$BadTokenException crashing the app.
         */
        callback = callbackContext;

        if (this.cordova.getActivity().isFinishing()) {
            return true;
        }
        if (action.equals("show")) {
            this.show(args);
        } else if (action.equals("setProgress")) {
            this.setProgress(args);
        } else if (action.equals("setLabel")) {
            this.setLabel(args);
        } else if (action.equals("update")) {
            this.update(args);
        } else if (action.equals("hide")) {
            this.hide();
        }
        return true;
    }


    /**
     * Show Dialog
     */
    private void show(final String args) {
        Log.d(LOG_TAG, args);
        final CordovaInterface cordova = this.cordova;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                // Check State
                if (ProgressView.progressViewObj != null) {
                    PluginResult result = new PluginResult(PluginResult.Status.ERROR, "(Cordova ProgressView) (show) ERROR: Dialog already showing");
                    result.setKeepCallback(true);
                    callback.sendPluginResult(result);
                    return;
                }

                ProgressViewData currentData = parseData(args);

                lastTheme = currentData.theme;
                lastMessage = currentData.message;

                ProgressView.progressViewObj = getProgressDialog(currentData);
                ProgressView.progressViewObj.show();

                // Callback
                PluginResult result = new PluginResult(PluginResult.Status.OK, "(Cordova ProgressView) (show) OK");
                result.setKeepCallback(true);
                callback.sendPluginResult(result);
            }

            ;
        };
        this.cordova.getActivity().runOnUiThread(runnable);
    }
    
    

    /**
     * Set Progress
     */
    private void setProgress(final String args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                // Check State
                if (ProgressView.progressViewObj == null) {
                    PluginResult result = new PluginResult(PluginResult.Status.ERROR, "(Cordova ProgressView) (setProgress) ERROR: No dialog to update");
                    result.setKeepCallback(true);
                    callback.sendPluginResult(result);
                    return;
                }

                // Get Arguments
                JSONArray argsObj = null;
                try {
                    argsObj = new JSONArray(args);
                } catch (JSONException e) {
                    // e.printStackTrace();
                }

                // Convert variable number types
                Double doubleValue = 0.0;
                Integer intValue;
                try {
                    doubleValue = argsObj.getDouble(0);
                    doubleValue = doubleValue * 100;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intValue = doubleValue.intValue();

                // Update Progress
                ProgressView.progressViewObj.setProgress(intValue);

                // Callback
                PluginResult result = new PluginResult(PluginResult.Status.OK, "(Cordova ProgressView) (setProgress) OK");
                result.setKeepCallback(true);
                callback.sendPluginResult(result);
            }
        };
        this.cordova.getActivity().runOnUiThread(runnable);
    }
    
    

    /**
     * Set Progress
     */
    private void update(final String args) {
        Log.d(LOG_TAG, "shape here===============" + args);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                // Check State
                if (ProgressView.progressViewObj == null) {
                    PluginResult result = new PluginResult(PluginResult.Status.ERROR, "(Cordova ProgressView) (setProgress) ERROR: No dialog to update");
                    result.setKeepCallback(true);
                    callback.sendPluginResult(result);
                    return;
                }

                ProgressViewData currentData = parseData(args);

                Log.d(LOG_TAG, "shape here again===============" + Integer.toString(currentData.shape));

                if (currentData.message == null) {
                    currentData.message = lastMessage;
                }

                ProgressDialog newProgress = getProgressDialog(currentData);
                newProgress.show();                

                Log.d(LOG_TAG, "shape setted==================");

                ProgressView.progressViewObj.dismiss();
                ProgressView.progressViewObj = newProgress;

                // Callback
                PluginResult result = new PluginResult(PluginResult.Status.OK, "(Cordova ProgressView) (setShape) OK");
                result.setKeepCallback(true);
                callback.sendPluginResult(result);
            }
        };
        this.cordova.getActivity().runOnUiThread(runnable);
    }

    private ProgressViewData parseData(String args) {

        // Get Arguments
        JSONArray argsObj = null;
        try {
            argsObj = new JSONArray(args);
        } catch (JSONException e) {
            // e.printStackTrace();
        }

        ProgressViewData currentData = new ProgressViewData();

        try {
            if(argsObj.getString(0) != null && !argsObj.getString(0).isEmpty()){
                try {
                    currentData.message = argsObj.getString(0).replaceAll("^\"|\"$", "");
                } catch (JSONException e) {
                    // e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (argsObj.get(1) != null) {
                try {
                    if ("CIRCLE".equals(argsObj.getString(1))) {
                        currentData.shape = ProgressDialog.STYLE_SPINNER;
                    }
                } catch (JSONException e) {
                    // e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (argsObj.get(2) != null) {
                try {
                    currentData.indeterminate = argsObj.getBoolean(2);
                } catch (JSONException e) {
                    // e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }                

        try {
            if (argsObj.get(3) != null) {
                String themeArg = null;
                try {
                    themeArg = argsObj.getString(3);
                } catch (JSONException e) {
                    // e.printStackTrace();
                }
                if ("TRADITIONAL".equals(themeArg)) {
                    currentData.theme = 1; // ProgressDialog.THEME_TRADITIONAL
                } else if ("DEVICE_DARK".equals(themeArg)) {
                    currentData.theme = 4; // ProgressDialog.THEME_DEVICE_DEFAULT_DARK
                }
                if ("HOLO_DARK".equals(themeArg)) {
                    currentData.theme = 2; // ProgressDialog.THEME_HOLO_DARK
                }
                if ("HOLO_LIGHT".equals(themeArg)) {
                    currentData.theme = 3; // ProgressDialog.THEME_HOLO_LIGHT
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return currentData;
    }
    
    private ProgressDialog getProgressDialog(ProgressViewData currentData) {

        ProgressDialog progress = new ProgressDialog(cordova.getActivity(), currentData.theme);
        progress.setProgressStyle(currentData.shape);
        progress.setTitle("");
        progress.setMessage(currentData.message);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        return progress;
    }

    
    /**
     * Set Label
     */
    private void setLabel(final String args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                // Check State
                if (ProgressView.progressViewObj == null) {
                    PluginResult result = new PluginResult(PluginResult.Status.ERROR, "(Cordova ProgressView) (setLabel) ERROR: No dialog to update");
                    result.setKeepCallback(true);
                    callback.sendPluginResult(result);
                    return;
                }
                
                // Get Arguments
                JSONArray argsObj = null;
                try {
                    argsObj = new JSONArray(args);
                } catch (JSONException e) {
                    // e.printStackTrace();
                }

                // Update Label
                String label = "";
                try {
                    if (argsObj.get(0) != null) {
                        try {
                            label = argsObj.getString(0);
                            if(label.isEmpty()){
                                label = " ";
                            }
                        } catch (JSONException e) {
                            // e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Update Label
                ProgressView.progressViewObj.setMessage(label.replaceAll("^\"|\"$", ""));

                // Callback
                PluginResult result = new PluginResult(PluginResult.Status.OK, "(Cordova ProgressView) (setLabel) OK");
                result.setKeepCallback(true);
                callback.sendPluginResult(result);
            }
        };
        this.cordova.getActivity().runOnUiThread(runnable);
    }
    
    
    
    /**
     * Hide
     */
    private void hide() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                // Check State
                if (ProgressView.progressViewObj == null) {
                    PluginResult result = new PluginResult(PluginResult.Status.ERROR, "(Cordova ProgressView) (Hide) ERROR: No dialog to hide");
                    result.setKeepCallback(true);
                    callback.sendPluginResult(result);
                    return;
                }

                // Hide
                ProgressView.progressViewObj.dismiss();
                ProgressView.progressViewObj = null;

                // Callback
                PluginResult result = new PluginResult(PluginResult.Status.OK, "(Cordova ProgressView) (Hide) OK");
                result.setKeepCallback(true);
                callback.sendPluginResult(result);
            }
        };
        this.cordova.getActivity().runOnUiThread(runnable);
    }
}