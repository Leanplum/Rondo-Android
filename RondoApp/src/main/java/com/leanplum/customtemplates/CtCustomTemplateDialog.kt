package com.leanplum.customtemplates

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.isVisible
import com.clevertap.android.sdk.inapp.customtemplates.CustomTemplateContext
import com.leanplum.internal.Log
import com.leanplum.rondo.FileProviderUtil
import com.leanplum.rondo.R
import com.leanplum.rondo.openFileFromProviderWithChooser

fun Activity.showCtTemplateDialog(context: CustomTemplateContext) {
    val dialog = Dialog(this, R.style.Dialog_FullWidth)
    RondoCustomTemplates.ctCustomTemplateDialogs[context.templateName] = dialog

    dialog.setCancelable(false)
    dialog.setContentView(R.layout.dialog_custom_template_ct)

    dialog.findViewById<Button>(R.id.hide_button)?.let { hideButton ->
        hideButton.setOnClickListener {
            RondoCustomTemplates.ctHiddenTemplateContext = context
            dialog.dismiss()
        }
        hideButton.isVisible = RondoCustomTemplates.ctHiddenTemplateContext == null
    }

    dialog.findViewById<Button>(R.id.dismiss_button)?.setOnClickListener {
        context.setDismissed()
        dialog.dismiss()
    }

    var fileType: String? = null
    dialog.findViewById<Spinner>(R.id.open_file_type)?.let { fileTypeSpinner ->
        fileTypeSpinner.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                FileProviderUtil.mimeTypes
            )
        fileTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                fileType = FileProviderUtil.mimeTypes[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                fileType = null
            }

        }
    }


    dialog.findViewById<Button>(R.id.open_file_button).setOnClickListener {
        val varName = dialog.findViewById<EditText>(R.id.open_file_text).text.toString()
        val filePath = context.getFile(varName)
        if (filePath == null) {
            Log.d("No file path for var: $varName")
            return@setOnClickListener
        }

        openFileFromProviderWithChooser(filePath, fileType)
    }

    if (context is CustomTemplateContext.TemplateContext) {
        val actionText = dialog.findViewById<EditText>(R.id.run_action_text)
        actionText.isVisible = true
        dialog.findViewById<TextView>(R.id.run_action_label).isVisible = true
        dialog.findViewById<Button>(R.id.run_action_button)?.let { runActionButton ->
            runActionButton.isVisible = true
            runActionButton.setOnClickListener {
                context.triggerActionArgument(actionText.text.toString(), this)
            }
        }
    }

    dialog.findViewById<TextView>(R.id.context).text = context.toString()
    dialog.findViewById<TextView>(R.id.definition).text =
        RondoCustomTemplates.ctDefinedTemplates.find { it.name == context.templateName }?.toString()

    dialog.setOnDismissListener {
        RondoCustomTemplates.ctCustomTemplateDialogs.remove(context.templateName)
    }

    dialog.show()
}
