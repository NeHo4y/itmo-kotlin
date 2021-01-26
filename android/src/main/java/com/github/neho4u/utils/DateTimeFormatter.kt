package com.github.neho4u.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.atCurrentTimeZone(): ZonedDateTime = atOffset(UTC).atZoneSameInstant(ZoneId.systemDefault())
val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
