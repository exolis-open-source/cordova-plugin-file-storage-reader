package FileStorageReaderPlugin;

import org.apache.cordova.*;

import org.json.JSONArray;
import org.json.JSONException;

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
    myWebView.getSettings().setJavaScriptEnabled(true);
    myWebView.getSettings().setAllowFileAccess(true);
    myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
    myWebView.setVisibility(View.INVISIBLE);

    myWebView.addJavascriptInterface(new Object() {
      @JavascriptInterface
      public void onData(String json) {
        callbackContext.success(json);
      }
    }, "JSBridge");

    myWebView.setWebViewClient(new WebViewClient() {
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
    cordova.getActivity().addContentView(myWebView, new FrameLayout.LayoutParams(1, 1));
    myWebView.loadUrl(url);
  }
}