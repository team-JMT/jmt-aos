package org.gdsc.presentation.base

interface Const {

    interface SIZE {
        companion object{
            const val B = 1L
            const val KB = 1024.0
            const val MB = 1048576.0
            const val GB = 1073741824.0
        }
    }

    interface DISTANCE {
        companion object {
            const val METER = 1
            const val KILOMETER = 1000
        }
    }
}