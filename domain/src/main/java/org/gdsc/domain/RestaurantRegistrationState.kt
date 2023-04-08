package org.gdsc.domain

enum class RestaurantRegistrationState(val code: Int, val codeMessage: String) {
    // 위치 정보만 존재 -> 등록 가능
    LOCATION_EXIST(200, "RECOMMEND_RESTAURANT_REGISTERABLE"),

    // 존재 X -> 등록 가능
    NO_EXIST(404, "RESTAURANT_NOT_FOUND"),

    // 위치 정보 및 사용자가 등록한 정보 존재 -> 등록 불가
    ALL_EXIST(409, "RESTAURANT_LOCATION_CONFLICT"),
}
