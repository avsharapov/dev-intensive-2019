package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class User(
        val id: String,
        var firstName: String?,
        var lastName: String?,
        var avatar: String?,
        var rating: Int = 0,
        var respect: Int = 0,
        var lastVisit: Date? = Date(),
        var isOnline: Boolean = false
) {
    constructor(id: String, firstName: String?, lastName: String?) : this(
            id = id,
            firstName = firstName,
            lastName = lastName,
            avatar = null
    )

    companion object Factory {
        private var lastId: Int = -1
        fun makeUser(fullName: String?): User {
            lastId++
            val (firstName, lastName) = Utils.parseFullName(fullName)
            return User(lastId.toString(), firstName = firstName, lastName = lastName)
        }
    }

    class Builder {
        private var id: String = (Factory.lastId++).toString()
        private var firstName: String? = null
        private var lastName: String? = null
        private var avatar: String? = null
        private var rating: Int = 0
        private var respect: Int = 0
        private var lastVisit: Date? = Date()
        private var isOnline: Boolean = false

        fun id(id: String): Builder {
            this.id = id
            return this
        }

        fun firstName(firstName: String?): Builder {
            this.firstName = firstName
            return this
        }

        fun lastName(lastName: String?): Builder {
            this.lastName = lastName
            return this
        }

        fun avatar(avatar: String?): Builder {
            this.avatar = avatar
            return this
        }

        fun rating(rating: Int): Builder {
            this.rating = rating
            return this
        }

        fun respect(respect: Int): Builder {
            this.respect = respect
            return this
        }

        fun lastVisit(lastVisit: Date?): Builder {
            this.lastVisit = lastVisit
            return this
        }

        fun isOnline(isOnline: Boolean): Builder {
            this.isOnline = isOnline
            return this
        }

        fun build() = User(
                id,
                firstName,
                lastName,
                avatar,
                rating,
                respect,
                lastVisit,
                isOnline
        )
    }
}