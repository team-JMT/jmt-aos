package org.gdsc.presentation.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import org.gdsc.domain.Empty

val Fragment.deviceMetrics
    get() = run {

        val displayMetrics = DisplayMetrics()

        // target version above 30
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            val windowManager = requireActivity().windowManager

            // target version above 31
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

                val metrics = windowManager.currentWindowMetrics
                metrics.bounds.also { bounds ->
                    displayMetrics.widthPixels = bounds.width()
                    displayMetrics.heightPixels = bounds.height()
                }
            } else {
                val display = requireActivity().display
                display?.getRealMetrics(displayMetrics)
            }

        } else {
            requireActivity().windowManager.defaultDisplay?.getMetrics(displayMetrics)
        }
        displayMetrics
    }

fun Fragment.hideKeyBoard(activity: Activity) {
    val inputMethodManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
}

fun Fragment.showKeyBoard(activity: Activity, view: View) {
    view.requestFocus()
    val inputMethodManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, 0)
}

fun LifecycleOwner.repeatWhenUiStarted(block: suspend () -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            block.invoke()
        }
    }
}

@SuppressLint("Range")
fun Uri.findPath(context: Context): String {
    val cursor: Cursor? = context.contentResolver.query(this, null, null, null, null)
    cursor.use {
        cursor?.moveToNext()
        return cursor?.getString(cursor.getColumnIndex("_data")) ?: String.Empty
    }
}