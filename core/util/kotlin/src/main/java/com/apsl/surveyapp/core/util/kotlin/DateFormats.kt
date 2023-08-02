package com.apsl.surveyapp.core.util.kotlin

import java.text.SimpleDateFormat
import java.util.Locale

@Suppress("ConstantLocale")
object DateFormats {
    val surveyDateFormat = SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.getDefault())
}
