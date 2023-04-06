package org.gdsc.presentation.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText

class TouchEventSpreadableEditText(context: Context, attrs: AttributeSet) :
    AppCompatEditText(context, attrs) {

    // 포커스가 없다면 부모에게 터치 이벤트 전달
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (this.isFocusable) super.onTouchEvent(event)
        else false
    }
}