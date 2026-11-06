package com.alborihi.myapp.presentation.ui.extension

import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(format: String = "dd/MM/yyyy"): Date? {
    return try {
        SimpleDateFormat(format, Locale.getDefault()).parse(this)
    } catch (e: Exception) {
        null
    }
}

fun String.capitalize(): String {
    return if (this.isEmpty()) this else this[0].uppercase() + this.substring(1)
}

fun String.toTitleCase(): String {
    return this.split(" ").joinToString(" ") { it.capitalize() }
}

fun Double.formatPercentage(): String {
    return String.format("%.1f%%", this)
}

fun Long.formatDate(format: String = "dd/MM/yyyy"): String {
    return try {
        SimpleDateFormat(format, Locale.getDefault()).format(Date(this))
    } catch (e: Exception) {
        this.toString()
    }
}
