package com.elanciers.vasantham_stores_ecomm.Adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elanciers.vasantham_stores_ecomm.R
import android.graphics.drawable.GradientDrawable
import android.graphics.Color
import android.text.Html
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import com.elanciers.vasantham_stores_ecomm.DataClass.Coupons
import org.json.JSONArray
import kotlin.random.Random.Default.nextInt


class PaymentsListAdapter(public val mActivity: Activity, public val mValues: JSONArray/*, private val listener: OnItemClickListener, private var onBottomReachedListener : OnBottomReachedListener */) : RecyclerView.Adapter<PaymentsListAdapter.DataObjectHolder>()
{
    val months = arrayListOf<String>("Oct","Nov","Dec","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep")
    interface OnItemClickListener {
        fun OnItemClick(view: View, position: Int, viewType: Int)
    }

    interface OnBottomReachedListener {

        fun onBottomReached(position: Int)

    }

    fun OnBottomReachedListener(onBottomReachedListener: OnBottomReachedListener) {

        //this.onBottomReachedListener = onBottomReachedListener
    }
    internal var resource: Int = 0
    inner class DataObjectHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var sno : TextView? = null
        var month : TextView? = null
        var dat : TextView? = null
        var rec : TextView? = null
        var amnt : TextView? = null

        init {

            sno = itemView.findViewById(R.id.sno) as TextView
            month = itemView.findViewById(R.id.month) as TextView
            dat = itemView.findViewById(R.id.dat) as TextView
            rec = itemView.findViewById(R.id.rec) as TextView
            amnt = itemView.findViewById(R.id.amnt) as TextView


            itemView.setOnClickListener(this)

        }

        override fun onClick(v: View) {
            try {
                //listener.OnItemClick(v, adapterPosition, ITEM_CONTENT_VIEW_TYPE)

            } catch (e: Exception) {

            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DataObjectHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.paymentlist_adapter, parent, false)

        return DataObjectHolder(view)
    }

    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {
        val jobject = mValues
        val i = position
        val mon=(months[position])
        val cardno=(jobject.getJSONObject(i).getString("card_no"))
        val payid=(jobject.getJSONObject(i).getString("payid"))
        val receptno=(jobject.getJSONObject(i).getString("receptno"))
        val amount=(jobject.getJSONObject(i).getString("amount"))
        val pay_date=(jobject.getJSONObject(i).getString("pay_date"))
        val due=(jobject.getJSONObject(i).getString("due"))
        holder.sno!!.setText(""+(position+1)+".")
        holder.month!!.setText(mon)
        holder.dat!!.setText(pay_date)
        holder.rec!!.setText(receptno)
        holder.amnt!!.setText(amount)
        if (position == mValues.length() - 1){
           // onBottomReachedListener.onBottomReached(position);
        }
    }


    override fun getItemCount(): Int {
        return mValues.length()
    }

    companion object {
        internal val ITEM_CONTENT_VIEW_TYPE = 1
    }

}