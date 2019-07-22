package ru.skillbranch.devintensive.utils

import java.lang.IllegalArgumentException

object Utils {

    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts = fullName?.split(' ')

        val firstName = parts
            ?.getOrNull(0)
            ?.getNotEmptyOrNull()
        val lastName = parts
            ?.getOrNull(1)
            ?.getNotEmptyOrNull()

        return firstName to lastName
    }

    private fun String.getNotEmptyOrNull(): String? =
        if (this.isEmpty()) null else this

    fun transliteration(payload: String, divider: String = " "): String =
        buildString {
            payload.forEach { char ->
                if (char.isWhitespace()) {
                    this.append(divider)
                    return@forEach
                }

                val translateStr = CharacterDictionary.charMap[char.toLowerCase()]
                translateStr?.let { trStr ->
                    this.append(
                        if (char.isUpperCase()) {
                            val firstChar = trStr[0].toUpperCase()
                            val otherChar = trStr.subSequence(1, trStr.length)
                            "$firstChar$otherChar"
                        } else {
                            trStr
                        }
                    )
                } ?: this.append(char)
            }
        }

    fun toInitials(firstName: String?, lastName: String?): String? {
        if (firstName.isNullOrBlank() && lastName.isNullOrBlank()) return null

        val firstNameSymbol = firstName?.getNotEmptyOrNull()?.first()?.toUpperCase()
        val lastNameSymbol = lastName?.getNotEmptyOrNull()?.first()?.toUpperCase()

        return "${firstNameSymbol ?: ""}${lastNameSymbol ?: ""}"
    }
}