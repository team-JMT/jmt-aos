package org.gdsc.presentation.base

interface SearchViewListener {
    fun changeFocus(focus: Boolean)
    fun onChangeText(text: CharSequence)
    fun onSubmitText(text: CharSequence)
    fun onSearchClear()
}

interface CancelViewListener{
    fun onCancel()
}