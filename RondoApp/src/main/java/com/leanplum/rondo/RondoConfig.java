package com.leanplum.rondo;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * This file represents configuration for Rondo that could be changed from the automated tests.
 *
 * You need to have Rondo installed before executing commands.
 * Execute the following commands to create and upload config to device:
 *   1. rm rondo.properties
 *   2. echo "debug-webview=true" > rondo.properties
 *   3. adb shell mkdir /sdcard/Android/data/com.leanplum.rondo/files
 *   4. adb push rondo.properties /sdcard/Android/data/com.leanplum.rondo/files/
 *
 * Note that if you reinstall app the directory will be cleaned.
 * Tested with Android 11.
 */
public class RondoConfig {

  public static void load(Context context) {
    Properties props = null;
    try {
      String path = context.getExternalFilesDir(null) + "/rondo.properties";
      File file = new File(path);
      if (!file.exists()) {
        Log.i("Leanplum", "rondo.properties missing");
        return;
      }
      InputStream is = new FileInputStream(file);
      props = new Properties();
      props.load(is);
    } catch (Throwable t) {
      Log.e("Leanplum", "error loading rondo.properties", t);
    }

    if (props != null) {
      handleProperties(props);
    }
  }

  private static void handleProperties(Properties props) {
    Log.i("Leanplum", "rondo.properties = " + props.toString());
    String value;

    value = props.getProperty("debug-webview");
    if (!TextUtils.isEmpty(value) && Boolean.parseBoolean(value)) {
      if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
        WebView.setWebContentsDebuggingEnabled(true);
      }
    }
  }

}
