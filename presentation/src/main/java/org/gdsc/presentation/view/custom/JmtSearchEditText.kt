package org.gdsc.presentation.view.custom

import android.annotation.SuppressLint
import androidx.constraintlayout.widget.ConstraintLayout
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.JmtSearchEditTextBinding

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
    }


}
