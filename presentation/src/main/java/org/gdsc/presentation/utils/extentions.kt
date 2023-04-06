package org.gdsc.presentation.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

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
    val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
}

fun Fragment.showKeyBoard(activity: Activity, view: View) {
    view.requestFocus()
    val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, 0)
}

fun EditText.addAfterTextChangedListener(block: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            block(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}

fun LifecycleOwner.repeatWhenUiStarted(block: suspend () -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            block.invoke()
        }
    }
}