package org.gdsc.jmt.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.gdsc.jmt.R
import org.gdsc.jmt.databinding.JmtConfirmDialogBinding
import org.gdsc.jmt.utils.deviceMetrics

class JmtConfirmDialog(private val context: Context) : DialogFragment() {

    private var _binding: JmtConfirmDialogBinding? = null
    private val binding
        get() = _binding ?: run {
            _binding = LayoutInflater.from(context).let {
                JmtConfirmDialogBinding.inflate(it)
            }
            _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setBackgroundDrawableResource(R.drawable.jmt_confirm_dialog_background)
        isCancelable = false

        binding.confirmButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()

        val displayMetrics = deviceMetrics

        val displayWidth = displayMetrics.widthPixels
        val displayHeight = displayMetrics.heightPixels

        val dialogWindowWidth = (displayWidth * 0.9f).toInt()
        val dialogWindowHeight = (displayHeight * 0.25f).toInt()

        dialog?.window?.setLayout(dialogWindowWidth, dialogWindowHeight)
    }

    fun setTitle(title: String): JmtConfirmDialog {
        binding.titleText.text = title
        return this
    }

    fun setContent(content: String): JmtConfirmDialog {
        binding.contentText.text = content
        return this
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "JmtConfirmDialog"
    }
}