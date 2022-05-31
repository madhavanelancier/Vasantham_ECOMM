package com.elanciers.vasantham_stores_ecomm.Adapters

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elanciers.vasantham_stores_ecomm.DataClass.BranchList
import com.elanciers.vasantham_stores_ecomm.DataClass.StoreItem
import com.elanciers.vasantham_stores_ecomm.R


class BranchAdapter(public val context: Context, private val items: ArrayList<BranchList>/*, private val listener: OnItemClickListener, private var onBottomReachedListener : OnBottomReachedListener*/ ) : RecyclerView.Adapter<BranchAdapter.DataObjectHolder>()
{

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
        ///internal var card : CardView
        internal var title : TextView
        internal var location : TextView

        init {

            //card = itemView.findViewById(R.id.card) as CardView
            title = itemView.findViewById(R.id.brnm) as TextView
            location = itemView.findViewById(R.id.bradr) as TextView


            itemView.setOnClickListener(this)

        }

        override fun onClick(v: View) {
            /*try {
                listener.OnItemClick(v, adapterPosition, ITEM_CONTENT_VIEW_TYPE)

            } catch (e: Exception) {

            }*/

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DataObjectHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.branch_item, parent, false)

        return DataObjectHolder(view)
    }

    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {

        holder.title.setText(items[position].branch)
        holder.location.setText(Html.fromHtml(items[position].address))

    }


    override fun getItemCount(): Int {
        return items.size
    }

    companion object {
        internal val ITEM_CONTENT_VIEW_TYPE = 1
    }

}
