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
    List<NotificationChannel> channels =
        LeanplumNotificationChannel.getNotificationChannels(Leanplum.getContext());
    if (channels == null) {
      return new ArrayList<>();
    }

    List<Pair<String, Boolean>> result = new ArrayList<>();
    for (NotificationChannel channel: channels) {
      result.add(new Pair<>(
          channel.getName().toString(),
          channel.getImportance() > NotificationManager.IMPORTANCE_NONE));
    }

    return result;
  }

  @RequiresApi(api = VERSION_CODES.O)
  public static boolean muteChannel(@NonNull String name) {
    List<NotificationChannel> channels =
        LeanplumNotificationChannel.getNotificationChannels(Leanplum.getContext());
    if (channels == null) {
      return false;
    }

    NotificationChannel channel = null;
    for (NotificationChannel ch: channels) {
      if (name.equals(ch.getName())) {
        channel = ch;
        break;
      }
    }
    if (channel == null) {
      return false;
    }

    NotificationManager notificationManager =
        Leanplum.getContext().getSystemService(NotificationManager.class);

    NotificationChannel edited = new NotificationChannel(
        channel.getId(), channel.getName(), NotificationManager.IMPORTANCE_NONE);
    notificationManager.createNotificationChannel(edited);

    channel = notificationManager.getNotificationChannel(channel.getId());
    return channel.getImportance() == NotificationManager.IMPORTANCE_NONE;
  }

}
