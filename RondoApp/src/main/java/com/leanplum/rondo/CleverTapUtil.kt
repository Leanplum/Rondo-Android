package com.leanplum.rondo

import android.app.Application
import android.app.NotificationManager
import com.clevertap.android.pushtemplates.PushTemplateNotificationHandler
import com.clevertap.android.sdk.ActivityLifecycleCallback
import com.clevertap.android.sdk.CleverTapAPI

object CleverTapUtil {
  fun initialize(application: Application) {

    // This code could be moved inside LP SDK

    ActivityLifecycleCallback.register(application)
    CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE)

    // TODO check API for O
    CleverTapAPI.createNotificationChannel(
      application,
      "YourChannelId",
      "Your Channel Name",
      "Your Channel Description",
      NotificationManager.IMPORTANCE_MAX,
      true)

    CleverTapAPI.setNotificationHandler(PushTemplateNotificationHandler())

    var cleverTapDefaultInstance: CleverTapAPI? = null
    cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(application)

  }
}
