package ru.skillbranch.devintensive.extensions

import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

enum class TimeUnits(val one: String, val few: String, val many: String) {
    SECOND("секунду", "секунды", "секунд"),
    MINUTE("минуту", "минуты", "минут"),
    HOUR("час", "часа", "часов"),
    DAY("день", "дня", "дней")
}

fun TimeUnits.plural(value: Int): String {
    val time = when {
        value == 1 || (value.rem(10) == 1 && value != 11) -> this.one
        value.rem(10) in 2..4 -> this.few
        with(value.rem(10)) {
            this == 0 || this in 5..19
        } -> this.many
        else -> throw IllegalStateException("can not make plural")
    }

    return "$value $time"
}

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    val ms = when (units) {
        TimeUnits.SECOND -> SECOND
        TimeUnits.MINUTE -> MINUTE
        TimeUnits.HOUR -> HOUR
        TimeUnits.DAY -> DAY
    }

    this.time += ms * value
    return this
}

fun Date.humanizeDiff(startDate: Date = Date()): String {
    val diffMs = this.time - startDate.time

    val oneSecond = 1000
    val fortyFiveSeconds = TimeUnit.SECONDS.toMillis(45)
    val seventyFiveSeconds = TimeUnit.SECONDS.toMillis(75)
    val fortyFiveMinutes = TimeUnit.MINUTES.toMillis(45)
    val seventyFiveMinutes = TimeUnit.MINUTES.toMillis(75)
    val twentyTwoHours = TimeUnit.HOURS.toMillis(22)
    val twentySixHours = TimeUnit.HOURS.toMillis(26)
    val threeHundredSixtyDays = TimeUnit.DAYS.toMillis(360)

    val past = diffMs < 0
    val optionPrefix = "через "
    val optionPostfix = " назад"

    return when (diffMs.absoluteValue) {
        in 0..oneSecond -> "только что"

        in oneSecond + 1..fortyFiveSeconds -> if (past) "несколько секунд назад" else "через несколько секунд"

        in fortyFiveSeconds + 1..seventyFiveSeconds -> if (past) "минуту назад" else "через минуту"

        in seventyFiveSeconds + 1..fortyFiveMinutes -> makePluralHumanizeStr(
            past,
            diffMs.absoluteValue,
            TimeUnits.MINUTE
        )
        in fortyFiveMinutes + 1..seventyFiveMinutes -> if (past) "час назад" else "через час"

        in seventyFiveMinutes + 1..twentyTwoHours -> makePluralHumanizeStr(
            past,
            diffMs.absoluteValue,
            TimeUnits.HOUR
        )

        in twentyTwoHours + 1..twentySixHours -> if (past) "день назад" else "через день"

        in twentySixHours + 1..threeHundredSixtyDays -> makePluralHumanizeStr(
            past,
            diffMs.absoluteValue,
            TimeUnits.DAY
        )

        else -> if (past) "более года назад" else "более чем через год"
    }
}

private fun makePluralHumanizeStr(
    past: Boolean,
    valueMs: Long,
    timeUnit: TimeUnits
): String {
    val coreStr = when (timeUnit) {
        TimeUnits.SECOND -> with(valueMs / 1000) {
            TimeUnits.SECOND.plural(this.toInt())
        }

        TimeUnits.MINUTE -> with(valueMs / (60 * 1000)) {
            TimeUnits.MINUTE.plural(this.toInt())
        }

        TimeUnits.HOUR -> with(valueMs / (60 * 60 * 1000)) {
            TimeUnits.HOUR.plural(this.toInt())
        }

        TimeUnits.DAY -> with(valueMs / (24 * 60 * 60 * 1000)) {
            TimeUnits.DAY.plural(this.toInt())
        }
    }
    val (prefix, postfix) = if (past) "" to " назад" else "через " to ""
    return buildString {
        append(prefix)
        append(coreStr)
        append(postfix)
    }
}