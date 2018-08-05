package com.github.krystiankowalik.splitme.api.transactionsservice.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

fun generateRandomId() = UUID.randomUUID().toString()

fun getCurrentTime()= LocalDateTime.now()
fun getCurrentDate() = LocalDate.now()