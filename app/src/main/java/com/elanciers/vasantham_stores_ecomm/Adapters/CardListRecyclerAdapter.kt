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
import com.elanciers.vasantham_stores_ecomm.DataClass.CardList
import com.elanciers.vasantham_stores_ecomm.DataClass.CardsResponse
import com.elanciers.vasantham_stores_ecomm.Database.CardHistoryData
import com.elanciers.vasantham_stores_ecomm.Database.CardHistoryDatabase
import com.elanciers.vasantham_stores_ecomm.R
import java.lang.Exception


class CardListRecyclerAdapter(val activity : Activity, private var items: ArrayList<CardList>, private val listener: OnItemClickListener, /*ps:Int, private var onBottomReachedListener : OnBottomReachedListener */) : RecyclerView.Adapter<CardListRecyclerAdapter.DataObjectHolder>(),
    Filterable
{
    private var ListFiltered: ArrayList<CardList>?
    init {
        ListFiltered = items
    }
    private var orig: ArrayList<CardList>? = null
    interface OnItemClickListener {
        fun OnItemClick(view: View, position: Int, card: CardList)
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
        internal var year : TextView
        internal var brancharea : TextView
        internal var cardno : TextView
        internal var amount : TextView
        init {

            name = itemView.findViewById(R.id.name) as TextView
            year = itemView.findViewById(R.id.year) as TextView
            brancharea = itemView.findViewById(R.id.brancharea) as TextView
            cardno = itemView.findViewById(R.id.cardno) as TextView
            amount = itemView.findViewById(R.id.amount) as TextView

            /*opt.setOnClickListener {
                lastSelectedPosition = getAdapterPosition();
                notifyDataSetChanged();
            }*/
            itemView.setOnClickListener(this)

        }

        override fun onClick(v: View) {
            //try {
                listener.OnItemClick(v, adapterPosition, items[position])
            /*} catch (e: Exception) {

            }*/

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DataObjectHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_list_items, parent, false)

        return DataObjectHolder(view)
    }

    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {
        holder.name.setText(items[position].name+" , "+items[position].phone)
        holder.year.setText(items[position].year)
        holder.amount.setText(items[position].paidamt)
        holder.cardno.setText("Card : "+items[position].cardNo)
        holder.brancharea.setText("Branch : "+items[position].branch+" , Area : "+items[position].area)
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
                val results: ArrayList<CardList?> = ArrayList<CardList?>()
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
                ListFiltered = results.values as ArrayList<CardList>?
                notifyDataSetChanged()
            }
        }
    }

}
