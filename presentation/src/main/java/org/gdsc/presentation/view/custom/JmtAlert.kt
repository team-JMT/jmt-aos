package org.gdsc.presentation.view.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.gdsc.presentation.BaseActivity
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.ComponentAlertBaseBinding
import org.gdsc.presentation.utils.toDp

class JmtAlert(private val context: Context) {

    companion object {
        private var dialog: AlertDialog? = null

        const val FILL_FILL = 0
        const val FILL_OUTLINE = 1
    }

    private val builder: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
    private val binding: ComponentAlertBaseBinding = ComponentAlertBaseBinding.inflate(
        LayoutInflater.from(context)
    )

    private var inputView: JmtEditText? = null
    private var contentView: View? = null

    private var dismissWithCancel: Boolean = false

    fun title(title: String): JmtAlert {
        binding.dialogTitle.text = title
        return this
    }

    fun title(titleRes: Int): JmtAlert {
        return this.title(context.getString(titleRes))
    }

    fun setCloseButton(isVisible: Boolean = true): JmtAlert {
        with(binding.dialogCloseBtn) {
            visibility = if (isVisible) View.VISIBLE else View.GONE
            setOnClickListener { dismiss() }
        }
        return this
    }

    fun isCancelable(cancelable: Boolean): JmtAlert {
        builder.setCancelable(
            if (dismissWithCancel) false else cancelable
        )
        return this
    }

    fun dismissWithCancel(dismissWithCancel: Boolean): JmtAlert {
        this.dismissWithCancel = dismissWithCancel
        if (dismissWithCancel) {
            builder.setCancelable(false)
            if (context is BaseActivity) builder.setOnCancelListener { context.finish() }
        }
        return this
    }

    fun content(content: String): JmtAlert {
        TextView(context).apply {
            gravity = Gravity.CENTER
            text = content
        }.let {
            binding.dialogContentLay.removeAllViewsInLayout()
            binding.dialogContentLay.isVisible = true
            binding.dialogContentLay.addView(it)
        }
        return this
    }

    fun show(): AlertDialog? {
        if (dialog?.isShowing != true) {
            builder.setView(binding.root)
            dialog = builder.create()
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window?.setLayout(
                context.resources.getDimensionPixelSize(R.dimen.alert_width),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            if (binding.dialogTitle.text.isNullOrEmpty()) {
                binding.dialogTitle.isVisible = false
            }
            dialog?.show()
        }

        return dialog
    }

    fun show(onDialogViewBinding: JmtAlert.(ComponentAlertBaseBinding) -> Unit): AlertDialog? {
        onDialogViewBinding(binding)
        return show()
    }

    fun dismiss() {
        if (dialog?.isShowing == true)
            dialog?.dismiss()
    }


    inner class MultiButtonBuilder {
        var leftButton: AppCompatButton? = null
        var rightButton: AppCompatButton? = null

        fun leftButton(
            text: String,
            _fillType: Int = FILL_FILL,
            autoDismiss: Boolean = true,
            onClick: (View) -> Unit = {}
        ) {
            leftButton = AppCompatButton(context).apply {
                this.text = text
                this.layoutParams =
                    LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                        height = 48.toDp
                    }
                this.background = when (_fillType) {
                    FILL_FILL -> ContextCompat.getDrawable(
                        context,
                        R.drawable.jmt_button_background_main
                    )

                    FILL_OUTLINE -> ContextCompat.getDrawable(
                        context,
                        R.drawable.jmt_button_outline_background_main
                    )

                    else -> ContextCompat.getDrawable(
                        context,
                        R.drawable.jmt_button_background_main
                    )
                }
                this.setTextColor(
                    when (_fillType) {
                        FILL_FILL -> ContextCompat.getColor(context, R.color.white)
                        FILL_OUTLINE -> ContextCompat.getColor(context, R.color.main500)
                        else -> ContextCompat.getColor(context, R.color.white)
                    }
                )
                this.setOnClickListener {
                    onClick(it)
                    if (autoDismiss) dismiss()
                }
            }
        }

        fun rightButton(
            text: String,
            _fillType: Int = FILL_FILL,
            autoDismiss: Boolean = true,
            onClick: (View) -> Unit = {}
        ) {
            rightButton = AppCompatButton(context).apply {
                this.text = text
                this.layoutParams =
                    LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                        leftMargin = 12.toDp
                        height = 48.toDp
                    }
                this.background = when (_fillType) {
                    FILL_FILL -> ContextCompat.getDrawable(
                        context,
                        R.drawable.jmt_button_background_main
                    )

                    FILL_OUTLINE -> ContextCompat.getDrawable(
                        context,
                        R.drawable.jmt_button_outline_background_main
                    )

                    else -> ContextCompat.getDrawable(
                        context,
                        R.drawable.jmt_button_background_main
                    )
                }
                this.setTextColor(
                    when (_fillType) {
                        FILL_FILL -> ContextCompat.getColor(context, R.color.white)
                        FILL_OUTLINE -> ContextCompat.getColor(context, R.color.main500)
                        else -> ContextCompat.getColor(context, R.color.white)
                    }
                )
                this.setOnClickListener {
                    onClick(it)
                    if (autoDismiss) dismiss()
                }
            }
        }
    }

    fun multiButton(addButton: MultiButtonBuilder.() -> Unit): JmtAlert {
        var buttons = MultiButtonBuilder().apply(addButton)
        LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, context.resources.getDimensionPixelSize(R.dimen.default_spacing), 0, 0)
            weightSum = 2f
            if (buttons.leftButton != null) {
                this.addView(buttons.leftButton)
            } else {
                this.addView(Space(context).apply {
                    layoutParams =
                        LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                })
            }
            buttons.rightButton?.let { rightButton ->
                this.addView(rightButton)
            }
        }.let {
            binding.dialogButtonLay.addView(it)
        }
        return this
    }
}