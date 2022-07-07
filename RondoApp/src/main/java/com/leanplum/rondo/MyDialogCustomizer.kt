package com.leanplum.rondo

import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.leanplum.messagetemplates.DialogCustomizer

/**
 * Set customizer by MessageTemplates.setCustomizer(...) before Leanplum.start().
 */
class MyDialogCustomizer : DialogCustomizer {

  override fun customizeRichInterstitial(messageDialog: Dialog?, messageContent: View?) {
    val webView = findWebView(messageContent)
    if (webView != null) {
      val webViewSettings = webView.settings

      webViewSettings.setGeolocationEnabled(false)
      webViewSettings.allowContentAccess = false
      // webViewSettings.allowFileAccess - Cannot be set because it would break the
      // Rich Interstitial behaviour. Hero image won't be shown
    }
  }

  override fun customizeWebInterstitial(messageDialog: Dialog?, messageContent: View?) {
    val webView = findWebView(messageContent)
    if (webView != null) {
      val webViewSettings = webView.settings

      webViewSettings.setGeolocationEnabled(false)
      webViewSettings.allowContentAccess = false
      webViewSettings.allowFileAccess = false
    }
  }
}

/**
 * Breadth First Search algorithm to find the WebView.
 */
fun findWebView(messageContent: View?): WebView? {
  if (messageContent !is ViewGroup) return null

  val queue = mutableListOf(messageContent)

  while (queue.isNotEmpty()) {
    val current = queue.removeAt(0)
      for (i in 0 until current.childCount) {
        val child = current.getChildAt(i)
        if (child is WebView) {
          return child
        } else if (child is ViewGroup) {
          queue.add(child)
        }
      }
  }
  return null
}
