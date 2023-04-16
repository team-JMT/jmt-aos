package org.gdsc.presentation.utils

import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
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

fun View.fadeIn() {
    val self = this
    val animation = AlphaAnimation(0f, 1f)
    animation.duration = 200
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {
            self.visibility = View.VISIBLE
        }

        override fun onAnimationRepeat(animation: Animation) {}
        override fun onAnimationEnd(animation: Animation) {
        }
    })
    self.startAnimation(animation)
}

fun View.fadeOut() {
    val self = this
    val animation = AlphaAnimation(1f, 0f)
    animation.duration = 200
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {
            self.visibility = View.GONE
        }

        override fun onAnimationRepeat(animation: Animation) {}
        override fun onAnimationEnd(animation: Animation) {
        }
    })
    self.startAnimation(animation)
}

fun View.getAbsolutePositionOnScreen(): Pair<Float, Float> {
    val location = IntArray(2)
    getLocationOnScreen(location)
    return Pair(location[0].toFloat(), location[1].toFloat())
}

val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()