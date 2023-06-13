package com.leanplum.rondo

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

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
  }

  private fun initCheckBoxes() {
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

  }

  fun delaySecondsInt(): Int {
    try {
      return Integer.parseInt(delaySeconds().text.toString())
    } catch (t: Throwable) {
      return 5
    }
  }

  fun onCheckboxClicked(view: View) {
    val checked: Boolean = (view as CompoundButton).isChecked

    when (view) {
      asyncHandlersView() -> {
        initCheckBoxes()
      }
      pauseQueueView() -> {
        initCheckBoxes()
      }

    }
  }

  fun onButtonClicked(view: View) {
    when (view) {
      showQueueButton() -> TextActivity.start(this, QueueActivityModel.queueJson())
      showDelayedMessagesButton() -> TextActivity.start(this, QueueActivityModel.delayedMessagesJson())
      showEventsButton() -> TextActivity.start(this, QueueActivityModel.eventsJson())
      resetEventsButton() -> QueueActivityModel.resetEvents()
    }
  }

  fun asyncHandlersView() = findViewById<CheckBox>(R.id.asyncHandlers)
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
