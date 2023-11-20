package com.newOs.colorCraze.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun getCurrentDate(): String = SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault()).format(Date())
}
