package com.leanplum.rondo

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.clevertap.android.sdk.CleverTapAPI
import com.clevertap.android.sdk.variables.Var
import com.clevertap.android.sdk.variables.callbacks.VariablesChangedCallback
import org.json.JSONObject

class MigrationActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_migration)

    state().text = "Merged SDK"
    accountId().text = "TODO"
    accountToken().text = "TODO"
    accountRegion().text = "TODO"

    prepareVariables()
    prepareButtons()
  }

  private fun prepareVariables() {
    val cleverTap = CleverTapAPI.getDefaultInstance(this) ?: return
    val variables = listOf(
      cleverTap.defineVariable("var_string", "hello world"),
      cleverTap.defineVariable("var_integer", 10),
      cleverTap.defineVariable("var_decimal", 11.2),
      cleverTap.defineVariable("var_boolean", true),
      cleverTap.defineVariable("var_dictionary", mapOf(
        "nested_string" to "hello nested",
        "nested_double" to 10.5
      )),
      cleverTap.defineVariable("dot_group.var_string", "hello world"),
      cleverTap.defineVariable("dot_group.var_dictionary", mapOf(
        "nested_float" to 0.5f,
        "nested_int" to 32
      )),
    )
    runOnUiThread {
      cleverTap.addVariablesChangedCallback(object : VariablesChangedCallback() {
        override fun variablesChanged() {
          Log.i("Rondo", "Rondo refreshing variables in Migration Details page")
          prepareVariableViews(variables, cleverTap)
        }
      })
      prepareVariableViews(variables, cleverTap)
    }
  }

  @SuppressLint("SetTextI18n")
  private fun prepareVariableViews(variables: List<Var<*>>, cleverTap: CleverTapAPI) {
    val container = variableContainer()
    container.removeAllViews()

    insertTextView(container, "Variables:")

    variables.forEach {
      val label = it.name() + " = " + it.value()
      insertTextView(container, label, 0)
    }

    insertButton(container, "Fetch Variables") {
      cleverTap.fetchVariables {
        Log.i("Rondo", "Rondo fetched variables result is $it")
      }
    }

    insertButton(container, "Sync Variables") {
      cleverTap.syncVariables()
    }
  }

  private fun prepareButtons() {
    val container = buttonContainer()

    MigrationMethodsContainer.buttonEntries.forEach {
      val txt = it.text
      when (val action = it.action) {
        null -> {
          insertTextView(container, txt)
        }
        else -> {
          insertButton(container, txt) {
            action.invoke(txt)
          }
        }
      }
    }
  }

  private fun insertTextView(container: LinearLayout, label: String, topMarginPx: Int = 20) {
    val tv = TextView(this).apply {
      text = label
    }
    val lp = LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.WRAP_CONTENT,
      ViewGroup.LayoutParams.WRAP_CONTENT
    )
    lp.topMargin = dpToPx(this, topMarginPx)
    container.addView(tv, lp)
  }

  fun dpToPx(context: Context, dp: Int): Int {
    val displayMetrics = context.resources.displayMetrics
    return Math.round(
      dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)
    )
  }

  private fun insertButton(container: LinearLayout, label: String, callback: (String) -> Unit) {
    val button = Button(this).apply {
      text = label
      transformationMethod = null
      setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10F)
      setOnClickListener {
        callback.invoke(label)
      }
    }
    val lp = LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.WRAP_CONTENT,
      ViewGroup.LayoutParams.WRAP_CONTENT
    )
    container.addView(button, lp)
  }

  private fun state() = findViewById<TextView>(R.id.state)
  private fun accountId() = findViewById<TextView>(R.id.accountId)
  private fun accountToken() = findViewById<TextView>(R.id.accountToken)
  private fun accountRegion() = findViewById<TextView>(R.id.accountRegion)
  private fun trackGooglePlayPurchases() = findViewById<TextView>(R.id.trackGooglePlayPurchases)
  private fun attributeMappings() = findViewById<Button>(R.id.attributeMappings)
  private fun disableFcmForward() = findViewById<Button>(R.id.disableFcmForward)
  private fun identityKeys() = findViewById<TextView>(R.id.identityKeys)
  private fun buttonContainer() = findViewById<LinearLayout>(R.id.buttonContainer)
  private fun variableContainer() = findViewById<LinearLayout>(R.id.variableContainer)
}
