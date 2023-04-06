package org.gdsc.presentation.view.custom

import android.annotation.SuppressLint
import androidx.constraintlayout.widget.ConstraintLayout
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.JmtSearchEditTextBinding
import org.gdsc.presentation.utils.addAfterTextChangedListener

@SuppressLint("ClickableViewAccessibility")
class JmtSearchEditText(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding = JmtSearchEditTextBinding.inflate(LayoutInflater.from(context), this, true)

    val text get() = binding.searchEditText.text.toString()

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

        setIconAndCancelVisibility()
        setCancel()

    }
    private fun setIconAndCancelVisibility() {
        binding.searchEditText.addAfterTextChangedListener {
            if (text.isEmpty()) {
                binding.searchIcon.visibility = View.VISIBLE
                binding.cancelText.visibility = View.GONE
            }
            else {
                binding.cancelText.visibility = View.VISIBLE
                binding.searchIcon.visibility = View.GONE
            }
        }
    }

    private fun setCancel() {
        binding.cancelText.setOnClickListener {
            binding.searchEditText.text?.clear()
            binding.searchEditText.clearFocus()
            hideKeyboardFromEditText()
        }
    }

    private fun hideKeyboardFromEditText() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }


}
