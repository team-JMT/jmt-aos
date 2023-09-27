package org.gdsc.presentation.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.textfield.TextInputEditText
import org.gdsc.presentation.R

class ClassicEditText(context: Context, attrs: AttributeSet) : TextInputEditText(context, attrs) {
    private val underlinePaint = Paint()
    private var clearIcon: Drawable? = null
    private var showClearIcon = false

    init {
        inputType = TYPE_CLASS_TEXT
        setLines(1)

        underlinePaint.color = ContextCompat.getColor(context, R.color.grey400)
        underlinePaint.strokeWidth = 4f

        minHeight = height

        // EditText 기본 배경 지우기
        background = null
        setPadding(0, paddingTop, paddingRight, paddingBottom)

        // Clear 아이콘 초기화
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_cancel)
        clearIcon = DrawableCompat.wrap(drawable!!)
        clearIcon!!.setBounds(0, 0, clearIcon!!.intrinsicWidth, clearIcon!!.intrinsicHeight)
        val padding = resources.getDimensionPixelSize(R.dimen.drawable_icon_spacing)
        setCompoundDrawablePadding(padding)

        // Clear 아이콘 보이기/숨기기 설정
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setClearIconVisible(s?.isNotEmpty() == true)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setClearIconVisible(visible: Boolean) {
        showClearIcon = visible
        setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            if (visible) clearIcon else null,
            null
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val startY = height
        val scrollX = scrollX

        canvas.drawLine(
            scrollX.toFloat(),
            startY.toFloat() - paddingBottom,
            (scrollX + width).toFloat(),
            startY.toFloat() - paddingBottom,
            underlinePaint
        )
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)

        // 포커스가 없을 때 Clear 아이콘 숨기기
        if (!focused) {
            setClearIconVisible(false)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Clear 아이콘 클릭 이벤트
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (showClearIcon && event.x >= width - paddingRight - clearIcon!!.intrinsicWidth) {
                setText("")
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}
