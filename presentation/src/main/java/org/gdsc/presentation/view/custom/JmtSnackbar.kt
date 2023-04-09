package org.gdsc.presentation.view.custom

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar.Duration
import com.google.android.material.snackbar.Snackbar
import org.gdsc.presentation.databinding.JmtSnackbarBinding

class JmtSnackbar private constructor(
    private val view: View,
    message: String,
    @Duration duration: Int
) {

    private val snackbar by lazy {
        Snackbar.make(view, "", duration)
    }
    private val snackbarLayout = snackbar.view as (Snackbar.SnackbarLayout)

    private val context = view.context
    private lateinit var binding: JmtSnackbarBinding

    companion object {
        fun make(view: View, message: String, @Duration duration: Int): JmtSnackbar {
            return JmtSnackbar(view, message, duration)
        }
    }

    init {
        setSnackbarView()
        setSnackbarText(message)
    }

    private fun setSnackbarView() {

        binding = JmtSnackbarBinding.inflate(LayoutInflater.from(context), snackbarLayout, false)

        snackbarLayout.run {
            removeAllViews()
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(binding.root)
        }

    }

    private fun setSnackbarText(message: String) {
        binding.snackbarText.text = message
    }

    fun setPadding(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0): JmtSnackbar {
        snackbarLayout.setPadding(left, top, right, bottom)
        return this
    }

    fun setTextColor(color: Int): JmtSnackbar {
        binding.snackbarText.setTextColor(color)
        return this
    }

    // TODO: Blur
    fun setBackgroundAlpha(alpha: Float): JmtSnackbar {
        binding.snackbarContainer.background.alpha = (alpha * 255).toInt()
        binding.snackbarContainer.elevation = 0f
        return this
    }

    fun show() {
        snackbar.show()
    }

}