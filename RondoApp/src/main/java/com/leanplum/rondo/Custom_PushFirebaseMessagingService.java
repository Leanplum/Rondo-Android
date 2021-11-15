package com.leanplum.rondo;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.RemoteMessage;
import com.leanplum.LeanplumPushFirebaseMessagingService;
import java.util.Map;

public class Custom_PushFirebaseMessagingService extends LeanplumPushFirebaseMessagingService {

  @Override
  public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
    Log.e("TAG", "message received");
    Map<String, String> data = remoteMessage.getData();
    data.put("push_clicked", String.valueOf(true));
    super.onMessageReceived(remoteMessage);
  }

  @Override
  public void onNewToken(@NonNull String token) {
    super.onNewToken(token);
  }
}
