package org.gdsc.domain

import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateManager {

    private const val FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"
    private const val VALID_MINUTE = 20

    private val simpleDateFormat = SimpleDateFormat(FORMAT, Locale.ROOT)
    private val dateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT)

    private val curDateTime
        get() = LocalDateTime.parse(simpleDateFormat.format(System.currentTimeMillis()), dateTimeFormatter)

    fun isValidAccessToken(expiresIn: Long): Boolean {

        val expiresInDateTime = LocalDateTime.parse(simpleDateFormat.format(expiresIn), dateTimeFormatter)

        return Duration.between(curDateTime, expiresInDateTime).toMinutes() > VALID_MINUTE
    }

}