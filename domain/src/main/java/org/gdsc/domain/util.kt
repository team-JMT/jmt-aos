package org.gdsc.domain

fun String.toMeterFormat(): String {
    return if (this.length > 3) {
        val km = this.substring(0, this.length - 3)
        val m = this.substring(this.length - 3, this.length)
        "$km.$m km"
    } else {
        "$this m"
    }
}

val String.Companion.Empty
    inline get() = ""