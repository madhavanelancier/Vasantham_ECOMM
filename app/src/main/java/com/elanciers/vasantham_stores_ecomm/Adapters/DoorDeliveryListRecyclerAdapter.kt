package com.elanciers.vasantham_stores_ecomm.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.elanciers.booking.DataClass.SpinnerPojo
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands
import com.elanciers.vasantham_stores_ecomm.DataClass.CardList
import com.elanciers.vasantham_stores_ecomm.DataClass.CardsResponse
import com.elanciers.vasantham_stores_ecomm.DataClass.DoorDeliveryList
import com.elanciers.vasantham_stores_ecomm.Database.CardHistoryData
import com.elanciers.vasantham_stores_ecomm.Database.CardHistoryDatabase
import com.elanciers.vasantham_stores_ecomm.LocationActivity
import com.elanciers.vasantham_stores_ecomm.R
import java.lang.Exception


class DoorDeliveryListRecyclerAdapter(val activity : Activity, private var items: ArrayList<DoorDeliveryList>/*, private val listener: OnItemClickListener,*/ /*ps:Int, private var onBottomReachedListener : OnBottomReachedListener */) : RecyclerView.Adapter<DoorDeliveryListRecyclerAdapter.DataObjectHolder>(),
    Filterable
{
    private var ListFiltered: ArrayList<DoorDeliveryList>?
    init {
        ListFiltered = items
    }
    private var orig: ArrayList<DoorDeliveryList>? = null
    interface OnItemClickListener {
        fun OnItemClick(view: View, position: Int, viewType: Int)
    }

    interface OnBottomReachedListener {

        fun onBottomReached(position: Int)

    }

    /*fun OnBottomReachedListener(onBottomReachedListener: OnBottomReachedListener) {

        this.onBottomReachedListener = onBottomReachedListener
    }*/
    internal var resource: Int = 0
    inner class DataObjectHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        internal var name : TextView
        internal var date : TextView
        internal var mobileno : TextView
        internal var adrs : TextView
        internal var area : TextView
        init {

            name = itemView.findViewById(R.id.name) as TextView
            date = itemView.findViewById(R.id.date) as TextView
            mobileno = itemView.findViewById(R.id.mobileno) as TextView
            adrs = itemView.findViewById(R.id.adrs) as TextView
            area = itemView.findViewById(R.id.area) as TextView

            /*opt.setOnClickListener {
                lastSelectedPosition = getAdapterPosition();
                notifyDataSetChanged();
            }*/
            itemView.setOnClickListener(this)

        }

        override fun onClick(v: View) {
            val st = Intent(activity, LocationActivity::class.java)
            /*st.putExtra("lat",lat)
            st.putExtra("lng",lng)*/
            name.context.startActivity(st)
            //try {
                //listener.OnItemClick(v, adapterPosition, ITEM_CONTENT_VIEW_TYPE)
            /*} catch (e: Exception) {

            }*/

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DataObjectHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.delivery_list_items, parent, false)

        return DataObjectHolder(view)
    }

    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {
        holder.name.setText(items[position].name)
        holder.mobileno.setText(items[position].phone+", "+items[position].aphone)
        holder.adrs.setText(items[position].address+","+items[position].landmark)
        holder.area.setText("Area : "+items[position].area)
    }


    override fun getItemCount(): Int {
        return ListFiltered!!.size
    }

    companion object {
        internal val ITEM_CONTENT_VIEW_TYPE = 1
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val oReturn = FilterResults()
                val results: ArrayList<DoorDeliveryList?> = ArrayList<DoorDeliveryList?>()
                //results = searchList
                if (orig == null) orig = ListFiltered
                if (constraint != null) {
                    if (orig != null && orig!!.size > 0) {
                        for (g in orig!!) {
                            if (g!!.name!!.toString()
                                    .contains(constraint.toString())/* || g.pid!!
                                    .contains(constraint.toString())*/
                            ) results.add(g)
                        }
                    }
                    oReturn.values = results
                }
                return oReturn
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                ListFiltered = results.values as ArrayList<DoorDeliveryList>?
                notifyDataSetChanged()
            }
        }
    }

}
