package com.elancier.vasantham_stores.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands
import com.elanciers.vasantham_stores_ecomm.Common.Utils
import com.elanciers.vasantham_stores_ecomm.Payment_Reciept

import com.elanciers.vasantham_stores_ecomm.R
import com.elanciers.vasantham_stores_ecomm.Rewardpointsbo
import com.google.android.material.floatingactionbutton.FloatingActionButton


class Due_list(private val mRecyclerViewItems2: List<Any>, private val context: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

     var utils=Utils(context);

    interface OnItemClickListener {
        fun OnItemClick(view: View, position: Int, viewType: Int)
    }


    override fun onCreateViewHolder(viewGroup:ViewGroup,viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.due_list_items, viewGroup, false)
                return ItemViewHolder(productView)
                utils=Utils(context)
            }
            else -> {
                val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.due_list_items, viewGroup, false)
                utils=Utils(context)

                return ItemViewHolder(productView)
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val itemViewHolder = holder as ItemViewHolder
                val item = mRecyclerViewItems2[position] as Rewardpointsbo

                holder.username.text = "Card_No :"+item.username
                holder.date.text = "Paid \n"+ Appconstands.rupees+item.date
                holder.name.text = item.id

                if(item.id=="10"){
                    holder.name.setText("10")
                }
                else if(item.id=="12"){
                    holder.name.setText("12")

                }


                if(item.visual_time=="false"){
                    holder.fab1.visibility=View.GONE
                    println("inside adap_false"+item.visual_time)

                    // holder.fab.setBackground(context.resources.getDrawable(R.drawable.ic_tick))
                    //holder.fab.setBackgroundColor(context.resources.getColor(R.color.white))
                    holder.username.setTextColor(context.resources.getColor(R.color.black))
                    holder.date.setTextColor(context.resources.getColor(R.color.black))
                    holder.name.setTextColor(context.resources.getColor(R.color.black))
                    holder.remark.setBackgroundDrawable(context.resources.getDrawable(R.drawable.drawab_corn))
                }
                else if(item.visual_time=="true"){
                    holder.fab1.visibility=View.VISIBLE
                    holder.remark.setBackgroundResource(R.drawable.round_button)
                    println("inside adap"+item.visual_time)
                    // holder.fab.setBackground(context.resources.getDrawable(R.drawable.ic_tick))
                    //holder.fab.setBackgroundColor(context.resources.getColor(R.color.white))
                    holder.username.setTextColor(context.resources.getColor(R.color.white))
                    holder.date.setTextColor(context.resources.getColor(R.color.white))
                    holder.name.setTextColor(context.resources.getColor(R.color.white))
                }

                holder.remark.setOnClickListener {
                    context.startActivity(
                        Intent(context, Payment_Reciept::class.java)
                        .putExtra("name",utils.name_due())
                        .putExtra("mobile",utils.mobile_due())
                        .putExtra("card",item.username)
                        .putExtra("amout",item.date)
                        .putExtra("rec",item.whome)
                        .putExtra("due",item.id)
                        .putExtra("date",item.name))

                    //(context as Dashboard).bg(position)

                }



            }

            // fall through
            else -> {
                val itemViewHolder = holder as ItemViewHolder
                val item = mRecyclerViewItems2[position] as Rewardpointsbo

                holder.username.text = item.date
                holder.date.text = item.username
                holder.name.visibility=View.GONE


            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        val recyclerViewItem = mRecyclerViewItems2[position]
        /*if (recyclerViewItem instanceof Confirmmodel) {
            return ITEM_CONFIRM_VIEW_TYPE;
        }*/
        return ITEM_CONTENT_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return mRecyclerViewItems2.size
    }

    inner class ItemViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        internal var username: TextView
        internal var date: TextView
        internal var name: TextView
        internal lateinit var remark: LinearLayout
        internal lateinit var amount: TextView
        internal lateinit var fab: FloatingActionButton
        internal lateinit var fab1: FloatingActionButton
        //internal var dt: TextView


        init {

            username = itemView.findViewById<View>(R.id.textView14) as TextView
            date = itemView.findViewById<View>(R.id.textView16) as TextView
            name = itemView.findViewById<View>(R.id.textView15) as TextView
            remark = itemView.findViewById<View>(R.id.card) as LinearLayout
            fab = itemView.findViewById<View>(R.id.imageProfile) as FloatingActionButton
            fab1 = itemView.findViewById<View>(R.id.imageProfile1) as FloatingActionButton



            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            try {
                listener.OnItemClick(view, adapterPosition, ITEM_CONTENT_VIEW_TYPE)
            } catch (e: Exception) {

            }

        }
    }

    companion object {
        internal val ITEM_CONTENT_VIEW_TYPE = 1
    }
}