package com.leanplum.customtemplates

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.leanplum.Leanplum
import com.leanplum.rondo.R

class CustomTemplatesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_custom_templates)
        initViews()
    }

    private fun initViews() {
        findViewById<TextView>(R.id.clevertap_templates).text =
            RondoCustomTemplates.ctDefinedTemplates.joinToString("\n")
        findViewById<TextView>(R.id.leanplum_templates).text =
            RondoCustomTemplates.lpDefinedTemplates.joinToString("\n") { it.toStringFormatted() }

        findViewById<Button>(R.id.clevertap_show_hidden).setOnClickListener {
            RondoCustomTemplates.ctHiddenTemplateContext?.let {
                RondoCustomTemplates.ctHiddenTemplateContext = null
                refreshHiddenTemplateViews()
                showCtTemplateDialog(it)
            }
        }
        findViewById<Button>(R.id.leanplum_show_hidden).setOnClickListener {
            RondoCustomTemplates.lpHiddenTemplateContext?.let {
                RondoCustomTemplates.lpHiddenTemplateContext = null
                refreshHiddenTemplateViews()
                showLpTemplateDialog(it)
            }
        }
        findViewById<Button>(R.id.clevertap_sync).setOnClickListener {
            Leanplum.addCleverTapInstanceCallback { ctInstance ->
                ctInstance.syncRegisteredInAppTemplates()
            }
        }
        refreshHiddenTemplateViews()
    }

    private fun refreshHiddenTemplateViews() {
        findViewById<TextView>(R.id.clevertap_hidden_template).let { textView ->
            textView.text = if (RondoCustomTemplates.ctHiddenTemplateContext != null) {
                RondoCustomTemplates.ctHiddenTemplateContext?.templateName
            } else {
                "none"
            }
        }
        findViewById<TextView>(R.id.leanplum_hidden_template).let { textView ->
            textView.text = if (RondoCustomTemplates.lpHiddenTemplateContext != null) {
                RondoCustomTemplates.lpHiddenTemplateContext?.actionName()
            } else {
                "none"
            }
        }
    }
}
