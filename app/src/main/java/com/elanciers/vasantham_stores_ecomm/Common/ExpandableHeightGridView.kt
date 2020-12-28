package com.elanciers.vasantham_stores_ecomm.Common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.GridView

class ExpandableHeightGridView : GridView {

    internal var isExpanded = false
        set

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(
        context: Context, attrs: AttributeSet,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
    }

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // HACK! TAKE THAT ANDROID!
        if (isExpanded) {
            // Calculate entire height by providing a very large height hint.
            // But do not use the highest 2 bits of this integer; those are
            // reserved for the MeasureSpec mode.
            val expandSpec = View.MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST
            )
            super.onMeasure(widthMeasureSpec, expandSpec)

            val params = layoutParams
            params.height = measuredHeight
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}
