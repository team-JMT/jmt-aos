package org.gdsc.presentation.view

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast
import org.gdsc.presentation.model.Route
import org.json.JSONObject

/** Instantiate the interface and set the context  */
class WebAppInterface(
    private val mContext: Context,
    private val slideUpBottomNavigationView: () -> Unit = {},
    private val slideDownBottomNavigationView: () -> Unit = {},
    private val navigateToRestaurantEdit: (Int) -> Unit = {},
    private val setAccessToken: () -> Unit = {},
) {

    /** Show a toast from the web page  */

    @JavascriptInterface
    fun navigation(data: String) {
        val isVisible:Boolean = JSONObject(data).get("isVisible") as Boolean

        if (isVisible) slideUpBottomNavigationView()
        else slideDownBottomNavigationView()
    }

    @JavascriptInterface
    fun token() = setAccessToken()

    @JavascriptInterface
    fun share() {
    }

    @JavascriptInterface
    fun navigate(data: String) {
        when(JSONObject(data).get("route") as String) {
            Route.EDIT_RESTAURANT.route-> {
                val restaurantId = JSONObject(data).get("restaurantId") as Int
                navigateToRestaurantEdit(restaurantId)
            }
        }
    }

    // webView.canGoBack으로 뒤로가기 처리 완료해서 비워뒀습니다.
    @JavascriptInterface
    fun back(isEnableBack: Boolean) {
    }
}
