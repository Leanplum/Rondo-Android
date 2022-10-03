package com.leanplum.rondo

import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.leanplum.internal.JsonConverter
import com.leanplum.migration.MigrationManager
import com.leanplum.migration.model.MigrationConfig
import com.leanplum.utils.SizeUtil
import org.json.JSONObject

class MigrationActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_migration)

    state().text = MigrationConfig.state
    accountId().text = MigrationConfig.accountId
    accountToken().text = MigrationConfig.accountToken
    accountRegion().text = MigrationConfig.accountRegion
    trackGooglePlayPurchases().text = MigrationConfig.trackGooglePlayPurchases.toString()
    attributeMappings().setOnClickListener {
      val json = JSONObject(JsonConverter.toJson(MigrationConfig.attributeMap)).toString(4)
      TextActivity.start(this, json)
    }
    disableFcmForward().isEnabled = MigrationManager.wrapper.fcmHandler?.forwardingEnabled ?: false
    disableFcmForward().setOnClickListener {
      MigrationManager.wrapper.fcmHandler?.forwardingEnabled = false
      it.isEnabled = false
    }

    prepareButtons()
  }

  private fun prepareButtons() {
    val container = findViewById<LinearLayout>(R.id.buttonContainer)
    MigrationMethodsContainer.buttonEntries.forEach {
      val txt = it.text
      when (val action = it.action) {
        null -> {
          val tv = TextView(this).apply {
            text = txt
          }
          val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
          )
          lp.topMargin = SizeUtil.dpToPx(this, 20)
          container.addView(tv, lp)
        }
        else -> {
          val button = Button(this).apply {
            text = txt
            transformationMethod = null
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10F)
            setOnClickListener {
              action.invoke(txt)
            }
          }
          val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
          )
          container.addView(button, lp)
        }
      }
    }
  }

  private fun state() = findViewById<TextView>(R.id.state)
  private fun accountId() = findViewById<TextView>(R.id.accountId)
  private fun accountToken() = findViewById<TextView>(R.id.accountToken)
  private fun accountRegion() = findViewById<TextView>(R.id.accountRegion)
  private fun trackGooglePlayPurchases() = findViewById<TextView>(R.id.trackGooglePlayPurchases)
  private fun attributeMappings() = findViewById<Button>(R.id.attributeMappings)
  private fun disableFcmForward() = findViewById<Button>(R.id.disableFcmForward)
}
