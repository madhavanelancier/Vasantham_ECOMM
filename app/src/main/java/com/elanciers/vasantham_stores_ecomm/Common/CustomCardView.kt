package com.elanciers.vasantham_stores_ecomm.Common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.elanciers.vasantham_stores_ecomm.R

class CustomCardView(context: Context,listener: onSliderClick?) : BaseSliderView(context) {
    val listeners = listener
    interface onSliderClick {
        fun OnSliderClick(view: View)
    }

    override fun getView(): View {
        val v = LayoutInflater.from(context).inflate(R.layout.image_scroll_layout, null)
        val target = v.findViewById<View>(R.id.img) as ImageView
        val description = v.findViewById<View>(R.id.cont) as TextView
        description.text = getDescription()
        //Picasso.with(v).load();
        //Glide.with(v).load(url)
        bindEventAndShow(v, target)
        if (listeners!=null){
            target.setOnClickListener {
                listeners.OnSliderClick(v)
            }
        }
        return v
    }

}


