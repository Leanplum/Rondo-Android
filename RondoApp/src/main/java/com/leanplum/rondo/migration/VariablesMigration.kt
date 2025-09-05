package com.leanplum.rondo.migration

import com.clevertap.android.sdk.CleverTapAPI
import com.leanplum.Leanplum
import com.leanplum.Var
import com.leanplum.annotations.Parser
import com.leanplum.annotations.Variable
import com.leanplum.internal.Log

class VariablesMigration {

    init {
        Parser.parseVariables()
        Leanplum.addCleverTapInstanceCallback { clevertap ->
            cleverTap = clevertap
        }
        Leanplum.setLogLevel(Log.Level.DEBUG)
        // Parse variables as usual from Leanplum SDK -> this will set leanplumVariable correctly
        Parser.parseVariablesForClasses(VariablesMigration::class.java)
    }

    var cleverTap: CleverTapAPI? = null

    // Define same variables in both dashboards
    val leanplumVariable: Var<String> = Var.define("stringVariable", "Default value in code")
    val clevertapVariable: com.clevertap.android.sdk.variables.Var<String>? =
        cleverTap?.defineVariable("stringVariable", "Default value in code")


    fun testVariables() {
        // Try printing data for both variables and it should be the same

        val output = buildString {
            append("Leanplum variable value is ")
            append(leanplumVariable.value())
            appendLine()
            append("CleverTap variable value is ")
            append(clevertapVariable?.value())
        }

        println(output)
    }

}

class VariableWrapper<T>(
    val variableName: String,
    val defaultValue: T
) {

    @Variable
    private val leanplumVariable: Var<T> = Var.define(variableName, defaultValue)
    var clevertapVariable: com.clevertap.android.sdk.variables.Var<T>? = null

    fun setup(cleverTap: CleverTapAPI) {
        Parser.parseVariables(this)
        clevertapVariable = cleverTap.defineVariable(variableName, defaultValue)
    }

    fun value(): T {
        val a = clevertapVariable?.value()
        val b = leanplumVariable.value()

        // A/B code experiment setup, based on this return value
        return b
    }
}

class TestClientClass {

    @Variable
    private val lp1: Var<String> = Var.define("String variable", "Default value")
    @Variable
    private val lp2: Var<Boolean> = Var.define("Boolean variable", false)

    // some initializer block
    fun init() {
        Parser.parseVariables(this)
    }
}

class TestClientClassNew {

    @Variable
    private val lp1: VariableWrapper<String> = VariableWrapper("String variable", "Default value")
    @Variable
    private val lp2: VariableWrapper<Boolean> = VariableWrapper("Boolean variable", false)

    // some initializer block
    fun init() {
        Leanplum.addCleverTapInstanceCallback { clevertap ->
            lp1.setup(clevertap)
            lp2.setup(clevertap)
        }

    }

    fun regularCodeBlock() {

        // regular code where client accesses variables, it provides same value() method.
        println("Currently the token number is ${lp1.value()}")
    }
}
