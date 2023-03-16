package com.dogdduddy.jmt.utils

import android.os.Build
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment

val Fragment.deviceMetrics
    get() = run {

        val displayMetrics = DisplayMetrics()

        // target version above 30
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            val windowManager = requireActivity().windowManager

            // target version above 31
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

                val metrics = windowManager.currentWindowMetrics
                metrics.bounds.also { bounds ->
                    displayMetrics.widthPixels = bounds.width()
                    displayMetrics.heightPixels = bounds.height()
                }
            } else {
                val display = requireActivity().display
                display?.getRealMetrics(displayMetrics)
            }

        } else {
            requireActivity().windowManager.defaultDisplay?.getMetrics(displayMetrics)
        }
        displayMetrics
    }