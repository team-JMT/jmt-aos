package org.gdsc.presentation.view

import android.content.Context
import android.webkit.JavascriptInterface
import org.json.JSONObject

const val WEB_BASE_URL = "https://jmt-frontend-ad7b8.web.app/"

sealed class WebViewReceivedData {
    data class Navigation(
        val isVisible: Boolean
    ): WebViewReceivedData()

    data class Back(
        val enable: Boolean
    ): WebViewReceivedData()
}
sealed class WebViewBrideObject {
    data class Token(val data: Any? = null): WebViewBrideObject()
    data class Other(val data: WebViewReceivedData): WebViewBrideObject()

}

fun JSONObject.toWebViewBrideObject(): WebViewBrideObject {
    return when(this.get("name")) {
        "token" -> {
            WebViewBrideObject.Token()
        }
        "navigation" -> {
            val isVisible = JSONObject(this.get("data").toString()).get("isVisible") as Boolean
            WebViewBrideObject.Other(WebViewReceivedData.Navigation(isVisible))
        }
        // back
        else -> {
            val enable = JSONObject(this.get("data").toString()).get("enable") as Boolean
            WebViewBrideObject.Other(WebViewReceivedData.Back(enable))
        }
    }
}

class WebAppInterface(
    private val mContext: Context,
    private val slideUpBottomNavigationView: () -> Unit = {},
    private val slideDownBottomNavigationView: () -> Unit = {},
    private val navigateToRestaurantEdit: (Int) -> Unit = {},
    private val setAccessToken: () -> Unit = {},
    private val setUserPosition: () -> Unit = {},
) {

    @JavascriptInterface
    fun userPosition() = setUserPosition()

    @JavascriptInterface
    fun navigation(data: String) {
        val isVisible:Boolean = JSONObject(data).get("isVisible") as Boolean

        if (isVisible) slideUpBottomNavigationView()
        else slideDownBottomNavigationView()
    }

//    @JavascriptInterface
//    fun token() = setAccessToken()

    // 딥링크 생성 필요
    @JavascriptInterface
    fun share() {
    }

    // 데이터 구조는 다시 상의 후에 결정해서, 객체화 시키면 좋을 것으로 보임
//    @JavascriptInterface
//    fun navigate(data: String) {
//        val result = JSONObject(data)
//
//        when(result.getString("route")) {
//            Route.EDIT_RESTAURANT.route-> {
//                val restaurantId = result.getString("restaurantId").toInt()
//                navigateToRestaurantEdit(restaurantId)
//            }
//        }
//    }

    @JavascriptInterface
    fun sendData(data: String) {
        when (val result = JSONObject(data).toWebViewBrideObject()) {
            is WebViewBrideObject.Token -> {
                setAccessToken()
            }
            is WebViewBrideObject.Other -> {
                when (val receivedData = result.data) {
                    is WebViewReceivedData.Navigation -> {
                        if (receivedData.isVisible) {
                            slideUpBottomNavigationView()
                        } else {
                            slideDownBottomNavigationView()
                        }
                    }
                    is WebViewReceivedData.Back -> {
                        if (receivedData.enable) {

                        } else {

                        }
                    }
                }
            }
        }
    }

    // webView.canGoBack으로 뒤로가기 처리 완료해서 비워뒀습니다.
//    @JavascriptInterface
//    fun back(isEnableBack: Boolean) {
//    }
}
