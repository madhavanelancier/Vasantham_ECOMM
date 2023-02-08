package com.elanciers.vasantham_stores_ecomm

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.elanciers.vasantham_stores_ecomm.Adapters.OrderListAdapter
import com.elanciers.vasantham_stores_ecomm.Common.AppUtil
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands
import com.elanciers.vasantham_stores_ecomm.Common.DBController
import com.elanciers.vasantham_stores_ecomm.Common.Utils
import com.elanciers.vasantham_stores_ecomm.DataClass.OrderDetail
import com.elanciers.vasantham_stores_ecomm.retrofit.ApproveUtils
import com.elanciers.vasantham_stores_ecomm.retrofit.Resp
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.activity_order.wbview
import kotlinx.android.synthetic.main.connection_error.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderActivity : AppCompatActivity(),NetworkStateReceiver.NetworkStateReceiverListener,OrderListAdapter.OnItemClickListener,OrderListAdapter.OnBottomReachedListener {
    override fun OnItemClick(view: View, position: Int, viewType: Int) {
        if (Oders[position].deliver_sts!="Declined") {
            val its = Intent(this, OrderDetailActivity::class.java)
            its.putExtra("id", Oders[position].id.toString())
            its.putExtra("dateandtime", Oders[position].dateandtime.toString())
            its.putExtra("deliver_sts", Oders[position].deliver_sts.toString())
            its.putExtra("total", Oders[position].price.toString())
            its.putExtra("subtotal", Oders[position].subtotal.toString())
            its.putExtra("delivery", Oders[position].delivery.toString())
            its.putExtra("discount", Oders[position].discount.toString())
            its.putExtra("details", Oders[position].details.toString())
            its.putExtra("order_details", Oders[position].order_details.toString())
            its.putExtra("shopnm", Oders[position].shopnm)
            its.putExtra("shoploc", Oders[position].shoploc)
            its.putExtra("adrs_type", Oders[position].adrs_type)
            its.putExtra("coupon_code", Oders[position].coupon_code)
            its.putExtra("adrs", Oders[position].adrs)
            its.putExtra("payment_mode", Oders[position].payment_mode)
            startActivity(its)
        }
    }

    override fun onBottomReached(position: Int) {
        if (Appconstands.net_status(activity)) {
            connection.visibility=View.GONE
            if (Oders.size>14) {
                start = start + Oders.size
                getOrders()
            }
        }else{
            connection.visibility=View.VISIBLE
        }
    }
    val tag = "Orders"
    val activity = this
    lateinit var utils : Utils
    lateinit var db : DBController
    lateinit var pDialog: Dialog

    var start = 0
    var Oders = ArrayList<OrderDetail>()
    lateinit var adp :OrderListAdapter
    private var networkStateReceiver: NetworkStateReceiver? = null
    val mFragmentTitleList1 = java.util.ArrayList<String>()
    val mFragmentTitleList2 = java.util.ArrayList<String>()
    val mFragmentIconList1 = java.util.ArrayList<Int>()
    val mFragmentIconList2 = java.util.ArrayList<Int>()
    fun startNetworkBroadcastReceiver(currentContext: Context?) {
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(currentContext as NetworkStateReceiver.NetworkStateReceiverListener?)
        registerNetworkBroadcastReceiver(currentContext!!)
    }
    fun unregisterNetworkBroadcastReceiver(currentContext: Context) {
        currentContext.unregisterReceiver(networkStateReceiver)
    }
    fun registerNetworkBroadcastReceiver(currentContext: Context) {
        currentContext.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Appconstands.changeStatusBarColor(this, R.color.statusbar)
        setContentView(R.layout.activity_order)
        startNetworkBroadcastReceiver(this);

        /*val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mToolbar);
        val ab = supportActionBar
        ab!!.setDisplayShowHomeEnabled(true)
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24px)
        ab!!.title = "My Orders"*/
        utils = Utils(activity)
        db = DBController(activity)
        pDialog = Dialog(activity)

        mFragmentTitleList1.add(AppUtil.languageString("Home").toString())
        mFragmentTitleList1.add(("All Category"))
        mFragmentTitleList2.add(AppUtil.languageString("Orders").toString())
        mFragmentTitleList2.add("Profile")

        mFragmentIconList1.add(R.drawable.ic_home)
        mFragmentIconList1.add(R.drawable.ic_search)
        mFragmentIconList2.add(R.drawable.ic_shopping_bag)
        mFragmentIconList2.add(R.drawable.ic_baseline_account_balck_24px)

        //highLightCurrentTab()
        selecttab2(0)

        var data = OrderDetail()
        data.name="Departmental Store"
        data.loc="Anna Nagar"
        data.price="180"
        data.items="Thuvaram Paruppu 500 gms x 1, Coconut Oil 500 ml x 1"
        data.dateandtime="Oct 04, 12.45 PM"
        //Oders.add(data)
        data = OrderDetail()
        data.name="Departmental Store"
        data.loc="Anna Nagar"
        data.price="180"
        data.items="Thuvaram Paruppu 500 gms x 1, Coconut Oil 500 ml x 1"
        data.dateandtime="Oct 04, 12.45 PM"
        //Oders.add(data)

        adp = OrderListAdapter(activity,Oders,activity,activity)
        orderlist.adapter = adp



        conbtn.setOnClickListener {
            if (Appconstands.net_status(activity)) {
                connection.visibility=View.GONE
                //start = start + Oders.size
                start =0
                Oders.clear()
                getOrders()
            }else{
                connection.visibility=View.VISIBLE
            }
        }

        wbview.settings.setJavaScriptEnabled(true)
        wbview!!.setWebViewClient(HelloWebViewClient())
        wbview.getSettings().setLoadWithOverviewMode(true);
        wbview.getSettings().setUseWideViewPort(true);
        val settings = wbview.settings
        settings.cacheMode = WebSettings.LOAD_DEFAULT


        // Enable zooming in web view
        settings.setSupportZoom(false)
        settings.builtInZoomControls = false
        settings.displayZoomControls = false

        // Zoom web view text
        settings.textZoom = 100

        // Enable disable images in web view
        settings.blockNetworkImage = false
        // Whether the WebView should load image resources
        settings.loadsImagesAutomatically = true


        // More web view settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.safeBrowsingEnabled = true  // api 26
        }
        //settings.pluginState = WebSettings.PluginState.ON
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.mediaPlaybackRequiresUserGesture = false


        // More optional settings, you can enable it by yourself
        settings.domStorageEnabled = true
        settings.setSupportMultipleWindows(true)
        settings.loadWithOverviewMode = true
        settings.allowContentAccess = true
        settings.setGeolocationEnabled(true)
        settings.allowUniversalAccessFromFileURLs = true
        settings.allowFileAccess = true

        // WebView settings
        wbview.fitsSystemWindows = true


        /*
            if SDK version is greater of 19 then activate hardware acceleration
            otherwise activate software acceleration
        */
        wbview.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        wbview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url.toString())
                return true
            }
        }
        wbview.loadUrl("https://vasanthamstore.com/my-account/customer-order")

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                //highLightCurrentTab()
                println("onTabSelected tab1 : " + tab.position)
                selecttab1(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                println("onTabUnselected tab1")
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                println("onTabReselected tab1")
                selecttab1(tab.position)
            }
        })

        tabs2.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab2: TabLayout.Tab) {
                //highLightCurrentTab()
                println("onTabSelected tab2 : " + tab2.position)
                selecttab2(tab2.position)

            }

            override fun onTabUnselected(tab2: TabLayout.Tab) {
                println("onTabUnselected tab2")
            }

            override fun onTabReselected(tab2: TabLayout.Tab) {
                println("onTabReselected tab2")
                selecttab2(tab2.position)
            }
        })

        tabImageViews.setOnClickListener {
            startActivity(Intent(activity, Signin_Due_Activity::class.java))
            /*val st = Intent(this,Dashboard::class.java)
            st.putExtra("cardno","20220018")
            startActivity(st)*/
        }
    }

    fun selecttab1(position: Int){
        val tab3 = tabs.getTabAt(0)
        assert(tab3 != null)
        tab3!!.setCustomView(null)
        val view3 = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null)
        val tabTextView3 = view3.findViewById<TextView>(R.id.tabTextView)
        tabTextView3.setText(mFragmentTitleList1.get(0))
        val tabImageView3 = view3.findViewById<ImageView>(R.id.tabImageView)
        tabImageView3.setImageResource(mFragmentIconList1.get(0))
        tab3.setCustomView(view3)

        val tab4 = tabs.getTabAt(1)
        assert(tab4 != null)
        tab4!!.setCustomView(null)
        val view4 = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null)
        val tabTextView4 = view4.findViewById<TextView>(R.id.tabTextView)
        tabTextView4.setText(mFragmentTitleList1.get(1))
        val tabImageView4 = view4.findViewById<ImageView>(R.id.tabImageView)
        tabImageView4.setImageResource(mFragmentIconList1.get(1))
        tab4.setCustomView(view4)

        val tab2 = tabs2.getTabAt(0)
        assert(tab2 != null)
        tab2!!.setCustomView(null)
        val view = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null)
        val tabTextView = view.findViewById<TextView>(R.id.tabTextView)
        tabTextView.setText(mFragmentTitleList2.get(0))
        val tabImageView = view.findViewById<ImageView>(R.id.tabImageView)
        tabImageView.setImageResource(mFragmentIconList2.get(0))
        tab2.setCustomView(view)

        val tab1 = tabs2.getTabAt(1)
        assert(tab1 != null)
        tab1!!.setCustomView(null)
        val view1 = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null)
        val tabTextView1 = view1.findViewById<TextView>(R.id.tabTextView)
        tabTextView1.setText(mFragmentTitleList2.get(1))
        val tabImageView1 = view1.findViewById<ImageView>(R.id.tabImageView)
        tabImageView1.setImageResource(mFragmentIconList2.get(1))
        tab1.setCustomView(view1)



        val tab = tabs.getTabAt(position)
        assert(tab != null)
        tab!!.setCustomView(null)
        tab!!.setCustomView(getSelectedTabView1(position))
        if(position==0)
        {
            startActivity(Intent(activity, HomeActivity::class.java))
            overridePendingTransition(0,0)


        }
        else if(position==1){
            startActivity(Intent(activity, ProductSearchActivity::class.java))
            overridePendingTransition(0,0)


        }

    }
    fun selecttab2(position: Int){

        val tab3 = tabs.getTabAt(0)
        assert(tab3 != null)
        tab3!!.setCustomView(null)
        val view3 = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null)
        val tabTextView3 = view3.findViewById<TextView>(R.id.tabTextView)
        tabTextView3.setText(mFragmentTitleList1.get(0))
        val tabImageView3 = view3.findViewById<ImageView>(R.id.tabImageView)
        tabImageView3.setImageResource(mFragmentIconList1.get(0))
        tab3.setCustomView(view3)

        val tab4 = tabs.getTabAt(1)
        assert(tab4 != null)
        tab4!!.setCustomView(null)
        val view4 = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null)
        val tabTextView4 = view4.findViewById<TextView>(R.id.tabTextView)
        tabTextView4.setText(mFragmentTitleList1.get(1))
        val tabImageView4 = view4.findViewById<ImageView>(R.id.tabImageView)
        tabImageView4.setImageResource(mFragmentIconList1.get(1))
        tab4!!.setCustomView(view4)

        val tab2 = tabs2.getTabAt(0)
        assert(tab2 != null)
        tab2!!.setCustomView(null)
        val view = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null)
        val tabTextView = view.findViewById<TextView>(R.id.tabTextView)
        tabTextView.setText(mFragmentTitleList2.get(0))
        val tabImageView = view.findViewById<ImageView>(R.id.tabImageView)
        tabImageView.setImageResource(mFragmentIconList2.get(0))
        tab2!!.setCustomView(view)

        val tab1 = tabs2.getTabAt(1)
        assert(tab1 != null)
        tab1!!.setCustomView(null)
        val view1 = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null)
        val tabTextView1 = view1.findViewById<TextView>(R.id.tabTextView)
        tabTextView1.setText(mFragmentTitleList2.get(1))
        val tabImageView1 = view1.findViewById<ImageView>(R.id.tabImageView)
        tabImageView1.setImageResource(mFragmentIconList2.get(1))
        tab1.setCustomView(view1)

        val tab = tabs2.getTabAt(position)
        assert(tab != null)
        tab!!.setCustomView(null)
        tab!!.setCustomView(getSelectedTabView2(position))

        if (position==1){
            val it = Intent(activity, ProfileActivity::class.java)
            //it.putExtra("name", catgs[position].content)
            //it.putExtra("id", catgs[position].id)
            //it.putExtra("lat", lat)
            //it.putExtra("lng", longi)
            startActivity(it)
            overridePendingTransition(0,0)

            //startActivity(Intent(activity, OrderActivity::class.java))
        }

    }


    fun getCentreTab(position: Int):View{
        val view = LayoutInflater.from(activity).inflate(R.layout.centre_tab, null)
        //val tabTextView = view.findViewById<TextView>(R.id.tabTextView)
        //tabTextView.setText(mFragmentTitleList.get(position))
        //val tabImageView = view.findViewById<ImageView>(R.id.tabImageView)
        //tabImageView.setImageResource(mFragmentIconList.get(position))
        return view
    }

    /*fun getTabView(position: Int): View {
        val view = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null)
        val tabTextView = view.findViewById<TextView>(R.id.tabTextView)
        tabTextView.setText(mFragmentTitleList.get(position))
        val tabImageView = view.findViewById<ImageView>(R.id.tabImageView)
        tabImageView.setImageResource(mFragmentIconList.get(position))
        return view
    }*/

    fun getSelectedTabView1(position: Int): View {
        val view = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null)
        val tabTextView = view.findViewById<TextView>(R.id.tabTextView)
        tabTextView.setText(mFragmentTitleList1.get(position))
        tabTextView.setTextColor(ContextCompat.getColor(activity, R.color.colorAccent))
        val tabImageView = view.findViewById<ImageView>(R.id.tabImageView)
        tabImageView.setImageResource(mFragmentIconList1.get(position))
        tabImageView.setColorFilter(
            ContextCompat.getColor(activity, R.color.colorAccent),
            PorterDuff.Mode.SRC_ATOP
        )
        return view
    }

    fun getSelectedTabView2(position: Int): View {
        val view = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null)
        val tabTextView = view.findViewById<TextView>(R.id.tabTextView)
        tabTextView.setText(mFragmentTitleList2.get(position))
        tabTextView.setTextColor(ContextCompat.getColor(activity, R.color.colorAccent))
        val tabImageView = view.findViewById<ImageView>(R.id.tabImageView)
        tabImageView.setImageResource(mFragmentIconList2.get(position))
        tabImageView.setColorFilter(
            ContextCompat.getColor(activity, R.color.colorAccent),
            PorterDuff.Mode.SRC_ATOP
        )
        return view
    }

    private inner class HelloWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            url: String
        ): Boolean {
            view.loadUrl(url)
            println("loadurl" + url)
            runOnUiThread {
                //  textView8.setText("Almost finish...")
                Appconstands.loading_show(activity, pDialog).show()

            }
            return true
        }
        override fun onPageFinished(view: WebView?, url: String?) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url)
            // pdialog!!.dismiss()
            Appconstands.loading_show(activity, pDialog).dismiss()

            //frameLayout1.visibility=View.VISIBLE
            wbview!!.visibility=View.VISIBLE

        }
        override fun onReceivedError(
            view: WebView?, errorCode: Int,
            description: String, failingUrl: String?
        ) {
            Log.i(
                "FragmentActivity.TAG",
                "GOT Page error : code : $errorCode Desc : $description"
            )
            //frameLayout1.visibility=View.VISIBLE
            wbview!!.visibility=View.VISIBLE
            //TODO We can show customized HTML page when page not found/ or server not found error.
            super.onReceivedError(view, errorCode, description, failingUrl)
        }
    }

    override fun onResume() {
        super.onResume()
        selecttab2(0)
        if (Appconstands.net_status(activity)) {
            connection.visibility=View.GONE
            registerNetworkBroadcastReceiver(this)

            //start = start + Oders.size
            start =0
            Oders.clear()
            //getOrders()
        }else{
            connection.visibility=View.VISIBLE
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.profile_page, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.getItemId()
        when(id){
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun getOrders(){
        /*val pDialo = ProgressDialog(this);
        pDialo.setMessage("Loading....");
        pDialo.setIndeterminate(false);
        pDialo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialo.setCancelable(false);
        pDialo.show()*/
        println("utils.userid() : "+utils.userid())
        if (start==0) {
            pDialog= Dialog(activity)
            Appconstands.loading_show(activity, pDialog).show()
        }
        val call = ApproveUtils.Get.Orders(utils.userid(),start.toString())
        call.enqueue(object : Callback<Resp> {
            override fun onResponse(call: Call<Resp>, response: Response<Resp>) {
                Log.e("$tag responce", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp
                    println(example)
                    if (example.status == "Success") {
                        val ad = response.body() as Resp
                        for (z in 0 until ad.response!!.size) {
                            val id = if (ad.response!![z].id.isNullOrEmpty()) "" else ad.response!![z].id.toString()
                            val vid = if (ad.response!![z].vid.isNullOrEmpty()) "" else ad.response!![z].vid.toString()
                            val location = if (ad.response!![z].location.isNullOrEmpty()) "" else ad.response!![z].location.toString()
                            val subtotal = if (ad.response!![z].subtotal.isNullOrEmpty()) "" else ad.response!![z].subtotal.toString()
                            val discount = if (ad.response!![z].discount.isNullOrEmpty()) "" else ad.response!![z].discount.toString()
                            val delivery = if (ad.response!![z].delivery.isNullOrEmpty()) "" else ad.response!![z].delivery.toString()
                            val ven_address = if (ad.response!![z].ven_address.isNullOrEmpty()) "" else ad.response!![z].ven_address.toString()
                            val user_address = if (ad.response!![z].user_address.isNullOrEmpty()) "" else ad.response!![z].user_address.toString()
                            val order_status = if (ad.response!![z].order_status.isNullOrEmpty()) "" else ad.response!![z].order_status.toString()
                            val user_type = if (ad.response!![z].user_type.isNullOrEmpty()) "" else ad.response!![z].user_type.toString()
                            val datetime = if (ad.response!![z].datetime.isNullOrEmpty()) "" else ad.response!![z].datetime.toString()
                            val coupon_code = if (ad.response!![z].coupon_code.isNullOrEmpty()) "" else ad.response!![z].coupon_code.toString()
                            val total = if (ad.response!![z].total.isNullOrEmpty()) "" else ad.response!![z].total.toString()
                            val payment_mode = if (ad.response!![z].payment_mode.isNullOrEmpty()) "" else ad.response!![z].payment_mode.toString()
                            val order_details = ad.response!![z].order_details
                            var itm = ""
                            val srr = JSONArray()
                            for (ads in 0 until order_details!!.size) {
                                val obj = JSONObject()
                                val product = if (order_details[ads].product.isNullOrEmpty()) "" else order_details[ads].product.toString()
                                val qty = if (order_details[ads].qty.isNullOrEmpty()) "" else order_details[ads].qty.toString()
                                val mesure = if (order_details[ads].mesure.isNullOrEmpty()) "" else order_details[ads].mesure.toString()
                                val price = if (order_details[ads].price.isNullOrEmpty()) "" else order_details[ads].price.toString()
                                val tota = if (order_details[ads].total.isNullOrEmpty()) "" else order_details[ads].total.toString()
                                itm = itm + "" + product + " " + mesure + " x " + qty + ","
                                obj.put("product",product)
                                obj.put("qty",qty)
                                obj.put("mesure",mesure)
                                obj.put("price",price)
                                obj.put("total",tota)
                                srr.put(obj)
                            }
                            println("srr : "+srr)
                            val data = OrderDetail()
                            data.id = id
                            data.name = vid
                            data.loc = location
                            data.price = total
                            data.subtotal = subtotal
                            data.delivery = delivery
                            data.deliver_sts = order_status
                            data.discount = discount
                            data.items = itm.removeSuffix(",")//"Thuvaram Paruppu 500 gms x 1, Coconut Oil 500 ml x 1"
                            data.dateandtime = datetime
                            data.details = srr
                            data.order_details = order_details
                            data.shopnm = vid
                            data.shoploc = ven_address
                            data.adrs_type =user_type
                            data.coupon_code =coupon_code
                            data.adrs = user_address
                            data.payment_mode = payment_mode
                            Oders.add(data)
                            adp.notifyDataSetChanged()
                            adps()
                        }
                        if(Oders.isEmpty()){
                            imageView31.visibility=View.VISIBLE
                            textView36.visibility=View.VISIBLE
                        }
                        else{
                            imageView31.visibility=View.GONE
                            textView36.visibility=View.GONE
                        }
                        //startActivity(Intent(activity, HomeActivity::class.java))
                    } else {
                        if(Oders.isEmpty()){
                            imageView31.visibility=View.VISIBLE
                            textView36.visibility=View.VISIBLE
                        }
                        else{
                            imageView31.visibility=View.GONE
                            textView36.visibility=View.GONE
                        }
                        Toast.makeText(
                                activity,
                                example.message,
                                Toast.LENGTH_LONG
                        ).show()
                    }

                }
                if (start==0) {
                    pDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<Resp>, t: Throwable) {
                Log.e("$tag Fail response", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                            activity,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                    ).show()
                }
                adps()
                if (start==0) {
                    pDialog.dismiss()
                }
                //loading_show(activity).dismiss()
            }
        })
    }
    fun adps(){
        /*if (Oders.isEmpty()){
            empdes.setText("You don't have any Oders . so kindly add Oders that hepls you checkout faster.")
            empbtn.setText("")
            empty_layout.visibility=View.VISIBLE
        }else{
            empty_layout.visibility=View.GONE
        }*/
    }

    override fun networkAvailable() {

    }

    override fun networkUnavailable() {
        val i=Intent(this, NetActivity::class.java)
        startActivity(i)
    }
}
/*{
    "Status": "Success",
    "Message": "",
    "Response": [
        {
            "id": 3,
            "vid": "Reliance",
            "subtotal": "90",
            "discount": null,
            "delivery": "10",
            "total": "100",
            "order_details": [
                {
                    "product": "Oil",
                    "qty": "1",
                    "mesure": "1 Kg",
                    "price": "60",
                    "total": "60"
                },
                {
                    "product": "Peas",
                    "qty": "1",
                    "mesure": "2 Kg",
                    "price": "100",
                    "total": "100"
                }
*/
