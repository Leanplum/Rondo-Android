package com.leanplum.rondo

import com.leanplum.ActionContext
import com.leanplum.actions.LeanplumActions
import com.leanplum.actions.MessageDisplayChoice
import com.leanplum.actions.MessageDisplayController
import com.leanplum.actions.MessageDisplayListener
import com.leanplum.actions.internal.ActionsTrigger
import com.leanplum.actions.internal.triggerDelayedMessages
import com.leanplum.internal.ActionManager
import org.json.JSONArray
import org.json.JSONObject

enum class PrioritizationType {
  ONLY_FIRST, ALL_REVERSED
}

object MessageDisplayControllerObject : MessageDisplayController {
  var displayChoice: MessageDisplayChoice? = null
  var prioritizationChoice: PrioritizationType? = null

  override fun shouldDisplayMessage(action: ActionContext): MessageDisplayChoice? = displayChoice

  override fun prioritizeMessages(
    actions: List<ActionContext>,
    trigger: ActionsTrigger?
  ): List<ActionContext> = when (prioritizationChoice) {
    PrioritizationType.ONLY_FIRST -> listOf(actions.first())
    PrioritizationType.ALL_REVERSED -> actions.reversed()
    else -> actions
  }
}

object MessageDisplayListenerObject : MessageDisplayListener {
  var trackedEventsJson = JSONArray()
  var trackDisplayEvents = true
  var trackDismissEvents = true
  var trackExecuteEvents = true

  override fun onMessageDisplayed(action: ActionContext) {
    if (trackDisplayEvents) {
      trackedEventsJson.addAction(action, "onMessageDisplayed")
    }
  }

  override fun onMessageDismissed(action: ActionContext) {
    if (trackDismissEvents) {
      trackedEventsJson.addAction(action, "onMessageDismissed")
    }
  }

  override fun onActionExecuted(name: String, action: ActionContext) {
    if (trackExecuteEvents) {
      trackedEventsJson.addAction(action, "onActionExecuted")
    }
  }
}

fun JSONArray.addAction(context: ActionContext, method: String? = null) {
  val jsonObject = JSONObject().apply {
    put("name", context.actionName())
    put("messageId", context.messageId)
    put("parentId", context.parentContext?.messageId ?: "")
    if (method != null) {
      put("method", method)
    }
  }
  this.put(jsonObject)
}

object QueueActivityModel {

  var controllerEnabled = false
  set(value) {
    field = value
    if (value) {
      LeanplumActions.setMessageDisplayController(MessageDisplayControllerObject)
    } else {
      LeanplumActions.setMessageDisplayController(null)
    }
  }

  var listenerEnabled = false
  set(value) {
    field = value
    if (value) {
      LeanplumActions.setMessageDisplayListener(MessageDisplayListenerObject)
    } else {
      resetEvents()
      LeanplumActions.setMessageDisplayListener(null)
    }
  }

  fun resetEvents() {
    MessageDisplayListenerObject.trackedEventsJson = JSONArray()
  }

  fun eventsJson(): String {
    return MessageDisplayListenerObject.trackedEventsJson.toString(4)
  }

  fun queueJson(): String {
    val json = JSONArray()
    for (action in ActionManager.getInstance().queue.queue) {
      json.addAction(action.context)
    }
    return json.toString(4)
  }

  fun delayedMessagesJson(): String {
    val json = JSONArray()
    for (action in ActionManager.getInstance().delayedQueue.queue) {
      json.addAction(action.context)
    }
    return json.toString(4)
  }

}
