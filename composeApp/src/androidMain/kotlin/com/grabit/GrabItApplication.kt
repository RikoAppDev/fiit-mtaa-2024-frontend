package com.grabit

import android.content.Context
import androidx.startup.Initializer

//https://proandroiddev.com/how-to-avoid-asking-for-android-context-in-kotlin-multiplatform-libraries-api-d280a4adebd2

internal lateinit var applicationContext: Context
    private set

object GrabItApplication

class GrabItApplicationContextInitializer : Initializer<GrabItApplication> {
    override fun create(context: Context): GrabItApplication {
        applicationContext = context.applicationContext
        return GrabItApplication
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}