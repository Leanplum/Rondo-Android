package com.leanplum.rondo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TextActivity : AppCompatActivity() {

  companion object {
    const val CONTENT_TEXT_EXTRA = "content_text"

    @JvmStatic
    fun start(activity: Activity, contentText: String) {
      val intent = Intent(activity, TextActivity::class.java)
      intent.putExtra(CONTENT_TEXT_EXTRA, contentText)
      activity.startActivity(intent)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_text)

    val textContent = intent.extras?.getString(CONTENT_TEXT_EXTRA, "")
    findViewById<TextView>(R.id.contentText).text = textContent
  }
}
