package org.gdsc.presentation.view

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast
import org.gdsc.presentation.model.Route
import org.json.JSONObject

const val WEB_BASE_URL = "https://jmt-matzip.dev/"

class WebAppInterface(
    private val mContext: Context,
    private val slideUpBottomNavigationView: () -> Unit = {},
    private val slideDownBottomNavigationView: () -> Unit = {},
    private val navigateToRestaurantEdit: (Int) -> Unit = {},
    private val setAccessToken: () -> Unit = {},
) {

    @JavascriptInterface
    fun navigation(data: String) {
        val isVisible:Boolean = JSONObject(data).get("isVisible") as Boolean

        if (isVisible) slideUpBottomNavigationView()
        else slideDownBottomNavigationView()
    }

    @JavascriptInterface
    fun token() = setAccessToken()

    // 딥링크 생성 필요
    @JavascriptInterface
    fun share() {
    }

    // 데이터 구조는 다시 상의 후에 결정해서, 객체화 시키면 좋을 것으로 보임
    @JavascriptInterface
    fun navigate(data: String) {
        val result = JSONObject(data)

        when(result.getString("route")) {
            Route.EDIT_RESTAURANT.route-> {
                val restaurantId = result.getString("restaurantId").toInt()
                navigateToRestaurantEdit(restaurantId)
            }
        }
    }

    // webView.canGoBack으로 뒤로가기 처리 완료해서 비워뒀습니다.
    @JavascriptInterface
    fun back(isEnableBack: Boolean) {
    }
}
