package com.infinity.my.utils

import android.view.View
import androidx.navigation.NavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

infix fun <T> T?.other(param: T): T = this ?: param

fun LocalDateTime.toMillis(
    zone: ZoneId = ZoneId.systemDefault(),
): Long? = atZone(zone)?.toInstant()?.toEpochMilli()

fun LocalDateTime.startOfDay(): Long = this
    .withHour(0)
    .withMinute(0)
    .withSecond(0)
    .withNano(0)
    .toMillis() other 0

fun LocalDateTime.endOfDay(): Long = this
    .withHour(23)
    .withMinute(59)
    .withSecond(59)
    .withNano(999)
    .toMillis() other 0

fun Long.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())

fun LocalDateTime.toDateTimeFormatter(format: String): String =
    format(DateTimeFormatter.ofPattern(format))



inline fun <reified T> Any.castTo() =
    if (this is T) this
    else throw NotImplementedError("Can`t cast to $this")

@ExperimentalCoroutinesApi
fun View.onClicked() = callbackFlow {
    setOnClickListener { offer(Unit) }
    awaitClose { setOnClickListener(null) }
}