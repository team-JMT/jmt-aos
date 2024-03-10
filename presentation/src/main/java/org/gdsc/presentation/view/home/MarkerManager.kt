package org.gdsc.presentation.view.home

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import org.gdsc.domain.FoodCategory
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.presentation.model.FoodCategoryItem


class MarkerManager {
    private var naverMap: NaverMap? = null
    private var dataList: MutableList<RegisteredRestaurant> = mutableListOf() // 여기에 페이징된 데이터를 저장
    private var markers: MutableList<Marker> = mutableListOf() // 마커를 저장할 리스트

    constructor(naverMap: NaverMap) {
        this.naverMap = naverMap
        dataList = ArrayList<RegisteredRestaurant>()
    }

    fun updateDataList(newDataList: List<RegisteredRestaurant>) {
        val dataSet = dataList.toSet()
        val newDataSet = newDataList.toSet()

        dataSet.subtract(newDataSet).let {
            it.forEach { removeData ->
                dataList.indexOf(removeData).let { index ->
                    if (index != -1) {
                        markers[index].map = null
                        markers.removeAt(index)
                        dataList.removeAt(index)
                    }
                }
            }
        }

        newDataSet.subtract(dataSet).let {
            it.forEach { insertData ->
                dataList.add(insertData)
                markers.add(
                    Marker().apply {
                        position = LatLng(insertData.y, insertData.x)
                        icon = OverlayImage.fromResource(
                            FoodCategoryItem(
                                FoodCategory.fromName(insertData.category)
                            ).getMarkerIcon()
                        )
                        map = naverMap
                    }
                )
            }
        }
    }
}