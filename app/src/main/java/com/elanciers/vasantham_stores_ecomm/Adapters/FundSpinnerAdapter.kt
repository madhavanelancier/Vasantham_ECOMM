package com.elanciers.vasantham_stores_ecomm.Adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.elanciers.vasantham_stores_ecomm.DataClass.Chit
import com.elanciers.vasantham_stores_ecomm.DataClass.Fund1
import com.elanciers.vasantham_stores_ecomm.DataClass.YearsResponse
import com.elanciers.vasantham_stores_ecomm.R

class FundSpinnerAdapter(val activity: Activity, internal var items: ArrayList<Fund1>) :
    ArrayAdapter<Fund1>(activity,0, items) {

    internal var inflater: LayoutInflater

    init {
        this.inflater = LayoutInflater.from(context)
    }

    override fun getView(position: Int, itemView: View?, parent: ViewGroup): View {
        var itemView = itemView
        val holder = ViewHolder()
        if (itemView == null) {
            itemView = inflater.inflate(R.layout.spinner_item2, null)
            itemView!!.tag = holder
        }

        holder.text = itemView.findViewById(android.R.id.text1) as TextView

        holder.text!!.setText(items[position].fundName)

        return itemView
    }

    internal inner class ViewHolder {
        var text : TextView? = null
    }
}