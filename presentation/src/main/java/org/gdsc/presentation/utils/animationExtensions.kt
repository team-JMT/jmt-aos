package org.gdsc.presentation.utils

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

fun View.fadeIn(duration: Long = 200) {
    val self = this
    val animation = AlphaAnimation(0f, 1f)
    animation.duration = duration
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

fun View.fadeOut(duration: Long = 200) {
    val self = this
    val animation = AlphaAnimation(1f, 0f)
    animation.duration = duration
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

fun View.slideUp(duration: Long = 200) {
    val self = this
    ObjectAnimator
        .ofFloat(self, "translationY", 0f)
        .apply {
            setDuration(duration)
            start()
        }
}

fun View.slideDown(duration: Long = 200) {
    val self = this
    ObjectAnimator
        .ofFloat(self, "translationY", self.height.toFloat())
        .apply {
            setDuration(duration)
            start()
        }
}