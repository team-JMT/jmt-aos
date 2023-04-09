package org.gdsc.presentation.view.custom

import android.annotation.SuppressLint
import androidx.constraintlayout.widget.ConstraintLayout
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.JmtSearchEditTextBinding
import org.gdsc.presentation.utils.fadeIn
import org.gdsc.presentation.utils.fadeOut

@SuppressLint("ClickableViewAccessibility")
class JmtSearchEditText(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding = JmtSearchEditTextBinding.inflate(LayoutInflater.from(context), this, true)

    val text get() = binding.searchEditText.text.toString()
    val editText get() = binding.searchEditText

    init {

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.JmtSearchEditText,
            0, 0
        ).apply {
            try {
                getBoolean(R.styleable.JmtSearchEditText_editTextEnabled, true).apply {
                    binding.searchEditText.isFocusable = this
                }
            } finally {
                recycle()
            }
        }

        removeAllViews()
        addView(binding.root)

        setCancel()
        setAnimation()

    }

    private fun setCancel() {
        binding.cancelText.setOnClickListener {
            binding.searchEditText.text?.clear()
            binding.searchEditText.clearFocus()
            hideKeyboardFromEditText()
            binding.searchIcon.fadeIn()
        }
    }


    private fun setAnimation() {

        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.searchIcon.fadeOut()
                binding.cancelText.fadeIn()
            } else {
                binding.searchIcon.fadeIn()
                binding.cancelText.fadeOut()
            }
        }

    }

    private fun hideKeyboardFromEditText() {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }

}
