package org.gdsc.presentation.view.custom

import androidx.constraintlayout.widget.ConstraintLayout
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.JmtEditTextBinding
import org.gdsc.presentation.utils.addAfterTextChangedListener

// TODO: 공백 처리 정책
class JmtEditText(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding = JmtEditTextBinding.inflate(LayoutInflater.from(context), this, true)

    val text get() = binding.nicknameEditText.text.toString()
    val editText = binding.nicknameEditText

    init {
        removeAllViews()
        addView(binding.root)
        setNicknameEditTextWatcher()
    }

    fun checkNickname() {
        with(binding) {

            val nicknameText = nicknameEditText.text.toString()

            if (nicknameText.isBlank()) {
                hideVerifyLayout()
                // TODO: 불가능한 닉네임 처리
            } else if (nicknameEditText.text.toString() == "불가능한 닉네임") {
                showVerifyLayout()
                verifyIcon.setImageResource(R.drawable.cancel_icon)
                verifyText.apply {
                    setTextColor(resources.getColor(R.color.unable_nickname_color, null))
                    text = context.getString(R.string.unable_nickname)
                }
            } else {
                showVerifyLayout()
                verifyIcon.setImageResource(R.drawable.check_icon)
                verifyText.apply {
                    setTextColor(resources.getColor(R.color.main600, null))
                    text = context.getString(R.string.enable_nickname)
                }
            }
        }
    }

    private fun setNicknameEditTextWatcher() {
        binding.nicknameEditText.addAfterTextChangedListener { checkNickname() }
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
