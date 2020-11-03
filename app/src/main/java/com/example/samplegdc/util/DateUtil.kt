package com.example.samplegdc.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun getCurrentTimestamp(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return dateFormat.format(Date())
    }
}