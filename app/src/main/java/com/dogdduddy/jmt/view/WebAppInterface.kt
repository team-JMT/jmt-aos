package com.dogdduddy.jmt.view

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast

/** Instantiate the interface and set the context  */
class WebAppInterface(private val mContext: Context) {

    /** Show a toast from the web page  */
    @JavascriptInterface
    fun showToast(toast: String) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
    }
}