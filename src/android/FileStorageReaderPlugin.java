package FileStorageReaderPlugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build.VERSION;

/**
 * This class reads local storage and send it to cordova.
 */
public class FileStorageReaderPlugin extends CordovaPlugin {

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    if (action.equals("readFileStorage")) {
      this.readFileStorage(callbackContext);
      return true;
    }
    return false;
  }

  private void readFileStorage(CallbackContext callbackContext) {
    if (VERSION.SDK_INT >= 21) {
      callbackContext.success();
    }
    callbackContext.success();
  }
}