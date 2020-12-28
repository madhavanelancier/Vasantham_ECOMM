package com.elanciers.vasantham_stores_ecomm.Adapters

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands
import com.elanciers.vasantham_stores_ecomm.Common.DBController
import com.elanciers.vasantham_stores_ecomm.Common.ExpandableHeightGridView
import com.elanciers.vasantham_stores_ecomm.DataClass.CartProduct
import com.elanciers.vasantham_stores_ecomm.DataClass.OfferItem
import com.elanciers.vasantham_stores_ecomm.OffersActivity
import com.elanciers.vasantham_stores_ecomm.R
import com.google.android.material.bottomsheet.BottomSheetDialog

class OfferListAdapter(public val mActivity: Activity, private val items: ArrayList<OfferItem>, private val listener: OnItemClickListener, private var onBottomReachedListener : OnBottomReachedListener ) : RecyclerView.Adapter<OfferListAdapter.DataObjectHolder>()
{
    val cid = ""
    val cnm = ""
    interface OnItemClickListener {
        fun OnItemClick(view: View, position: Int, viewType: Int)
    }

    interface OnBottomReachedListener {

        fun onBottomReached(position: Int)

    }

    fun OnBottomReachedListener(onBottomReachedListener: OnBottomReachedListener) {

        this.onBottomReachedListener = onBottomReachedListener
    }
    internal var resource: Int = 0
    inner class DataObjectHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        ///internal var card : CardView
        var vendor_lay : LinearLayout? = null
        internal var image: ImageView
        internal var title : TextView
        internal var location : TextView
        internal var desc : TextView
        internal var times : TextView


        //product
        var title_lay : LinearLayout? = null
        var add : LinearLayout? = null
        var subcat : TextView? = null
        var subcatlist : ExpandableHeightGridView? = null
        var nm : TextView? = null
        var price : TextView? = null
        var qty : TextView? = null
        var option : TextView? = null
        var customize : TextView? = null
        var image1 : ImageView? = null
        var minus : ImageView? = null
        var plus : ImageView? = null
        lateinit var db : DBController

        init {

            //card = itemView.findViewById(R.id.card) as CardView
            vendor_lay = itemView.findViewById(R.id.vendor_lay) as LinearLayout
            image= itemView.findViewById(R.id.restaurantimg) as ImageView
            title = itemView.findViewById(R.id.restaurantname) as TextView
            location = itemView.findViewById(R.id.restaurantaddress) as TextView
            desc = itemView.findViewById(R.id.offers) as TextView
            times = itemView.findViewById(R.id.restaurantcity) as TextView

            //product
            title_lay = itemView.findViewById(R.id.title_lay) as LinearLayout
            add = itemView.findViewById(R.id.add) as LinearLayout
            subcat = itemView.findViewById(R.id.subcat) as TextView
            //subcatlist = itemView.findViewById(R.id.subcatlist) as ExpandableHeightGridView

            nm = itemView.findViewById(R.id.nm) as TextView
            price = itemView.findViewById(R.id.price) as TextView
            qty = itemView.findViewById(R.id.qty) as TextView
            option = itemView.findViewById(R.id.options) as TextView
            customize = itemView.findViewById(R.id.customize) as TextView
            image1 = itemView.findViewById(R.id.image1) as ImageView
            minus = itemView.findViewById(R.id.minus) as ImageView
            plus = itemView.findViewById(R.id.plus) as ImageView
            itemView.setOnClickListener(this)

        }

