package com.apsl.surveyapp.initializers

import android.content.Context
import androidx.startup.Initializer
import com.apsl.surveyapp.BuildConfig
import timber.log.Timber

@Suppress("Unused")
class TimberInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
