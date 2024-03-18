package org.gdsc.presentation.view.custom

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.internal.managers.FragmentComponentManager
import org.gdsc.presentation.R

class BottomSheetDialog {

    private var dismissAfterClick = true
    private var btmDlg: BottomSheetDialog
    var customView: View? = null

    var style: Int = R.style.JmtBottomSheetStyle
    private var context: Context


    constructor(context: Context) {
        this.context = context
        btmDlg = BottomSheetDialog(context, style).apply {
            init()
        }
    }

    fun BottomSheetDialog.init() {
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
        behavior.setPeekHeight(0, false)
        behavior.maxHeight = getBottomSheetDialogDefaultHeight()
    }

    val isShowing: Boolean
        get() {
            return this.btmDlg.isShowing
        }

    fun show() {
        if (isShowing) return
        this.btmDlg.show()
    }

    fun dismiss() {
        if (!isShowing) return
        this.btmDlg.dismiss()
    }

    fun <T : ViewBinding> bindBuilder(
        binding: T,
        callback: T.(BottomSheetDialog) -> Unit
    ): org.gdsc.presentation.view.custom.BottomSheetDialog {
        this.customView = binding.root
        btmDlg.setContentView(this.customView!!)
        binding.callback(btmDlg)
        return this
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getScreenRealHeight() * 70 / 100
    }

    fun setCustomViewHeightMax() {
        val height = getScreenRealHeight()

        btmDlg.behavior.maxHeight = height
        this.customView?.layoutParams?.height = height
    }

    private fun getScreenRealHeight(): Int {
        val activity = FragmentComponentManager.findActivity(context) as Activity
        return activity
            .window.decorView.findViewById<ViewGroup>(android.R.id.content)
            .getChildAt(0).height
    }
}