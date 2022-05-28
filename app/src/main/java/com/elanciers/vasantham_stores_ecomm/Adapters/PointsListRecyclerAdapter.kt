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
import com.elanciers.vasantham_stores_ecomm.DataClass.LoyaltyList
import com.elanciers.vasantham_stores_ecomm.DataClass.Orders
import com.elanciers.vasantham_stores_ecomm.Database.CardHistoryData
import com.elanciers.vasantham_stores_ecomm.Database.CardHistoryDatabase
import com.elanciers.vasantham_stores_ecomm.R
import java.lang.Exception


class PointsListRecyclerAdapter(val activity : Activity, private var items: ArrayList<Orders>/*, private val listener: OnItemClickListener,*/ /*ps:Int, private var onBottomReachedListener : OnBottomReachedListener */) : RecyclerView.Adapter<PointsListRecyclerAdapter.DataObjectHolder>()
{

    interface OnItemClickListener {
        fun OnItemClick(view: View, position: Int, card: Orders)
    }

    interface OnBottomReachedListener {

        fun onBottomReached(position: Int)

    }

    /*fun OnBottomReachedListener(onBottomReachedListener: OnBottomReachedListener) {

        this.onBottomReachedListener = onBottomReachedListener
    }*/
    internal var resource: Int = 0
    inner class DataObjectHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        internal var date : TextView
        internal var point : TextView
        internal var amount : TextView
        internal var branch : TextView
        init {

            date = itemView.findViewById(R.id.date) as TextView
            point = itemView.findViewById(R.id.point) as TextView
            amount = itemView.findViewById(R.id.amount) as TextView
            branch = itemView.findViewById(R.id.branch) as TextView

            /*opt.setOnClickListener {
                lastSelectedPosition = getAdapterPosition();
                notifyDataSetChanged();
            }*/
            itemView.setOnClickListener(this)

        }

        override fun onClick(v: View) {
            //try {
                //listener.OnItemClick(v, adapterPosition, items[position])
            /*} catch (e: Exception) {

            }*/

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DataObjectHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.point_list_items, parent, false)

        return DataObjectHolder(view)
    }

    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {
        holder.date.setText(items[position].date)
        holder.amount.setText(items[position].billAmount)
        holder.point.setText(items[position].pointsEarned)
        holder.branch.setText(items[position].branch)
    }


    override fun getItemCount(): Int {
        return items.size
    }

    companion object {
        internal val ITEM_CONTENT_VIEW_TYPE = 1
    }



}
