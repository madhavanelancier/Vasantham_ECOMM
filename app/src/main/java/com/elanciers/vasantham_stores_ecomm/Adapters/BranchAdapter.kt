package com.elanciers.vasantham_stores_ecomm.Adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
        internal var ph : TextView

        init {

            //card = itemView.findViewById(R.id.card) as CardView
            title = itemView.findViewById(R.id.brnm) as TextView
            location = itemView.findViewById(R.id.bradr) as TextView
            ph = itemView.findViewById(R.id.ph) as TextView


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
        holder.ph.setText(items[position].phone)
        holder.ph.setOnClickListener {
            val u = Uri.parse("tel:" + items[position].phone.toString())
            // Create the intent and set the data for the
            // intent as the phone number.
            val i = Intent(Intent.ACTION_DIAL, u)

            try {
                // Launch the Phone app's dialer with a phone
                // number to dial a call.
                holder.ph.context.startActivity(i)
            } catch (s: SecurityException) {
                // show() method display the toast with
                // exception message.
                Toast.makeText(holder.ph.context, "An error occurred", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }


    override fun getItemCount(): Int {
        return items.size
    }

    companion object {
        internal val ITEM_CONTENT_VIEW_TYPE = 1
    }

}
