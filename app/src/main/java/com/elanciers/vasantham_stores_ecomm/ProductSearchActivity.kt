package com.elanciers.vasantham_stores_ecomm

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.elanciers.booking.DataClass.SpinnerPojo
import com.elanciers.vasantham_stores_ecomm.Adapters.TitledRecyclerAdapter
import com.elanciers.vasantham_stores_ecomm.Common.AppUtil
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands
import com.elanciers.vasantham_stores_ecomm.Common.DBController
import com.elanciers.vasantham_stores_ecomm.Common.Utils
import com.elanciers.vasantham_stores_ecomm.DataClass.AllCat
import com.elanciers.vasantham_stores_ecomm.DataClass.TitledProduct
import com.elanciers.vasantham_stores_ecomm.retrofit.ApproveUtils
import com.elanciers.vasantham_stores_ecomm.retrofit.Resp
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_product_search.*
import kotlinx.android.synthetic.main.activity_product_search.tabs
import kotlinx.android.synthetic.main.activity_product_search.tabs2
import kotlinx.android.synthetic.main.activity_product_search.wbview
import kotlinx.android.synthetic.main.connection_error.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductSearchActivity : AppCompatActivity(),NetworkStateReceiver.NetworkStateReceiverListener,TitledRecyclerAdapter.OnItemClickListener,TitledRecyclerAdapter.OnBottomReachedListener {
    override fun OnItemClick(view: View, position: Int, viewType: Int) {

    }

    override fun onBottomReached(position: Int) {
        /*start=start+Products.size
        getProducts(src)*/
        if (Appconstands.net_status(activity)) {
            connection.visibility=View.GONE
            if (Products.size>14) {
                start=start+Products.size
                //getProducts(src)
            }
        }else{
            connection.visibility=View.VISIBLE
        }
    }
    val tag = "Product"
    val activity = this
    lateinit var utils : Utils
    lateinit var db : DBController
    lateinit var pDialog: Dialog
    var froms="";
    val Products = ArrayList<AllCat>()
    lateinit var adp : TitledRecyclerAdapter
    var start =0
    var src = ""
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
        setContentView(R.layout.activity_product_search)
        startNetworkBroadcastReceiver(this);

        Appconstands.changeStatusBarColor(activity, R.color.statusbar)
        utils = Utils(activity)
        db = DBController(activity)

        mFragmentTitleList1.add(AppUtil.languageString("Home").toString())
        mFragmentTitleList1.add(("All Category"))
        mFragmentTitleList2.add(AppUtil.languageString("Orders").toString())
        mFragmentTitleList2.add("Profile")

        mFragmentIconList1.add(R.drawable.ic_home)
        mFragmentIconList1.add(R.drawable.ic_search)
        mFragmentIconList2.add(R.drawable.ic_shopping_bag)
        mFragmentIconList2.add(R.drawable.ic_baseline_account_balck_24px)

        //highLightCurrentTab()
        selecttab1(1)
        wbview.settings.setJavaScriptEnabled(true)
        wbview!!.setWebViewClient(HelloWebViewClient())
        wbview.getSettings().setLoadWithOverviewMode(true);
        wbview.getSettings().setUseWideViewPort(true);
        val settings = wbview.settings
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.setAppCachePath(cacheDir.path)


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
        wbview.loadUrl("https://vasanthamstore.com/category")

        try{
            val frm=intent.extras
            froms=frm!!.getString("frm").toString()
        }
        catch (e:Exception){

        }

        val mLayoutManager: LinearLayoutManager
        mLayoutManager = LinearLayoutManager(this)
        prolist!!.setLayoutManager(mLayoutManager)
        //recyclerView.adapter = ExtendedRecyclerAdapter(mActivity!!,pro_data,this,this)//SimpleStringRecyclerViewAdapter(recyclerView, pro_data)
        /*adp = TitledRecyclerAdapter(activity!!,pro_data,title,id,this,this)//SimpleStringRecyclerViewAdapter(recyclerView, pro_data)
        prolist.adapter = adp*/

        searchedit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, star: Int, before: Int, count: Int) {
                if (Appconstands.net_status(activity)) {
                    connection.visibility=View.GONE
                    if (searchedit.text.trim().length>3){
                        src = searchedit.text.trim().toString()
                        start=0
                        Products.clear()
                        if(froms.isEmpty()) {
                            getProducts(searchedit.text.trim().toString())
                        }
                        else if(froms=="home") {
                            getProducts_global(searchedit.text.trim().toString())
                        }
                    }
                }

                else{
                    connection.visibility=View.VISIBLE
                }

            }
        })

        back.setOnClickListener {
            finish()
        }
        //view_cart_lay()

        conbtn.setOnClickListener {
            if (Appconstands.net_status(activity)) {
                connection.visibility=View.GONE
                if (searchedit.text.trim().length>3){
                    src = searchedit.text.trim().toString()
                    start=0
                    Products.clear()
                    getProducts(searchedit.text.trim().toString())
                }
            }else{
                connection.visibility=View.VISIBLE
            }
        }

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

        if (position==0){
            startActivity(Intent(activity, OrderActivity::class.java))
            overridePendingTransition(0,0)

        }else{
            //if (location_shimmer.visibility==View.GONE) {
            val it = Intent(activity, ProfileActivity::class.java)
            //it.putExtra("name", catgs[position].content)
            //it.putExtra("id", catgs[position].id)
            //it.putExtra("lat", lat)
            //it.putExtra("lng", longi)
            startActivity(it)
            overridePendingTransition(0,0)

            //}
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
    fun getProducts(src:String){
        val typeid  = utils.gettypeid()
        val vendor_id  = utils.getvendor_id()
        //loading_show(activity, pDialog).show()
        println("type : "+typeid)
        println("vendor_id : "+vendor_id)
        if (start==0){
            product_shimmer.visibility = View.VISIBLE
            product_shimmer.startShimmer()
        }
        val call = ApproveUtils.Get.getSearchProducts(vendor_id.toString(),src,start.toString())
        call.enqueue(object : Callback<Resp> {
            override fun onResponse(call: Call<Resp>, response: Response<Resp>) {
                Log.e("$tag response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp
                    println(example)
                    if (example.status == "Success") {
                        val slid = response.body() as Resp
                        val res = slid.response!!
                        for (h in 0 until res.size){
                            val fr = AllCat()
                            fr.cid = res[h].id
                            fr.title = res[h].category
                            val details = res[h].details
                            val AllPro = ArrayList<TitledProduct>()
                            for (d in 0 until details!!.size){
                                val pro = details[d].pro
                                val sid = details[d].sid
                                val name = details[d].name
                                for (p in 0 until pro!!.size){
                                    val pid = pro[p].pid
                                    val pname = pro[p].pname
                                    val image = pro[p].image
                                    val option = pro[p].option
                                    val options = ArrayList<SpinnerPojo>()
                                    for (o in 0 until option!!.size){
                                        val sdata = SpinnerPojo()
                                        val price = option[o].price
                                        val opname = option[o].name
                                        sdata.id = o.toString()
                                        sdata.name = opname
                                        sdata.price = price
                                        options.add(sdata)
                                    }
                                    val dts = TitledProduct()
                                    dts.title = name
                                    dts.sid = sid
                                    dts.pid = pid
                                    dts.name = pname
                                    dts.qty = "0"
                                    dts.nm = options[0].name
                                    dts.price = options[0].price
                                    dts.selpos = 0
                                    dts.img =image
                                    dts.options = options
                                    AllPro.add(dts)
                                }
                            }
                            if (start==0) {
                                product_shimmer.visibility = View.GONE
                                product_shimmer.stopShimmer()
                            }
                            fr.pros = AllPro
                            Products.add(fr)
                            adp = TitledRecyclerAdapter("ProductSearchActivity",activity!!,AllPro,res[h].category.toString(),res[h].id.toString(),activity,activity)//SimpleStringRecyclerViewAdapter(recyclerView, pro_data)
                            prolist.adapter = adp
                        }
                    }
                }
                //pDialog.dismiss()
                //product_layout.visibility = View.VISIBLE
                if (start==0) {
                    product_shimmer.visibility = View.GONE
                    product_shimmer.stopShimmer()
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
                //pDialog.dismiss()
                //loading_show(activity).dismiss()
                //product_layout.visibility = View.VISIBLE
                if (start==0) {
                    product_shimmer.visibility = View.GONE
                    product_shimmer.stopShimmer()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        selecttab1(1)

        registerNetworkBroadcastReceiver(this)
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

            //https://virtuewin.com/v3/#/reward-history
            //https://virtuewin.com/v3/#/online-training
            //https://virtuewin.com/v3/#/video








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

    fun getProducts_global(src:String){
        val typeid  = utils.gettypeid()
        val vendor_id  = utils.getvendor_id()
        //loading_show(activity, pDialog).show()
        println("type : "+typeid)
        println("vendor_id : "+vendor_id)
        if (start==0){
            product_shimmer.visibility = View.VISIBLE
            product_shimmer.startShimmer()
        }
        val call = ApproveUtils.Get.getSearchProducts("",src,start.toString())
        call.enqueue(object : Callback<Resp> {
            override fun onResponse(call: Call<Resp>, response: Response<Resp>) {
                Log.e("$tag response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp
                    println(example)
                    if (example.status == "Success") {
                        val slid = response.body() as Resp
                        val res = slid.response!!
                        for (h in 0 until res.size){
                            val fr = AllCat()
                            fr.cid = res[h].id
                            fr.title = res[h].category
                            val details = res[h].details
                            val AllPro = ArrayList<TitledProduct>()
                            for (d in 0 until details!!.size){
                                val pro = details[d].pro
                                val sid = details[d].sid
                                val name = details[d].name
                                for (p in 0 until pro!!.size){
                                    val pid = pro[p].pid
                                    val pname = pro[p].pname
                                    val image = pro[p].image
                                    val option = pro[p].option
                                    val options = ArrayList<SpinnerPojo>()
                                    for (o in 0 until option!!.size){
                                        val sdata = SpinnerPojo()
                                        val price = option[o].price
                                        val opname = option[o].name
                                        sdata.id = o.toString()
                                        sdata.name = opname
                                        sdata.price = price
                                        options.add(sdata)
                                    }
                                    val dts = TitledProduct()
                                    dts.title = name
                                    dts.sid = sid
                                    dts.pid = pid
                                    dts.name = pname
                                    dts.qty = "0"
                                    dts.nm = options[0].name
                                    dts.price = options[0].price
                                    dts.selpos = 0
                                    dts.img =image
                                    dts.options = options
                                    AllPro.add(dts)
                                }
                            }
                            if (start==0) {
                                product_shimmer.visibility = View.GONE
                                product_shimmer.stopShimmer()
                            }
                            fr.pros = AllPro
                            Products.add(fr)
                            adp = TitledRecyclerAdapter("ProductSearchActivity",activity!!,AllPro,res[h].category.toString(),res[h].id.toString(),activity,activity)//SimpleStringRecyclerViewAdapter(recyclerView, pro_data)
                            prolist.adapter = adp
                        }
                    }
                }
                if (start==0) {
                    product_shimmer.visibility = View.GONE
                    product_shimmer.stopShimmer()
                }
                //pDialog.dismiss()
                //product_layout.visibility = View.VISIBLE

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
                //pDialog.dismiss()
                //loading_show(activity).dismiss()
                //product_layout.visibility = View.VISIBLE
                if (start==0) {
                    product_shimmer.visibility = View.GONE
                    product_shimmer.stopShimmer()
                }
            }
        })
    }

    fun view_cart_lay(){
        val tot = db.cartTotal.toString()
        println("db.cartTotal : "+db.cartTotal)
        if (db.cartTotal!=0.toDouble()/*&&(db.vendor_id==utils.getvendor_id())*/) {
            val vendor_nm = db.vendor_nm
            val items = db.cartItem.toString()
            cart_layout.visibility = View.VISIBLE
            tot_items.setText(items+" items - ")
            tot_price.setText("₹ "+tot)
            shop.setText("From : "+vendor_nm)
            continue_cart.setOnClickListener {
                startActivity(Intent(activity, CartActivity::class.java))
            }
        }else{
            cart_layout.visibility = View.GONE
        }

    }

    override fun networkAvailable() {
    }

    override fun networkUnavailable() {
        val i=Intent(this, NetActivity::class.java)
        startActivity(i)
    }
}
