package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import ru.skillbranch.devintensive.R

fun Activity.hideKeyboard() {
    currentFocus?.let { v ->
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).also { inputMethodManager ->
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
}

fun Activity.isKeyboardOpen(): Boolean {
    val permissibleError = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50F, resources.displayMetrics).toInt()
    val rootView = findViewById<View>(android.R.id.content)
    val rect = Rect()
    rootView.getWindowVisibleDisplayFrame(rect)
    //Log.d("M_Activity","${rootView.height}/${rect.height()}")
    val difference = rootView.height - rect.height()
    return (difference > permissibleError)
}

fun Activity.isKeyboardClosed(): Boolean {
    return !this.isKeyboardOpen()
}