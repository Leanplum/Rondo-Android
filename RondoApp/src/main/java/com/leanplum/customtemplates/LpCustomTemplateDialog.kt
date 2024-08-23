package com.leanplum.customtemplates

import android.app.Activity
import android.app.Dialog
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.leanplum.ActionContext
import com.leanplum.rondo.R
import com.leanplum.rondo.openFileFromProviderWithChooser

fun Activity.showLpTemplateDialog(context: ActionContext) {
    val dialog = Dialog(this, R.style.Dialog_FullWidth)
    RondoCustomTemplates.lpCustomTemplateDialogs[context.actionName()] = dialog

    dialog.setCancelable(false)
    dialog.setContentView(R.layout.dialog_custom_template_lp)

    dialog.findViewById<Button>(R.id.hide_button)?.let { hideButton ->
        hideButton.setOnClickListener {
            RondoCustomTemplates.lpHiddenTemplateContext = context
            dialog.dismiss()
        }
        hideButton.isVisible = RondoCustomTemplates.lpHiddenTemplateContext == null
    }

    dialog.findViewById<Button>(R.id.dismiss_button)?.setOnClickListener {
        context.actionDismissed()
        dialog.dismiss()
    }

    dialog.findViewById<Button>(R.id.run_action_button)?.let { runActionButton ->
        runActionButton.setOnClickListener {
            context.runActionNamed(dialog.findViewById<EditText>(R.id.run_action_text).text.toString())
        }
    }

    dialog.findViewById<Button>(R.id.open_file_button).setOnClickListener {
        val varName = dialog.findViewById<EditText>(R.id.open_file_text).text.toString()
        val filePath = ActionContext.filePath(context.stringNamed(varName))
        if (filePath == null) {
            Toast.makeText(this, "No file path for var: $varName", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

        openFileFromProviderWithChooser(filePath)
    }

    dialog.findViewById<TextView>(R.id.context).text = context.toStringFormatted()
    dialog.findViewById<TextView>(R.id.definition).text =
        RondoCustomTemplates.lpDefinedTemplates.find { it.name == context.actionName() }
            ?.toStringFormatted()

    dialog.setOnDismissListener {
        RondoCustomTemplates.lpCustomTemplateDialogs.remove(context.actionName())
    }

    dialog.show()
}
