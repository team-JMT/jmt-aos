package org.gdsc.presentation.view.custom

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import org.gdsc.presentation.R

class FlexibleCornerImageView(context: Context, attrs: AttributeSet) :
    ShapeableImageView(context, attrs) {

    init {

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.FlexibleCornerImageView,
            0, 0
        ).apply {
            try {

                val allCornerRadius =
                    getDimension(R.styleable.FlexibleCornerImageView_all_corner_radius, 0f)

                if (allCornerRadius == 0f) {

                    val topLeftRadius =
                        getDimension(
                            R.styleable.FlexibleCornerImageView_top_left_corner_radius, 0f
                        )
                    val topRightRadius = getDimension(
                        R.styleable.FlexibleCornerImageView_top_right_corner_radius,
                        0f
                    )
                    val bottomLeftRadius = getDimension(
                        R.styleable.FlexibleCornerImageView_bottom_left_corner_radius,
                        0f
                    )
                    val bottomRightRadius = getDimension(
                        R.styleable.FlexibleCornerImageView_bottom_right_corner_radius,
                        0f
                    )

                    setCornerRounded(
                        listOf(
                            topLeftRadius,
                            topRightRadius,
                            bottomLeftRadius,
                            bottomRightRadius
                        )
                    )

                } else {
                    setCornerRounded(
                        List(4) { allCornerRadius }
                    )
                }
            } finally {
                recycle()
            }
        }
    }

    private fun setCornerRounded(corners: List<Float>) {
        this.shapeAppearanceModel = ShapeAppearanceModel.Builder()
            .setAllCornerSizes(0f)
            .setTopLeftCornerSize(corners[TOP_LEFT])
            .setTopRightCornerSize(corners[TOP_RIGHT])
            .setBottomLeftCornerSize(corners[BOTTOM_LEFT])
            .setBottomRightCornerSize(corners[BOTTOM_RIGHT])
            .build()
    }

    companion object {
        private const val TOP_LEFT = 0
        private const val TOP_RIGHT = 1
        private const val BOTTOM_LEFT = 2
        private const val BOTTOM_RIGHT = 3
    }

}