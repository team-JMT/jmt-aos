package org.gdsc.presentation.view.custom

import androidx.constraintlayout.widget.ConstraintLayout
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.JmtEditTextBinding
import org.gdsc.presentation.utils.ValidationUtils
import org.gdsc.presentation.utils.addAfterTextChangedListener

// TODO: 공백 처리 정책
class JmtEditText(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding = JmtEditTextBinding.inflate(LayoutInflater.from(context), this, true)

    val text get() = binding.nicknameEditText.text.toString()
    val editText = binding.nicknameEditText

    init {

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.JmtEditText,
            0, 0
        ).apply {
            try {
                val isVerifyEnable = getBoolean(R.styleable.JmtEditText_verifyTextEnabled, false)
                if (isVerifyEnable.not()) {
                    binding.verifyContainer.visibility = View.GONE
                } else {
                    setNicknameEditTextWatcher()
                }
                getString(R.styleable.JmtEditText_jmtEditTextHint)?.let {
                    binding.nicknameEditText.hint = it
                    binding.nicknameEditText.setHintTextColor(ContextCompat.getColor(context, R.color.grey300))
                }
            } finally {
                recycle()
            }
        }

        removeAllViews()
        addView(binding.root)

    }

    private fun checkNickname(inputText: String) {
        with(binding) {

            if (inputText.isBlank()) {
                hideVerifyLayout()
                // 특수문자 포함 시
            } else if (ValidationUtils.checkName(inputText)) {
                showVerifyLayout()
                verifyIcon.setImageResource(R.drawable.cancel_icon)
                verifyText.apply {
                    setTextColor(resources.getColor(R.color.unable_nickname_color, null))
                    text = context.getString(R.string.unable_nickname_cause_special_characters)
                }
            } else {
                showVerifyLayout()
                verifyIcon.setImageResource(R.drawable.check_icon)
                verifyText.apply {
                    setTextColor(resources.getColor(R.color.green500, null))
                    text = context.getString(R.string.enable_nickname)
                }
            }

            if (inputText.length == 10) {
                Toast.makeText(context, "닉네임은 최대 10글자입니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setDuplicatedNickname() {
        with(binding) {
            showVerifyLayout()
            verifyIcon.setImageResource(R.drawable.cancel_icon)
            verifyText.apply {
                setTextColor(resources.getColor(R.color.unable_nickname_color, null))
                text = context.getString(R.string.unable_nickname_cause_duplicated)
            }
        }
    }

    private fun setNicknameEditTextWatcher() {
        binding.nicknameEditText.addAfterTextChangedListener { checkNickname(it) }
    }

    private fun hideVerifyLayout() {
        binding.verifyIcon.visibility = View.INVISIBLE
        binding.verifyText.visibility = View.INVISIBLE
    }

    private fun showVerifyLayout() {
        binding.verifyIcon.visibility = View.VISIBLE
        binding.verifyText.visibility = View.VISIBLE
    }
}
