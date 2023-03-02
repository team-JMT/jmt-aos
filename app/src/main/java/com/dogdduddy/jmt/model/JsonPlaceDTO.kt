package com.dogdduddy.jmt.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class JsonPlaceDTO(
    @Expose
    @SerializedName("token")
    var token: String
)
