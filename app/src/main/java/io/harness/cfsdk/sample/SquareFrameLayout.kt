package io.harness.cfsdk.sample

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.google.android.material.card.MaterialCardView


class SquareFrameLayout(context: Context, attrs: AttributeSet) : MaterialCardView(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = measuredWidth
        setMeasuredDimension(width, width)
    }
}