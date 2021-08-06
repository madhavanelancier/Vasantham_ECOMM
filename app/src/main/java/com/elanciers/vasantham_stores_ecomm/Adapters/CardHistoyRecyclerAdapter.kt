package com.elanciers.vasantham_stores_ecomm.Adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.elanciers.booking.DataClass.SpinnerPojo
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands
import com.elanciers.vasantham_stores_ecomm.Database.CardHistoryData
import com.elanciers.vasantham_stores_ecomm.Database.CardHistoryDatabase
import com.elanciers.vasantham_stores_ecomm.R
import java.lang.Exception


class CardHistoyRecyclerAdapter(val activity : Activity, private var items: ArrayList<CardHistoryData?>, private val listener: OnItemClickListener, /*ps:Int, private var onBottomReachedListener : OnBottomReachedListener */) : RecyclerView.Adapter<CardHistoyRecyclerAdapter.DataObjectHolder>(),
    Filterable
{
    private var ListFiltered: ArrayList<CardHistoryData?>?
    init {
        ListFiltered = items
    }
    private var orig: ArrayList<CardHistoryData?>? = null
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

        internal var spinner_text : TextView
        internal var imageView34 : ImageView
        init {

            spinner_text = itemView.findViewById(R.id.spinner_text) as TextView
            imageView34 = itemView.findViewById(R.id.imageView34) as ImageView

            /*opt.setOnClickListener {
                lastSelectedPosition = getAdapterPosition();
                notifyDataSetChanged();
            }*/
            spinner_text.setOnClickListener(this)

        }

        override fun onClick(v: View) {
            //try {
                listener.OnItemClick(v, adapterPosition, ITEM_CONTENT_VIEW_TYPE)
            /*} catch (e: Exception) {

            }*/

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DataObjectHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_history_list_item, parent, false)

        return DataObjectHolder(view)
    }

    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {
        holder.spinner_text!!.setText(ListFiltered!![position]!!.username)

        holder.imageView34.setOnClickListener {
            CardHistoryDatabase.getDatabase(activity)!!.cardDao()!!.deleteCart(ListFiltered!![position]!!)
            ListFiltered!!.removeAt(position)
            try{
            orig!!.removeAt(position)}catch (e:Exception){}
            notifyDataSetChanged()
        }
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
                val results: ArrayList<CardHistoryData?> = ArrayList<CardHistoryData?>()
                //results = searchList
                if (orig == null) orig = ListFiltered
                if (constraint != null) {
                    if (orig != null && orig!!.size > 0) {
                        for (g in orig!!) {
                            if (g!!.username!!.toString()
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
                ListFiltered = results.values as ArrayList<CardHistoryData?>?
                notifyDataSetChanged()
            }
        }
    }

}
