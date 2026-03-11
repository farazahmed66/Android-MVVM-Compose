package com.faraz.codewars.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun String?.toReadableDate(): String {
    if (this.isNullOrEmpty()) return ""

    return try {
        val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        input.timeZone = TimeZone.getTimeZone("UTC")

        val output = SimpleDateFormat("dd MMM yyyy | h:mm a", Locale.getDefault())

        val date = input.parse(this)
        output.format(date!!)
    } catch (_: Exception) {
        this
    }
}