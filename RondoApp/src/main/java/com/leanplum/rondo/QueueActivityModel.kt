package com.leanplum.rondo

import org.json.JSONArray
import org.json.JSONObject

enum class PrioritizationType {
  ONLY_FIRST, ALL_REVERSED
}

object QueueActivityModel {

  var controllerEnabled = false
  set(value) {
    field = value
  }

  var listenerEnabled = false
  set(value) {
    field = value
  }

  fun resetEvents() {
  }

  fun eventsJson(): String {
    return ""
  }

  fun queueJson(): String {
    val json = JSONArray()
    return json.toString(4)
  }

  fun delayedMessagesJson(): String {
    val json = JSONArray()
    return json.toString(4)
  }

}
