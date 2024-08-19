package com.leanplum.customtemplates

import android.app.Activity
import android.app.Dialog
import com.clevertap.android.sdk.CleverTapAPI
import com.clevertap.android.sdk.inapp.customtemplates.CustomTemplate
import com.clevertap.android.sdk.inapp.customtemplates.CustomTemplateContext
import com.clevertap.android.sdk.inapp.customtemplates.FunctionPresenter
import com.clevertap.android.sdk.inapp.customtemplates.TemplatePresenter
import com.leanplum.ActionContext
import com.leanplum.Leanplum.ACTION_KIND_ACTION
import com.leanplum.Leanplum.ACTION_KIND_MESSAGE
import com.leanplum.LeanplumActivityHelper
import com.leanplum.actions.internal.ActionDefinition
import com.leanplum.actions.internal.defineAction
import com.leanplum.callbacks.ActionCallback
import com.leanplum.internal.ActionManager

object RondoCustomTemplates {

    val ctDefinedTemplates: MutableList<CustomTemplate> = mutableListOf()
    val lpDefinedTemplates: MutableList<ActionDefinition> = mutableListOf()

    var ctHiddenTemplateContext: CustomTemplateContext? = null
    var lpHiddenTemplateContext: ActionContext? = null

    val ctCustomTemplateDialogs: MutableMap<String, Dialog> = mutableMapOf()
    val lpCustomTemplateDialogs: MutableMap<String, Dialog> = mutableMapOf()

    @JvmStatic
    fun registerCleverTapTemplates() {
        val templatePresenter = object : TemplatePresenter {

            override fun onClose(context: CustomTemplateContext.TemplateContext) {
                ctCustomTemplateDialogs[context.templateName]?.dismiss()
                context.setDismissed()
            }

            override fun onPresent(context: CustomTemplateContext.TemplateContext) {
                runWithCurrentActivity {
                    showCtTemplateDialog(context)
                }
            }

        }
        val functionPresenter = FunctionPresenter {
            runWithCurrentActivity {
                showCtTemplateDialog(it)
            }
        }

        val templates = createCleverTapTemplates(templatePresenter, functionPresenter)

        ctDefinedTemplates.addAll(templates)
        CleverTapAPI.registerCustomInAppTemplates { templates }
    }

    @JvmStatic
    fun defineLeanplumTemplates() {
        val presentHandler = object : ActionCallback() {
            override fun onResponse(actionContext: ActionContext): Boolean {
                runWithCurrentActivity {
                    showLpTemplateDialog(actionContext)
                }
                return true
            }
        }
        val dismissHandler = object : ActionCallback() {
            override fun onResponse(context: ActionContext): Boolean {
                lpCustomTemplateDialogs[context.actionName()]?.dismiss()
                context.actionDismissed()
                return true
            }

        }
        val actions = createLeanplumTemplates(presentHandler, dismissHandler)

        lpDefinedTemplates.addAll(actions)
        actions.forEach {
            ActionManager.getInstance().defineAction(it)
        }
    }

    private fun runWithCurrentActivity(action: Activity.() -> Unit) {
        val currentActivity = LeanplumActivityHelper.getCurrentActivity()
        if (currentActivity == null || currentActivity.isFinishing) {
            LeanplumActivityHelper.queueActionUponActive {
                runWithCurrentActivity(action)
            }
        } else {
            action(currentActivity)
        }
    }
}

fun ActionDefinition.toStringFormatted(): String {
    return """
        |{
        |    name = $name,
        |    kind = ${
        when (kind) {
            ACTION_KIND_MESSAGE -> "MESSAGE"
            ACTION_KIND_ACTION -> "ACTION"
            else -> ""
        }
    },
        |    args =
        |      ${
        args.value.joinToString(",\n      ") { arg ->
            "name = ${arg.name()}, kind = ${arg.kind()}, default = ${arg.defaultValue()}"
        }
    }
        |}""".trimMargin()
}

fun ActionContext.toStringFormatted(): String {
    return """
        |{
        |   name = ${actionName()},
        |   args =
        |   ${
        args.map { "${it.key} = ${it.value}" }.joinToString(",\n      ")
    }
        |}
    """.trimMargin()
}
