package com.elanciers.vasantham_stores_ecomm.Adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.elanciers.vasantham_stores_ecomm.DataClass.AreaResponse
import com.elanciers.vasantham_stores_ecomm.Database.CardHistoryData
import com.elanciers.vasantham_stores_ecomm.R
import java.lang.Exception

class AreaSpinnerAdapter(val activity: Activity, internal var items: ArrayList<AreaResponse>) :
    ArrayAdapter<AreaResponse>(activity,0, items) {
    private val mDepartmentsAll: ArrayList<AreaResponse>
    internal var inflater: LayoutInflater
    private var ListFiltered: ArrayList<AreaResponse>
    private var orig: ArrayList<AreaResponse>? = null
    init {
        mDepartmentsAll = items
        ListFiltered = items
        this.inflater = LayoutInflater.from(context)
    }

    override fun getView(position: Int, itemView: View?, parent: ViewGroup): View {
        var itemView = itemView
        try {
            val holder = ViewHolder()
            if (itemView == null) {
                itemView = inflater.inflate(R.layout.spinner_item2, null)
                itemView!!.tag = holder
            }

            holder.text = itemView.findViewById(android.R.id.text1) as TextView

            holder.text!!.setText(items[position].areaname)
        }catch (e:Exception){}
        return itemView!!
    }

    override fun getItem(position: Int): AreaResponse? {
        return items[position]
    }

    internal inner class ViewHolder {
        var text : TextView? = null
    }

    /*override fun getFilter(): Filter {
        return object : Filter() {
            override fun convertResultToString(resultValue: Any): String? {
                return (resultValue as AreaResponse).areaname
            }

            protected override fun performFiltering(constraint: CharSequence?): FilterResults? {
                val filterResults = FilterResults()
                val departmentsSuggestion: MutableList<AreaResponse> = ArrayList()
                if (constraint != null) {
                    if (constraint.isNotEmpty()) {
                        for (department in mDepartmentsAll) {
                            if (department.areaname!!.lowercase()
                                    .startsWith(constraint.toString().lowercase())
                            ) {
                                departmentsSuggestion.add(department)
                            }
                        }
                    }
                    filterResults.values = departmentsSuggestion
                    filterResults.count = departmentsSuggestion.size
                }
                return filterResults
            }

            protected override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                items.clear()
                if (results != null && results.count > 0) {
                    // avoids unchecked cast warning when using mDepartments.addAll((ArrayList<Department>) results.values);
                    *//*for (`object` in results.values) {
                        if (`object` is AreaResponse) {
                            items.add(`object` as AreaResponse)
                        }
                    }*//*
                    items = results.values as ArrayList<AreaResponse>
                    notifyDataSetChanged()
                } else if (constraint == null) {
                    // no filter, add entire original list back in
                    if (mDepartmentsAll.isNotEmpty()) {
                        items = mDepartmentsAll
                    }
                    notifyDataSetInvalidated()
                }
            }
        }
    }*/

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun convertResultToString(resultValue: Any): String? {
                return (resultValue as AreaResponse).areaname
            }
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val oReturn = FilterResults()
                val results: ArrayList<AreaResponse> = ArrayList<AreaResponse>()
                //results = searchList
                if (orig == null) orig = items
                if (constraint.isNotEmpty()) {
                    if (orig != null && orig!!.size > 0) {
                        for (g in orig!!) {
                            if (g.areaname!!.toString().lowercase()
                                    .contains(constraint.toString().lowercase())/* || g.pid!!
                                .contains(constraint.toString())*/
                            ) results.add(g)
                        }
                    }
                    oReturn.values = results
                }
                return oReturn
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                try {
                    items = results.values as ArrayList<AreaResponse>
                    notifyDataSetChanged()
                }catch (e:Exception){}
            }
        }
    }
}