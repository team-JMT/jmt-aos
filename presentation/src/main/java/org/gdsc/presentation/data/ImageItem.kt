package com.example.customimagepicker.data

import android.net.Uri

data class ImageItem(
    var uri: Uri,
    var isChecked: Boolean
)