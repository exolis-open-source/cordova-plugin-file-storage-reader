package FileStorageReaderPlugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build.VERSION;
import android.webkit.*;
import android.view.*;
import android.widget.*;

/**
 * This class reads local storage and send it to cordova.
 */
public class FileStorageReaderPlugin extends CordovaPlugin {

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    if (action.equals("readFileStorage")) {
      this.readFileStorage(args.getString(0), callbackContext);
      return true;
    }
    return false;
  }

  private void readFileStorage(String url, CallbackContext callbackContext) {
    WebView myWebView = new WebView(cordova.getContext());
    webView.getSettings().setJavaScriptEnabled(true);
    webView.getSettings().setAllowFileAccess(true);
    webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
    webView.setVisibility(View.INVISIBLE);

    webView.addJavascriptInterface(new Object() {
      @JavascriptInterface
      public void onData(String json) {
        callbackContext.success(json);
      }
    }, "JSBridge");

    webView.setWebViewClient(new WebViewClient() {
      @Override
      public void onPageFinished(WebView view, String url) {
        view.evaluateJavascript(
          "(function() {" +
          " var out = {};" +
          " for (var i = 0; i < localStorage.length; i++) {" +
          "   var k = localStorage.key(i);" +
          "   out[k] = localStorage.getItem(k);" +
          " }" +
          " JSBridge.onData(JSON.stringify(out));" +
          "})()", null);
      }
    });
    cordova.getActivity().addContentView(webView, new FrameLayout.LayoutParams(1, 1));
    webView.loadUrl(url);
  }
}