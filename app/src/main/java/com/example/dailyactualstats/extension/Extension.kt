package com.example.dailyactualstats.extension

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
fun Activity.hideSoftKeybord(view: View) {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        view.windowToken,
        0
    )
}

fun <T : View?> Fragment.bindView(@IdRes idRes: Int): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        requireView().findViewById<T>(idRes)
    }
}