        override fun onClick(v: View) {
            try {
                listener.OnItemClick(v, adapterPosition, ITEM_CONTENT_VIEW_TYPE)

            } catch (e: Exception) {

            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DataObjectHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.offers_adapter, parent, false)

        return DataObjectHolder(view)
    }

    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {


        if (!items[position].img.isNullOrEmpty()) {
            //Glide.with(mActivity).load(Appconstands.ImageDomain + items[position].img).error(R.mipmap.povalogo).into(holder.image)
        }
        holder.title.setText(items[position].name)
        val loc = items[position].distance+" " + items[position].loc
        holder.location.setText(loc)
        if (items[position].offer.isNullOrEmpty()){
            holder.desc.visibility= View.INVISIBLE
        }else {
            holder.desc.setText(items[position].offer)
            holder.desc.visibility= View.VISIBLE
        }
        holder.times.setText(items[position].tim)
        if (items[position].tim.isNullOrEmpty()){
            holder.times.visibility=View.INVISIBLE
        }else{
            holder.times.visibility=View.VISIBLE
        }
        /*if (position == items.size - 1){
            onBottomReachedListener.onBottomReached(position);
        }*/
        if (position==0) {
            holder.vendor_lay!!.visibility = View.VISIBLE
        }else {
            if (items[position].id != items[position - 1].id) {
                holder.vendor_lay!!.visibility = View.VISIBLE
            } else {
                holder.vendor_lay!!.visibility = View.GONE
            }
        }

        //Product
        val db = DBController(mActivity)
        //val utils = Utils(mActivity)
        /*if (position==0) {
            holder.title_lay!!.visibility=View.VISIBLE
            holder.subcat!!.setText(items[position].title)
        }else{
            if (items[position].title==items[position-1].title){
                holder.title_lay!!.visibility = View.GONE
                holder.subcat!!.setText(items[position].title)
            }else {
                holder.title_lay!!.visibility = View.VISIBLE
                holder.subcat!!.setText(items[position].title)
            }
        }*/

        //Option
        if (items[position].options!!.isNotEmpty()) {
            val sp = items[position].selpos!!
            val qty = db.qty(items[position].pid.toString())//,items[position].options!![sp].id.toString()
            println("qty : "+qty)
            items[position].qty = qty
            holder.option!!.visibility= View.VISIBLE
            holder.option!!.setText(items[position].options!![0].name)
            println("items[position].options!!.size : "+items[position].options!!.size)
            if (items[position].options!!.size==1){
                holder.customize!!.visibility=View.GONE
            }else{
                holder.customize!!.visibility=View.VISIBLE
            }
        }else{
            holder.option!!.visibility= View.INVISIBLE
        }

        if (!items[position].pimg.isNullOrEmpty()){
            Glide.with(mActivity).load(items[position].pimg).error(R.mipmap.ic_bringszo_logo).into(holder.image1!!)
        }
        //SetText
        holder.nm!!.setText(items[position].pname)
        holder.qty!!.setText(items[position].qty)
        holder.price!!.setText("₹ "+items[position].options!![0].price)
        val varient = db.varients(items[position].pid.toString())
        println("varient : "+varient)
        if (!varient.isNullOrEmpty()) {
            if (varient!="0") {
                holder!!.customize!!.setText(varient + " Varients added")
            }else{
                holder!!.customize!!.setText("Customizable")
            }
        }

        //Qty
        if (items[position].qty=="0"){
            holder.add!!.visibility=View.VISIBLE
        }else{
            holder.add!!.visibility=View.GONE
        }

        //Add
        holder.add!!.setOnClickListener {
            if (db.vendor_id.isNullOrEmpty()||db.vendor_id==items[position].id) {
                println("db.vendor_id : "+db.vendor_id)
                println("items[position].id : "+items[position].id)
                val sp = items[position].selpos!!
                val qty = db.optqty(items[position].pid.toString(),items[position].options!![sp].id.toString())
                val cart = CartProduct()
                cart.vendor_id = items[position].id
                cart.vendor_nm = items[position].name
                cart.img = items[position].img
                cart.cid = cid
                cart.cnm = cnm
                cart.sid = items[position].sid
                cart.snm = items[position].name
                cart.pid = items[position].pid
                cart.pnm = items[position].pname
                cart.qty = (qty.toString().toInt() + 1).toString()
                cart.opid = items[position].options!![sp].id
                cart.opnm = items[position].options!![sp].name
                cart.price = items[position].options!![sp].price
                val d = db.CartIn_Up(cart)
                println("cartIn_up : " + d)
                items[position].qty = (qty.toString().toInt() + 1).toString()
                //notifyDataSetChanged()
                //holder.add!!.visibility=View.GONE
                //items[position].qty="1"
                
                (mActivity as OffersActivity).view_cart_lay()
                
                notifyDataSetChanged()
            }else{
                println("db.vendor_id : "+db.vendor_id)
                println("items[position].id : "+items[position].id)
                //CartRemove
                val alert = AlertDialog.Builder(mActivity)
                alert.setTitle("Replace cart item?")
                alert.setMessage("Your cart contains items from ${db.vendor_nm}. Do you want discart the selection and add items from ${items[position].name}?")
                alert.setPositiveButton("yes", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        db.dropHoleCart()
                        val sp = items[position].selpos!!
                        val qty = db.optqty(items[position].pid.toString(),items[position].options!![sp].id.toString())
                        val cart = CartProduct()
                        cart.vendor_id = items[position].id
                        cart.vendor_nm = items[position].name
                        cart.img = items[position].img
                        cart.cid = cid
                        cart.cnm = cnm
                        cart.sid = items[position].sid
                        cart.snm = items[position].name
                        cart.pid = items[position].pid
                        cart.pnm = items[position].pname
                        cart.qty = (qty.toString().toInt() + 1).toString()
                        cart.opid = items[position].options!![sp].id
                        cart.opnm = items[position].options!![sp].name
                        cart.price = items[position].options!![sp].price
                        val d = db.CartIn_Up(cart)
                        println("cartIn_up : " + d)
                        items[position].qty = (qty.toString().toInt() + 1).toString()
                        //notifyDataSetChanged()
                        //holder.add!!.visibility=View.GONE
                        //items[position].qty="1"
                        (mActivity as OffersActivity).view_cart_lay()
                        notifyDataSetChanged()
                    }
                })
                alert.setNegativeButton("no", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        dialog!!.dismiss()
                    }
                })
                alert.show()
            }
        }

        //Minus
        holder.minus!!.setOnClickListener {
            if (db.vendor_id.isNullOrEmpty()||db.vendor_id==items[position].id) {
                if (db.more(items[position].pid.toString())>1){
                    //RemovePop
                    RemovePop(items[position])
                }else {
                    println("db.vendor_id : " + db.vendor_id)
                    println("items[position].id : " + items[position].id)
                    val sp = items[position].selpos!!
                    val qty = db.optqty(items[position].pid.toString(), items[position].options!![sp].id.toString())
                    val cart = CartProduct()
                    cart.vendor_id = items[position].id
                    cart.vendor_nm = items[position].name
                    cart.img = items[position].img
                    cart.cid = cid
                    cart.cnm = cnm
                    cart.sid = items[position].sid
                    cart.snm = items[position].name
                    cart.pid = items[position].pid
                    cart.pnm = items[position].pname
                    cart.qty = (qty.toString().toInt() - 1).toString()
                    cart.opid = items[position].options!![sp].id
                    cart.opnm = items[position].options!![sp].name
                    cart.price = items[position].options!![sp].price
                    val d = db.CartIn_Up(cart)
                    println("cartIn_up : " + d)
                    items[position].qty = (qty.toString().toInt() - 1).toString()
                    //notifyDataSetChanged()
                    //holder.add!!.visibility=View.GONE
                    //items[position].qty="1"
                    (mActivity as OffersActivity).view_cart_lay()
                    notifyDataSetChanged()
                }
            }else{
                println("db.vendor_id : "+db.vendor_id)
                println("items[position].id : "+items[position].id)
                //CartRemove
            }
            //if (holder.qty!!.text.toString().toInt()>=1){
            //items[position].qty = (holder.qty!!.text.toString().toInt()-1).toString()
            //}else{
            //items[position].qty = "0"
            //}
            //notifyDataSetChanged()
        }

        //Plus
        holder.plus!!.setOnClickListener {
            if (db.vendor_id.isNullOrEmpty()||db.vendor_id==items[position].id) {
                if (db.more(items[position].pid.toString())>1){
                    //db.LastItm(items[position].pid.toString())
                    RepeatPop(items[position])
                }else {
                    if (items[position].options!!.size>1){
                        RepeatPop(items[position])
                    }else {
                        println("db.vendor_id : " + db.vendor_id)
                        println("items[position].id : " + items[position].id)
                        val sp = items[position].selpos!!
                        val qty = db.optqty(items[position].pid.toString(), items[position].options!![sp].id.toString())
                        val cart = CartProduct()
                        cart.vendor_id = items[position].id
                        cart.vendor_nm = items[position].name
                        cart.img = items[position].img
                        cart.cid = cid
                        cart.cnm = cnm
                        cart.sid = items[position].sid
                        cart.snm = items[position].name
                        cart.pid = items[position].pid
                        cart.pnm = items[position].pname
                        cart.qty = (qty.toString().toInt() + 1).toString()
                        cart.opid = items[position].options!![sp].id
                        cart.opnm = items[position].options!![sp].name
                        cart.price = items[position].options!![sp].price
                        val d = db.CartIn_Up(cart)
                        println("cartIn_up : " + d)
                        items[position].qty = (qty.toString().toInt() + 1).toString()
                        (mActivity as OffersActivity).view_cart_lay()
                        notifyDataSetChanged()
                    }
                }
            }else{
                println("db.vendor_id : "+db.vendor_id)
                println("items[position].id : "+items[position].id)
                //CartRemove
            }
        }

        //OptionPOP
        holder.option!!.setOnClickListener {
            if (items[position].options!!.size>1) {
                OptionPop(items[position])
            }

        }

        if (position == items.size - 1){
            onBottomReachedListener.onBottomReached(position);
        }
    }


    override fun getItemCount(): Int {
        return items.size
    }

    companion object {
        internal val ITEM_CONTENT_VIEW_TYPE = 1
    }

    fun RepeatPop(items : OfferItem){
        val db = DBController(mActivity)
        //val utils = Utils(mActivity)
        val openwith = BottomSheetDialog(mActivity)
        openwith.setOnDismissListener {
            println("dismiss")
        }
        openwith.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
        val popUpView = mActivity.layoutInflater.inflate(R.layout.repeat_item, null)
        val pronm = popUpView.findViewById(R.id.pronm) as TextView
        val variant = popUpView.findViewById(R.id.variant) as TextView
        val variant2 = popUpView.findViewById(R.id.variant2) as TextView
        val choose = popUpView.findViewById(R.id.choose) as TextView
        val repeat = popUpView.findViewById(R.id.repeat) as TextView
        val cancel = popUpView.findViewById(R.id.cancel) as ImageView
        pronm.setText(items.name)
        val cart1 = db.LastItm(items.pid.toString())
        val opid = cart1.opid
        val opnm = cart1.opnm
        val price = cart1.price
        val qty = cart1.qty
        variant.setText(cart1.opnm)
        variant2.setText(Appconstands.rupees +cart1.price)
        val animMoveToTop = AnimationUtils.loadAnimation(mActivity, R.anim.bottom_top)
        popUpView.animation =animMoveToTop
        cancel.setOnClickListener {
            openwith.dismiss()
        }
        repeat.setOnClickListener {
            items.price = price
            items.selpos = opid.toString().toInt()
            println("db.vendor_id : "+db.vendor_id)
            println("utils.getvendor_id() : "+items.id)
            //val sp = opid.toString().toInt()
            //val qty = db.optqty(items.pid.toString(),opid.toString())
            val cart = CartProduct()
            cart.vendor_id = items.id
            cart.vendor_nm = items.name
            cart.img = items.img
            cart.cid = cid
            cart.cnm = cnm
            cart.sid = items.sid
            cart.snm = items.name
            cart.pid = items.pid
            cart.pnm = items.pname
            cart.qty = (qty.toString().toInt() + 1).toString()
            cart.opid = opid.toString()
            cart.opnm = opnm.toString()
            cart.price = price.toString()
            val d = db.CartIn_Up(cart)
            println("cartIn_up : " + d)
            items.qty = (qty.toString().toInt() + 1).toString()
            //notifyDataSetChanged()
            //holder.add!!.visibility=View.GONE
            //items.qty="1"
            openwith.dismiss()
            (mActivity as OffersActivity).view_cart_lay()
            notifyDataSetChanged()
        }

        choose.setOnClickListener {
            openwith.dismiss()
            OptionPop(items)
        }
        openwith.setContentView(popUpView);
        openwith.setCancelable(true)
        openwith.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        openwith.window!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        openwith.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val displaymetrics = DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        val width =  (displaymetrics.widthPixels * 1);
        val height =  (displaymetrics.heightPixels * 1);
        openwith.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        openwith.show()
    }

    fun OptionPop(items: OfferItem){
        val db = DBController(mActivity)
        //val utils = Utils(mActivity)
        var selpos = items.selpos!!
        val openwith = BottomSheetDialog(mActivity)
        openwith.setOnDismissListener {
            println("dismiss")
        }
        openwith.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
        val popUpView = mActivity.layoutInflater.inflate(R.layout.options_popup, null)
        /*popUpView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val eventConsumed = gestureDetector.onTouchEvent(event);
                println("eventConsumed : "+eventConsumed)
                if (eventConsumed) {
                    return true;
                } else {
                    return false;
                }
            }

        });*/
        val pronm = popUpView.findViewById(R.id.pronm) as TextView
        val selected = popUpView.findViewById(R.id.selected) as TextView
        val itmtot = popUpView.findViewById(R.id.itmtot) as TextView
        val add_item = popUpView.findViewById(R.id.add_item) as TextView
        val cancel = popUpView.findViewById(R.id.cancel) as ImageView
        pronm.setText(items.name)
        val optionslist = popUpView.findViewById(R.id.optionslist) as RecyclerView
        //optionslist.isExpanded=true
        selected.setText(items.options!![selpos].name)
        itmtot.setText(Appconstands.rupees +items.options!![selpos].price)
        optionslist.adapter=OptionsRecyclerAdapter(mActivity,items.options!!,object : OptionsRecyclerAdapter.OnItemClickListener {
            override fun OnItemClick(view: View, pos: Int, viewType: Int, lastpos : Int) {
                selpos=pos
                println("option clicked name : "+items.options!![selpos].name)
                println("option clicked price : "+items.options!![selpos].price)
                selected.setText(items.options!![selpos].name)
                itmtot.setText(Appconstands.rupees +items.options!![selpos].price)
            }

        },selpos)
        //optionslist.adapter=OptionsAdapter(context,R.layout.optioslist_adapter,items.options!!)
        //val animMoveToTop = AnimationUtils.loadAnimation(mActivity, R.anim.bottom_top)
        /*animMoveToTop.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {}
        })*/
        //popUpView.animation =animMoveToTop
        cancel.setOnClickListener {
            openwith.dismiss()
        }

        add_item.setOnClickListener {
            openwith.dismiss()
            if (db.vendor_id.isNullOrEmpty()||db.vendor_id==items.id) {
                items.price = items.options!![selpos].price
                items.selpos = selpos
                println("db.vendor_id : "+db.vendor_id)
                println("utils.getvendor_id() : "+items.id)
                val sp = selpos
                val qty = db.optqty(items.pid.toString(),items.options!![sp].id.toString())
                val cart = CartProduct()
                cart.vendor_id = items.id
                cart.vendor_nm = items.name
                cart.img = items.img
                cart.cid = cid
                cart.cnm = cnm
                cart.sid = items.sid
                cart.snm = items.name
                cart.pid = items.pid
                cart.pnm = items.pname
                cart.qty = (qty.toString().toInt() + 1).toString()
                cart.opid = items.options!![sp].id
                cart.opnm = items.options!![sp].name
                cart.price = items.options!![sp].price
                val d = db.CartIn_Up(cart)
                println("cartIn_up : " + d)
                items.qty = (qty.toString().toInt() + 1).toString()
                //notifyDataSetChanged()
                //holder.add!!.visibility=View.GONE
                //items.qty="1"
                (mActivity as OffersActivity).view_cart_lay()
                notifyDataSetChanged()
            }else{
                println("db.vendor_id : "+db.vendor_id)
                println("utils.getvendor_id() : "+items.id)
            }
            //notifyDataSetChanged()
        }

        openwith.setContentView(popUpView);
        openwith.setCancelable(true)
        openwith.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        openwith.window!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        openwith.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val displaymetrics = DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        val width =  (displaymetrics.widthPixels * 1);
        val height =  (displaymetrics.heightPixels * 1);
        openwith.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        openwith.show()
    }

    fun RemovePop(items: OfferItem){
        val db = DBController(mActivity)
        //val utils = Utils(mActivity)
        val openwith = BottomSheetDialog(mActivity)
        openwith.setOnDismissListener {
            println("dismiss")
            notifyDataSetChanged()
        }
        openwith.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
        val popUpView = mActivity.layoutInflater.inflate(R.layout.remove_item, null)
        val pronm = popUpView.findViewById(R.id.pronm) as TextView
        val optionslist = popUpView.findViewById(R.id.optionslist) as ExpandableHeightGridView
        val cancel = popUpView.findViewById(R.id.cancel) as ImageView
        val cart1 = db.LastItm(items.pid.toString())
        pronm.setText("You have multiple customizations for ${cart1.pnm}. Choose which one to remove.")
        val cartlist = db.CartList()
        val adp = ProductAdapter(mActivity,R.layout.remove_ite_adapter,cartlist)
        optionslist.adapter=adp
        optionslist.isExpanded=true

        val animMoveToTop = AnimationUtils.loadAnimation(mActivity, R.anim.bottom_top)
        popUpView.animation =animMoveToTop
        cancel.setOnClickListener {
            openwith.dismiss()
        }
        openwith.setContentView(popUpView);
        openwith.setCancelable(true)
        openwith.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        openwith.window!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        openwith.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val displaymetrics = DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        val width =  (displaymetrics.widthPixels * 1);
        val height =  (displaymetrics.heightPixels * 1);
        openwith.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        openwith.show()
    }

}