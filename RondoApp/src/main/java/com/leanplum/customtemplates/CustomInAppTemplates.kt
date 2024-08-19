package com.leanplum.customtemplates

import com.clevertap.android.sdk.inapp.customtemplates.CustomTemplate
import com.clevertap.android.sdk.inapp.customtemplates.FunctionPresenter
import com.clevertap.android.sdk.inapp.customtemplates.TemplatePresenter
import com.clevertap.android.sdk.inapp.customtemplates.function
import com.clevertap.android.sdk.inapp.customtemplates.template
import com.clevertap.android.sdk.inapp.customtemplates.templatesSet
import com.leanplum.ActionArgs
import com.leanplum.Leanplum.ACTION_KIND_ACTION
import com.leanplum.Leanplum.ACTION_KIND_MESSAGE
import com.leanplum.actions.internal.ActionDefinition
import com.leanplum.callbacks.ActionCallback

fun createCleverTapTemplates(
    templatePresenter: TemplatePresenter,
    functionPresenter: FunctionPresenter
): Set<CustomTemplate> {
    return templatesSet(
        function(isVisual = false) {
            name("function-lina1")
            stringArgument("Monday", "string")
            booleanArgument("email", false)
            booleanArgument("phone", true)
            fileArgument("Friday")
            presenter(functionPresenter)
        },
        template {
            name("template-lina")
            booleanArgument("var1", false)
            stringArgument("var2", "Default")
            doubleArgument("folder1.var3", 0.0)
            fileArgument("folder1.var4")
            mapArgument(
                "map", mapOf(
                    "int" to 0, "string" to "Default"
                )
            )
            actionArgument("action")
            presenter(templatePresenter)
        })
}

fun createLeanplumTemplates(
    presentHandler: ActionCallback,
    dismissHandler: ActionCallback
): List<ActionDefinition> {
    return listOf(
        ActionDefinition(
            name = "function-lina1",
            kind = ACTION_KIND_ACTION,
            args = ActionArgs()
                .with("Monday", "string")
                .with("email", false)
                .with("phone", true)
                .withFile("Friday", null),
            options = mapOf(),
            presentHandler = presentHandler,
            dismissHandler = dismissHandler
        ),
        ActionDefinition(
            name = "template-lina",
            kind = ACTION_KIND_MESSAGE,
            args = ActionArgs()
                .with("var1", false)
                .with("var2", "Default")
                .with("folder1.var3", 0.0)
                .withFile("folder1.var4", null)
                .with(
                    "map", mapOf(
                        "int" to 0, "string" to "Default"
                    )
                )
                .withAction("action", ""),
            options = mapOf(),
            presentHandler = presentHandler,
            dismissHandler = dismissHandler
        )
    )
}
