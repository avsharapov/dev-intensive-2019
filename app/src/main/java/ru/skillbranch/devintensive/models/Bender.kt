package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    var wrongAnswerCounter = 0

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question

    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> =
        when {
            !question.isValid(answer) -> {
                "${question.warningMsg}\n${question.question}" to status.color
            }

            !question.answers.contains((answer.toLowerCase())) -> {
                wrongAnswerCounter++
                if (wrongAnswerCounter > 3) {
                    wrongAnswerCounter = 0
                    reset()
                    "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
                } else {
                    status = status.nextStatus()
                    "Это неправильный ответ\n${question.question}" to status.color
                }
            }

            else -> {
                wrongAnswerCounter = 0
                question = question.nextQuestion()
                "Отлично - ты справился\n${question.question}" to status.color
            }
        }

    private fun reset() {
        status = Status.NORMAL
        question = Question.NAME
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status =
            if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override val warningMsg: String
                get() = "Имя должно начинаться с заглавной буквы"

            override fun isValid(answer: String): Boolean =
                answer.isEmpty() || answer.first().isUpperCase()

            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override val warningMsg: String
                get() = "Профессия должна начинаться со строчной буквы"

            override fun isValid(answer: String): Boolean =
                answer.isEmpty() || answer.first().isLowerCase()

            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override val warningMsg: String
                get() = "Материал не должен содержать цифр"

            override fun isValid(answer: String): Boolean {
                if (answer.isEmpty()) return true
                for (char: Char in answer) {
                    if (char.isDigit()) return false
                }
                return true
            }

            override fun nextQuestion(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override val warningMsg: String
                get() = "Год моего рождения должен содержать только цифры"

            override fun isValid(answer: String): Boolean {
                if (answer.isEmpty()) return true
                return try {
                    answer.toInt()
                    true
                } catch (ex: NumberFormatException) {
                    false
                }
            }

            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override val warningMsg: String
                get() = "Серийный номер содержит только цифры, и их 7"

            override fun isValid(answer: String): Boolean {
                if (answer.isEmpty()) return true
                return try {
                    answer.toInt()
                    answer.length == 7
                } catch (ex: NumberFormatException) {
                    false
                }
            }

            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override val warningMsg: String
                get() = ""

            override fun isValid(answer: String): Boolean =
                true

            override fun nextQuestion(): Question = NAME
        };

        abstract val warningMsg: String
        abstract fun isValid(answer: String): Boolean
        abstract fun nextQuestion(): Question
    }
}