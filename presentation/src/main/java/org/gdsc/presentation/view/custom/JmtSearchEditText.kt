package org.gdsc.presentation.view.custom

import android.annotation.SuppressLint
import androidx.constraintlayout.widget.ConstraintLayout
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.gdsc.presentation.R
import org.gdsc.presentation.base.CancelViewListener
import org.gdsc.presentation.base.SearchViewListener
import org.gdsc.presentation.databinding.JmtSearchEditTextBinding
import org.gdsc.presentation.utils.fadeIn
import org.gdsc.presentation.utils.fadeOut
import kotlin.coroutines.CoroutineContext

@SuppressLint("ClickableViewAccessibility")
class JmtSearchEditText(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding = JmtSearchEditTextBinding.inflate(LayoutInflater.from(context), this, true)

    val text get() = binding.searchEditText.text.toString()
    val editText get() = binding.searchEditText

    private var searchViewListener: SearchViewListener? = null
    private var cancelViewListener: CancelViewListener? = null
    private var afterTextChanged: ((String?) -> Unit)? = null
    private var autoSubmit = false

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
        setClear()
        setAnimation()
        setEditorKeyListener()
        setTextChangedListener()

    }

    private fun setCancel() {
        binding.cancelText.setOnClickListener {
            binding.searchEditText.text?.clear()
            binding.searchEditText.clearFocus()
            hideKeyboardFromEditText()
            binding.searchIcon.fadeIn()
            cancelViewListener?.onCancel()
        }
    }

    private fun setClear() {
        binding.clearIcon.setOnClickListener {
            binding.searchEditText.text?.clear()
            searchViewListener?.onSearchClear()
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

    private fun setEditorKeyListener() {
        binding.searchEditText.setOnEditorActionListener { v, actionID, event ->

            if (actionID == EditorInfo.IME_ACTION_SEARCH) {
                clearFocus()
                hideKeyboardFromEditText()
                searchViewListener?.onSubmitText(v.text.toString())
                true
            } else {
                false
            }
        }

        binding.searchEditText.onKey { _, keyCode, _ ->

            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                clearFocus()
                hideKeyboardFromEditText()
                searchViewListener?.onSubmitText(binding.searchEditText.text.toString())
            }
        }
    }


    private fun setTextChangedListener() {
        binding.searchEditText.textChangedListener {
            onTextChanged { charSequence, _, _, _ ->
                binding.clearIcon.isVisible = !charSequence.isNullOrEmpty()
                searchViewListener?.onChangeText(charSequence ?: "")
                if (autoSubmit) {
                    searchViewListener?.onSubmitText(binding.searchEditText.text.toString())
                    autoSubmit = false
                }
            }

            afterTextChanged {
                afterTextChanged?.invoke(it.toString())
            }

        }
    }
    private fun hideKeyboardFromEditText() {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }

    fun setSearchViewListener(searchViewListener: SearchViewListener) {
        this.searchViewListener = searchViewListener
    }

    fun setCancelViewListener(cancelViewListener: CancelViewListener) {
        this.cancelViewListener = cancelViewListener
    }

    fun setSearchText(text: String) {
        binding.searchEditText.setText(text)
    }

    fun setAfterTextChanged(action: (String?) -> Unit) {
        afterTextChanged = action
    }

    fun android.view.View.onKey(
        context: CoroutineContext = Dispatchers.Main,
        returnValue: Boolean = false,
        handler: suspend CoroutineScope.(v: android.view.View, keyCode: Int, event: android.view.KeyEvent?) -> Unit
    ) {
        setOnKeyListener { v, keyCode, event ->
            GlobalScope.launch(context, CoroutineStart.DEFAULT) {
                handler(v, keyCode, event)
            }
            returnValue
        }
    }

    fun android.widget.TextView.textChangedListener(
        context: CoroutineContext = Dispatchers.Main,
        init: __TextWatcher.() -> Unit
    ) {
        val listener = __TextWatcher(context)
        listener.init()
        addTextChangedListener(listener)
    }
}

class __TextWatcher(private val context: CoroutineContext) : android.text.TextWatcher {

    private var _beforeTextChanged: (suspend CoroutineScope.(CharSequence?, Int, Int, Int) -> Unit)? = null


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        val handler = _beforeTextChanged ?: return
        GlobalScope.launch(context) {
            handler(s, start, count, after)
        }
    }

    fun beforeTextChanged(
        listener: suspend CoroutineScope.(CharSequence?, Int, Int, Int) -> Unit
    ) {
        _beforeTextChanged = listener
    }

    private var _onTextChanged: (suspend CoroutineScope.(CharSequence?, Int, Int, Int) -> Unit)? = null


    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val handler = _onTextChanged ?: return
        GlobalScope.launch(context) {
            handler(s, start, before, count)
        }
    }

    fun onTextChanged(
        listener: suspend CoroutineScope.(CharSequence?, Int, Int, Int) -> Unit
    ) {
        _onTextChanged = listener
    }

    private var _afterTextChanged: (suspend CoroutineScope.(android.text.Editable?) -> Unit)? = null


    override fun afterTextChanged(s: android.text.Editable?) {
        val handler = _afterTextChanged ?: return
        GlobalScope.launch(context) {
            handler(s)
        }
    }

    fun afterTextChanged(
        listener: suspend CoroutineScope.(android.text.Editable?) -> Unit
    ) {
        _afterTextChanged = listener
    }

}