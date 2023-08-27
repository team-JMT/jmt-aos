package org.gdsc.presentation.utils

import org.gdsc.presentation.base.Const
import kotlin.math.floor

class CalculatorUtils {

    companion object {
        fun getDistanceConvert(byte: Int, type: Int): Int {
            return byte / type
        }

        fun getDistanceWithLength(byte: Int): String {
            return if (byte >= Const.DISTANCE.KILOMETER) {
                "${getDistanceConvert(byte, 1000)}km"
            } else {
                "${getDistanceConvert(byte, 1)}m"
            }
        }
    }
}