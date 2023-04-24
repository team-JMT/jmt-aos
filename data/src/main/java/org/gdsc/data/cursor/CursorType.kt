package org.gdsc.data.cursor

enum class CursorType {
    IMAGE {
        override val cursorFactory: CursorFactory
            get() = ImageCursorFactory()
    }
    ,
    VIDEO {
        override val cursorFactory: CursorFactory
            get() = VideoCursorFactory()
    },
    IMAGE_AND_VIDEO {
        override val cursorFactory: CursorFactory
            get() = ImageVideoCursorFactory()
    };

    abstract val cursorFactory:CursorFactory
}