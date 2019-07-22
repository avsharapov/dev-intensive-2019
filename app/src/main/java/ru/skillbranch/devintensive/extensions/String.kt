package ru.skillbranch.devintensive.extensions

import java.lang.StringBuilder

fun String.truncate(charAmount: Int = 16): String {
    val trimStr = this.trim()
    if (trimStr.length <= charAmount || charAmount <= 0) return trimStr

    return with(trimStr.subSequence(0, charAmount)) {
          "${this.trim()}..."
    }
}

fun String.stripHtml(): String =
    buildString {
        var tag = false
        var previousCharIsNotWhitespace = true

        this@stripHtml.forEach {
            if (it == '<') {
                tag = true
            }

            if (!tag) {
                val currentCharIsWhitespace = it.isWhitespace()
                if (previousCharIsNotWhitespace || !currentCharIsWhitespace) {
                    this@buildString.append(it)
                }

                previousCharIsNotWhitespace = !currentCharIsWhitespace

            } else if (it == '>') {
                tag = false
            }
        }
    }