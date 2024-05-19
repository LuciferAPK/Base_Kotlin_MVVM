package com.cyrus.base_kotlin_mvvm.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object KeyboardUtils {
    fun hideKeyboard(activity: Activity?) {
        if (null != activity) {
            val view = activity.currentFocus
            if (view != null) {
                val inputManager =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(
                    view.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }

    fun hideKeyboard(view: View, context: Context?) {
        if (null != context) {
            val inputManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    fun showKeyboard(activity: Activity, view: View?) {
        if (null != view) {
            view.requestFocus()
            view.postDelayed(Runnable {
                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
            }, 200)
        }
    }

    fun showSoftKeyboard(editText: EditText) {
        (editText.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
            editText,
            InputMethodManager.SHOW_IMPLICIT
        )
    }
}