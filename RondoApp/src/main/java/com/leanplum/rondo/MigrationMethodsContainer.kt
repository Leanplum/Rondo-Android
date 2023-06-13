package com.leanplum.rondo

import com.clevertap.android.sdk.leanplum.LeanplumCT
import java.util.*

data class ButtonEntry(
  val text: String,
  val action: ((String) -> Unit)? = null
)

object MigrationMethodsContainer {

  val buttonEntries = mutableListOf<ButtonEntry>()

  init {
    prepareSetUserAttributes()
    prepareAdvanceTo()
    prepareTrack()
    prepareTrackPurchase()
    prepareTrackGooglePlayPurchase()
    prepareSetTrafficSourceInfo()
  }

  private fun createTrackParams() = mutableMapOf<String, Any?>(
    // lists are not supported for LP.track
    "byte_param_" + Byte.MAX_VALUE to Byte.MAX_VALUE,
    "short_param_" + Short.MAX_VALUE to Short.MAX_VALUE,
    "int_param_" + Int.MAX_VALUE to Int.MAX_VALUE,
    "long_param_" + Long.MAX_VALUE to Long.MAX_VALUE,
    "float_param_" + Float.MAX_VALUE to Float.MAX_VALUE,
    "double_param_" + Double.MAX_VALUE to Double.MAX_VALUE,
    "string_param_str" to "str",
    "bool_param_true" to true,
    "date_param_now" to Date(),
  )

  private fun createAttributeParams() = createTrackParams().apply {
    // arrays and nested lists are not supported in LP.setUserAttribute
    this["list_param_a_1_b_2"] = listOf("a", 1, "b", 2)
    this["list_param_empty"] = emptyList<Any>()
  }

  private fun prepareTrack() {
    buttonEntries.apply {
      add(ButtonEntry("Leanplum.track"))
      add(ButtonEntry("track(event)") { text -> LeanplumCT.track(text) })
      add(ButtonEntry("track(event info)") { text -> LeanplumCT.track(text, "info") })
      add(ButtonEntry("track(event value info one-param)") { text -> LeanplumCT.track(text, 5.0, "info", mapOf("param1" to "value1")) })
      add(ButtonEntry("track(event all-type-params)") { text -> LeanplumCT.track(text, createTrackParams()) })
    }
  }

  private fun prepareTrackPurchase() {
    buttonEntries.apply {
      add(ButtonEntry("Leanplum.trackPurchase"))
      add(ButtonEntry("trackPurchase(event 0 null null)") { text -> LeanplumCT.trackPurchase(text, 0.0, null, null) })
      add(ButtonEntry("trackPurchase(event value currency one-param)") { text -> LeanplumCT.trackPurchase(text, 2.5, "BGN", mapOf("param1" to "value1")) })
      add(ButtonEntry("trackPurchase(event value currency all-type-params)") { text -> LeanplumCT.trackPurchase(text, 2.5, "BGN", createTrackParams()) })
    }
  }

  private fun createPurchaseData() = """{"orderId":"${System.currentTimeMillis()}"}"""

  private fun prepareTrackGooglePlayPurchase() {
    buttonEntries.apply {
      add(ButtonEntry("Leanplum.trackGooglePlayPurchase"))
      add(ButtonEntry("trackGooglePlayPurchase(event null 0 null null null null)") { text -> LeanplumCT.trackGooglePlayPurchase(text, null, 0, null, null, null, null) })
      add(ButtonEntry("trackGooglePlayPurchase(event item value currency data signature one-param)") { text -> LeanplumCT.trackGooglePlayPurchase(text, "item", 10000000, "BGN", createPurchaseData(), "data-signature", mapOf("param1" to "value1")) })
      add(ButtonEntry("trackGooglePlayPurchase(event item value currency data signature all-type-params)") { text -> LeanplumCT.trackGooglePlayPurchase(text, "item", 10000000, "BGN", createPurchaseData(), "data-signature", createTrackParams()) })
    }
  }

  private fun prepareAdvanceTo() {
    buttonEntries.apply {
      add(ButtonEntry("Leanplum.advanceTo"))
      add(ButtonEntry("advanceTo(null)") { LeanplumCT.advanceTo(null) }) // will be skipped in CT
      add(ButtonEntry("advanceTo(event)") { text -> LeanplumCT.advanceTo(text) })
      add(ButtonEntry("advanceTo(event info)") { text -> LeanplumCT.advanceTo(text, "info") })
      add(ButtonEntry("advanceTo(event info one-param)") { text -> LeanplumCT.advanceTo(text, "info", mapOf("param1" to "value1")) })
      add(ButtonEntry("advanceTo(event info all-type-params)") { text -> LeanplumCT.advanceTo(text, "info", createTrackParams()) })
    }
  }

  private fun prepareSetUserAttributes() {
    buttonEntries.apply {
      add(ButtonEntry("Leanplum.setUserAttributes"))
      add(ButtonEntry("setUserAttributes(null)") { LeanplumCT.setUserAttributes(null) })
      add(ButtonEntry("setUserAttributes(date-of-birth-DOB)") {
        @Suppress("DEPRECATION")
        LeanplumCT.setUserAttributes(mapOf("DOB" to Date(89, 11, 31)))
      })
      add(ButtonEntry("setUserAttributes(one-attribute)") { LeanplumCT.setUserAttributes(mapOf("param1" to "value1")) })
      add(ButtonEntry("setUserAttributes(all-type-attributes)") { LeanplumCT.setUserAttributes(createAttributeParams()) })
    }
  }

  private fun createTrafficSourceInfo() = mutableMapOf(
    "publisherId" to "pub-id",
    "publisherName" to "pub-name",
    "publisherSubPublisher" to "sub-publisher",
    "publisherSubSite" to "sub-site",
    "publisherSubCampaign" to "sub-campaign",
    "publisherSubAdGroup" to "sub-ad-group",
    "publisherSubAd" to "sub-ad",
  )

  private fun prepareSetTrafficSourceInfo() {
    buttonEntries.apply {
      add(ButtonEntry("Leanplum.setTrafficSourceInfo"))
      add(ButtonEntry("setTrafficSourceInfo(all-type-source-info)") { LeanplumCT.setTrafficSourceInfo(createTrafficSourceInfo()) })
    }
  }
}
