package org.gdsc.presentation.view

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast

/** Instantiate the interface and set the context  */
class WebAppInterface(
    private val mContext: Context,
    private val slideUpBottomNavigationView: () -> Unit = {},
    private val slideDownBottomNavigationView: () -> Unit = {},
    private val navigateToRestaurantEdit: (Int) -> Unit = {}
) {

    /** Show a toast from the web page  */
    @JavascriptInterface
    fun showToast(toast: String) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun setVisibleNavigationAndToolBar(isVisible: Boolean) {
        if (isVisible) slideUpBottomNavigationView()
        else slideDownBottomNavigationView()
    }

    @JavascriptInterface
    fun editRestaurantInfo(restaurantId: Int) {
        navigateToRestaurantEdit(restaurantId)
    }

}