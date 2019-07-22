package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity() {

    lateinit var benderObj: Bender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val status = savedInstanceState?.getString("STATUS") ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION") ?: Bender.Question.NAME.name
        val wrongAnswerCounter = savedInstanceState?.getInt("WRONG_ANSWER_COUNTER") ?: 0
        benderObj = makeBender(status, question, wrongAnswerCounter)

        val text = benderObj.askQuestion()
        val (r, g, b) = benderObj.status.color
        showBenderPhrase(text, r, g, b)

        Log.w("M_MainActivity", "onCreate $status $question")

        iv_send.setOnClickListener {
            answerToBender()
        }

        et_message.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    this@MainActivity.hideKeyboard()
                    answerToBender()
                    true
                }
                else -> false
            }
        }
    }

    private fun answerToBender() {
        val answer = et_message.text.toString()
        val (phrase, color) = benderObj.listenAnswer(answer)
        et_message.setText("")
        val (r, g, b) = color
        showBenderPhrase(phrase, r, g, b)
    }

    private fun makeBender(status: String, question: String, wrongAnswerCounter: Int): Bender =
        Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question)).also {
            it.wrongAnswerCounter = wrongAnswerCounter
        }

    private fun showBenderPhrase(text: String, red: Int, green: Int, blue: Int) {
        tv_text.text = text
        iv_bender.setColorFilter(Color.rgb(red, green, blue), PorterDuff.Mode.MULTIPLY)
    }

    override fun onStart() {
        super.onStart()
        Log.w("M_MainActivity", "onStart")

    }

    override fun onRestart() {
        super.onRestart()
        Log.w("M_MainActivity", "onRestart")

    }

    override fun onResume() {
        super.onResume()
        Log.w("M_MainActivity", "onResume")

    }

    override fun onPause() {
        super.onPause()
        Log.w("M_MainActivity", "onPause")

    }

    override fun onStop() {
        super.onStop()
        Log.w("M_MainActivity", "onStop")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("M_MainActivity", "onDestroy")

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("STATUS", benderObj.status.name)
        outState.putString("QUESTION", benderObj.question.name)
        outState.putInt("WRONG_ANSWER_COUNTER", benderObj.wrongAnswerCounter)
        Log.w("M_MainActivity", "onSaveInstanceState ${benderObj.status.name} ${benderObj.question.name}")

    }
}
