package com.elanciers.vasantham_stores_ecomm.Common

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View


open class MySpannable
/**
 * Constructor
 */
    (isUnderline: Boolean) : ClickableSpan() {

    private var isUnderline = true

    init {
        this.isUnderline = isUnderline
    }

    override fun updateDrawState(ds: TextPaint) {
        ds.isUnderlineText = isUnderline
        ds.color = Color.parseColor("#1b76d3")
    }

    override fun onClick(widget: View) {


    }
}