package org.gdsc.presentation.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText

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

fun View.getAbsolutePositionOnScreen(): Pair<Float, Float> {
    val location = IntArray(2)
    getLocationOnScreen(location)
    return Pair(location[0].toFloat(), location[1].toFloat())
}