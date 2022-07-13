package com.leanplum.rondo

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.leanplum.actions.LeanplumActions
import com.leanplum.actions.MessageDisplayChoice
import com.leanplum.internal.ActionManager
import com.leanplum.internal.Log

class QueueActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_queue)

    initCheckBoxes()
    initController()
    initListener()
  }

  override fun onResume() {
    super.onResume()
    pauseQueueView().isChecked = LeanplumActions.isQueuePaused()
  }

  private fun initCheckBoxes() {
    pauseQueueView().isChecked = LeanplumActions.isQueuePaused()
    disableQueueView().isChecked = !LeanplumActions.isQueueEnabled()
    dismissOnPushOpenedView().isChecked = ActionManager.getInstance().dismissOnPushOpened
    continueOnActivityResumeView().isChecked = ActionManager.getInstance().continueOnActivityResumed
  }

  private fun initController() {
    if (QueueActivityModel.controllerEnabled) {
      controllerSwitch().isChecked = true
      controllerGroup().visibility = View.VISIBLE
    } else {
      controllerSwitch().isChecked = false
      controllerGroup().visibility = View.GONE
    }

    controllerSwitch().setOnCheckedChangeListener { _, checked ->
      controllerSwitch().isChecked = checked
      controllerGroup().visibility = if (checked) View.VISIBLE else View.GONE
      QueueActivityModel.controllerEnabled = checked
    }

    initShouldDisplayMessage()
    initPrioritizeMessages()
  }

  private fun initShouldDisplayMessage() {
    val viewId: Int
    when(MessageDisplayControllerObject.displayChoice) {
      null -> viewId = R.id.radioDisplayNone
      MessageDisplayChoice.show() -> viewId = R.id.radioShow
      MessageDisplayChoice.discard() -> viewId = R.id.radioDiscard
      MessageDisplayChoice.delayIndefinitely() -> viewId = R.id.radioDelayIndefinitely
      else -> {
        viewId = R.id.radioDelay
        delaySeconds().setText(
          MessageDisplayControllerObject.displayChoice?.delaySeconds.toString())
      }
    }
    radioGroupDisplay().check(viewId)
  }

  private fun initPrioritizeMessages() {
    val viewId = when(MessageDisplayControllerObject.prioritizationChoice) {
      PrioritizationType.ONLY_FIRST -> R.id.radioOnlyFirst
      PrioritizationType.ALL_REVERSED -> R.id.radioAllReversed
      else -> R.id.radioPrioritizeNone
    }
    radioGroupPrioritize().check(viewId)
  }

  private fun initListener() {
    if (QueueActivityModel.listenerEnabled) {
      listenerSwitch().isChecked = true
      listenerGroup().visibility = View.VISIBLE
    } else {
      listenerSwitch().isChecked = false
      listenerGroup().visibility = View.GONE
    }

    listenerSwitch().setOnCheckedChangeListener { _, checked ->
      listenerSwitch().isChecked = checked
      listenerGroup().visibility = if (checked) View.VISIBLE else View.GONE
      QueueActivityModel.listenerEnabled = checked
    }

    onMessageDisplayedCheckBox().isChecked = MessageDisplayListenerObject.trackDisplayEvents
    onMessageDismissedCheckBox().isChecked = MessageDisplayListenerObject.trackDismissEvents
    onActionExecutedCheckBox().isChecked = MessageDisplayListenerObject.trackExecuteEvents
  }

  fun onRadioButtonClicked(view: View) = with(MessageDisplayControllerObject) {
    when (view) {
      showRadioButton() -> displayChoice = MessageDisplayChoice.show()
      discardRadioButton() -> displayChoice = MessageDisplayChoice.discard()
      delayIndefinitelyRadioButton() -> displayChoice = MessageDisplayChoice.delayIndefinitely()
      delayRadioButton() -> displayChoice = MessageDisplayChoice.delay(delaySecondsInt())
      displayNoneRadioButton() -> displayChoice = null

      onlyFirstRadioButton() -> prioritizationChoice = PrioritizationType.ONLY_FIRST
      allReversedRadioButton() -> prioritizationChoice = PrioritizationType.ALL_REVERSED
      prioritizeNoneRadioButton() -> prioritizationChoice = null
    }
  }

  fun delaySecondsInt(): Int {
    try {
      return Integer.parseInt(delaySeconds().text.toString())
    } catch (t: Throwable) {
      Log.e("error parsing: ${t.message}")
      return 5
    }
  }

  fun onCheckboxClicked(view: View) {
    val checked: Boolean = (view as CompoundButton).isChecked

    when (view) {
      pauseQueueView() -> LeanplumActions.setQueuePaused(checked)
      disableQueueView() -> LeanplumActions.setQueueEnabled(!checked)
      dismissOnPushOpenedView() -> LeanplumActions.setDismissOnPushOpened(checked)
      continueOnActivityResumeView() -> LeanplumActions.setContinueOnActivityResumed(checked)

      onMessageDisplayedCheckBox() -> MessageDisplayListenerObject.trackDisplayEvents = checked
      onMessageDismissedCheckBox() -> MessageDisplayListenerObject.trackDismissEvents = checked
      onActionExecutedCheckBox() -> MessageDisplayListenerObject.trackExecuteEvents = checked
    }
  }

  fun onButtonClicked(view: View) {
    when (view) {
      showQueueButton() -> TextActivity.start(this, QueueActivityModel.queueJson())
      triggerDelayedMessagesButton() -> LeanplumActions.triggerDelayedMessages()
      showDelayedMessagesButton() -> TextActivity.start(this, QueueActivityModel.delayedMessagesJson())
      showEventsButton() -> TextActivity.start(this, QueueActivityModel.eventsJson())
      resetEventsButton() -> QueueActivityModel.resetEvents()
    }
  }

  fun pauseQueueView() = findViewById<CheckBox>(R.id.pauseQueue)
  fun disableQueueView() = findViewById<CheckBox>(R.id.disableQueue)
  fun dismissOnPushOpenedView() = findViewById<CheckBox>(R.id.dismissOnPushOpened)
  fun continueOnActivityResumeView() = findViewById<CheckBox>(R.id.continueOnActivityResume)
  fun showQueueButton() = findViewById<Button>(R.id.showQueue)

  fun controllerSwitch() = findViewById<SwitchCompat>(R.id.controllerSwitch)
  fun controllerGroup() = findViewById<LinearLayout>(R.id.controllerGroup)

  fun radioGroupDisplay() = findViewById<RadioGroup>(R.id.radioGroupDisplay)
  fun showRadioButton() = findViewById<RadioButton>(R.id.radioShow)
  fun discardRadioButton() = findViewById<RadioButton>(R.id.radioDiscard)
  fun delayIndefinitelyRadioButton() = findViewById<RadioButton>(R.id.radioDelayIndefinitely)
  fun delayRadioButton() = findViewById<RadioButton>(R.id.radioDelay)
  fun delaySeconds() = findViewById<EditText>(R.id.delaySeconds)
  fun displayNoneRadioButton() = findViewById<RadioButton>(R.id.radioDisplayNone)

  fun radioGroupPrioritize() = findViewById<RadioGroup>(R.id.radioGroupPrioritize)
  fun onlyFirstRadioButton() = findViewById<RadioButton>(R.id.radioOnlyFirst)
  fun allReversedRadioButton() = findViewById<RadioButton>(R.id.radioAllReversed)
  fun prioritizeNoneRadioButton() = findViewById<RadioButton>(R.id.radioPrioritizeNone)

  fun triggerDelayedMessagesButton() = findViewById<Button>(R.id.triggerDelayedMessages)
  fun showDelayedMessagesButton() = findViewById<Button>(R.id.showDelayedMessages)

  fun listenerSwitch() = findViewById<SwitchCompat>(R.id.listenerSwitch)
  fun listenerGroup() = findViewById<LinearLayout>(R.id.listenerGroup)
  fun onMessageDisplayedCheckBox() = findViewById<CheckBox>(R.id.trackOnMessageDisplayed)
  fun onMessageDismissedCheckBox() = findViewById<CheckBox>(R.id.trackOnMessageDismissed)
  fun onActionExecutedCheckBox() = findViewById<CheckBox>(R.id.trackOnActionExecuted)
  fun showEventsButton() = findViewById<Button>(R.id.showEvents)
  fun resetEventsButton() = findViewById<Button>(R.id.resetEvents)
}
