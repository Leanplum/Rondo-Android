package com.leanplum;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build.VERSION_CODES;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.List;

public class ChannelManager {

  @RequiresApi(api = VERSION_CODES.O)
  public static List<Pair<String, Boolean>> listChannels() {
    return null;
  }

  @RequiresApi(api = VERSION_CODES.O)
  public static boolean muteChannel(@NonNull String name) {
    return false;
  }

}
