package com.elanciers.vasantham_stores_ecomm

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.elanciers.adport.Adapter.CategoriesAdapter
import com.elanciers.vasantham_stores_ecomm.Adapter.TabAdapter
import com.elanciers.vasantham_stores_ecomm.Common.*
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands.RequestPermissionCode
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands.net_status
import com.elanciers.vasantham_stores_ecomm.DataClass.CheckCardData
import com.elanciers.vasantham_stores_ecomm.DataClass.ImageScroll
import com.elanciers.vasantham_stores_ecomm.DataClass.LoyaltyPoints
import com.elanciers.vasantham_stores_ecomm.retrofit.ApproveUtils
import com.elanciers.vasantham_stores_ecomm.retrofit.Resp
import com.elanciers.vasantham_stores_ecomm.retrofit.Respval
import com.elanciers.vasantham_stores_ecomm.retrofit.RetrofitClient2
import com.google.android.gms.location.LocationRequest
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*
import kotlin.math.roundToInt


class HomeActivity : AppCompatActivity()/*, LocationListener*/ {
var permissionCode = 1000;

    val tag = "Home"
    val activity = this
    lateinit var utils : Utils
    lateinit var pDialog: Dialog
    lateinit var db: DBController

    private var hasNextPage: Boolean = true
    val TAG: String = activity::class.java.name
    val mFragmentTitleList1 = ArrayList<String>()
    val mFragmentTitleList2 = ArrayList<String>()
    val mFragmentIconList1 = ArrayList<Int>()
    val mFragmentIconList2 = ArrayList<Int>()
    protected val CONTENT2 = arrayOf(
        "Pager Carousel",
        "Title 2",
        "Title 3",
        "Title 4",
        "Title 5",
        "Title 6",
        "Title 7",
        "Title 8",
        "Title 9"
    )
    val catgs = ArrayList<ImageScroll>()
    var pinpop=""
    var locationmanager: LocationManager? = null
    var lat =  9.939093//: Double = 0.toDouble()
    var longi  = 78.121719//:Double = 0.toDouble()
    var locationrequest : LocationRequest? = null ;
    val poparr=ArrayList<String>()
    fun getLocation() {
        try {
            println("locat")
            locationmanager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {

                return
            }
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_FINE
            //locationmanager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5f, activity)
            locationmanager!!.requestSingleUpdate(
                criteria,
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        println("singlelocation : " + location.latitude + " , " + location.longitude)
                        location_shimmer.stopShimmer()
                        location_shimmer.visibility = View.GONE
                        location_layout.visibility = View.VISIBLE
                        getCompleteAddressString(location)
                    }

                    override fun onStatusChanged(
                        provider: String,
                        status: Int,
                        extras: Bundle
                    ) {
                    }

                    override fun onProviderEnabled(provider: String) {}
                    override fun onProviderDisabled(provider: String) {}
                },
                null
            )
            // Log.e(“Network”, “Network”);
            if (locationmanager != null) {
                val location = locationmanager!!
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (location != null) {
                    println("lastknown : " + location.latitude + " , " + location.longitude)
                    location_shimmer.stopShimmer()
                    location_shimmer.visibility=View.GONE
                    location_layout.visibility=View.VISIBLE
                    getCompleteAddressString(location)
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            println("error : " + e.printStackTrace())
        }
        /*SingleShotLocationProvider.requestSingleUpdate(activity,object : SingleShotLocationProvider.LocationCallback {
             override fun onNewLocationAvailable(location: Location?) {
               Log.d("Location", "my location is " + location.toString());
                 location_shimmer.stopShimmer()
                 location_shimmer.visibility=View.GONE
                 location_layout.visibility=View.VISIBLE
                 getCompleteAddressString(location!!)
             }
        })*/

    }
var locationManager: LocationManager? = null;
    var mContext: Context? = null;
    var fList = ArrayList<Fragment>()
    lateinit var sliderAdp : MyPageAdapter
    lateinit var CategAdp : CategoriesAdapter
    var isContinue = false;
    var isGPS = false;
    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 600 //delay in milliseconds before task is to be executed
    val PERIOD_MS: Long = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        changeStatusBarColor()
        db = DBController(activity)
        utils = Utils(activity)
        pDialog = Dialog(activity)
        mContext=this

        lang()
        name.setText(utils.name().toString())
        mob.setText(utils.mobile().toString())
        //DBController(activity).dropHoleCart()
        //DBController(activity).dropLocation()
        mFragmentTitleList1.add(AppUtil.languageString("Home").toString())
        mFragmentTitleList1.add(AppUtil.languageString("Search").toString())
        mFragmentTitleList2.add(AppUtil.languageString("Orders").toString())
        mFragmentTitleList2.add(AppUtil.languageString("Profile").toString())

        mFragmentIconList1.add(R.drawable.ic_home)
        mFragmentIconList1.add(R.drawable.ic_search)
        mFragmentIconList2.add(R.drawable.ic_shopping_bag)
        mFragmentIconList2.add(R.drawable.ic_baseline_account_balck_24px)


        location_shimmer.visibility=View.GONE
        location_layout.visibility=View.VISIBLE
        location_shimmer.stopShimmer()

        /*if (CheckingPermissionIsEnabledOrNot()){
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED) {
               // val mLocationManager =  getSystemService(LOCATION_SERVICE) as (LocationManager);
               // mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5f, this);
                locationManager = mContext!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                locationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    2000,
                    10F,
                    locationListenerGPS
                )


            }


            statusCheck()
            //getLocation()
        }else{
            RequestMultiplePermission()
        }*/
        sliderAdp = MyPageAdapter(supportFragmentManager, fList)
        viewPager.setAdapter(sliderAdp)
        //viewPager.setPageTransformer(false, CustPageTransformer(activity));



        /*val handler = Handler()
        val Update = Runnable {
            if (currentPage == fList.size - 1) {
                currentPage = 0
            }
            viewPager.setCurrentItem(currentPage++, true)
        }

        timer = Timer() // This will create a new Thread

        timer!!.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS, PERIOD_MS)*/

        //getLocation()
        highLightCurrentTab()
        selecttab1(0)

        tabImageViews.setOnClickListener {
            startActivity(Intent(activity, Signin_Due_Activity::class.java))
            /*val st = Intent(this,Dashboard::class.java)
            st.putExtra("cardno","20220018")
            startActivity(st)*/
        }

        if (net_status(activity)){
            getSlider()
            //getCategory()
            version()
            getpopup()
        }
        else{
            val toast=Toast.makeText(applicationContext,"No internet connection",Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER,0,0)
            toast.show()

        }

        points_lay.setOnClickListener {
            startActivity(Intent(this@HomeActivity, PointsListActivity::class.java))
        }

        var data1 = ImageScroll()
        data1.id ="1"
        data1.img = "https://conviviumosteria.com/wp-content/uploads/2019/06/best-italian-restaurant-brooklyn-nyc.jpg"
        data1.content = "Osteria Italian Restaurant Brooklyn"
        //catgs.add(data1)
        data1 = ImageScroll()
        data1.id ="2"
        data1.img = "https://rudymaxasworld.com/wp-content/uploads/2016/09/Restaurant-22.jpg"
        data1.content = "Rudy Maxa's World"
        //catgs.add(data1)
        data1 = ImageScroll()
        data1.id ="3"
        data1.img = "https://media.gq-magazine.co.uk/photos/5d13a96b7fcc8e403c821131/16:9/w_1920,c_limit/02-gq-19   mar19_b.jpg"
        data1.content = "London British GQ"


        val adap = TabAdapter(getSupportFragmentManager(), activity)
        val imgs = arrayListOf<Int>()

        /*swipeToRefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                //shuffle()
                finish()
                startActivity(Intent(this@HomeActivity, HomeActivity::class.java))
                swipeToRefresh.setRefreshing(false)
            }
        })*/

        viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })


        profile.setOnClickListener {
            /*val uri = Uri.parse("smsto:6374934371")
            val i = Intent(Intent.ACTION_SENDTO, uri)
            i.putExtra("sms_body", "Any Queries: ")
            i.setPackage("com.whatsapp")
            startActivity(i)*/
            val popupMenu = PopupMenu(this, it)
            popupMenu.menuInflater.inflate(R.menu.language_menu, popupMenu.menu)
            val menu: Menu = popupMenu.getMenu()
            for (i in 0 until menu.size()) {
                val item = menu.getItem(i)
                val s = SpannableString(item.title)
                s.setSpan(ForegroundColorSpan(Color.BLACK), 0, s.length, 0)
                item.title = s
            }
            popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(menuItem: MenuItem): Boolean {
                    // Toast message on menu item clicked
                    popupMenu.dismiss()
                    profile.setText(if (menuItem.title.toString().equals("English")) "En" else "த")
                    if (menuItem.title.toString().equals("English")) {
                        AppController.setLanguageobj( AppController.getEnLanguageobj())
                        utils.savePreferences("language","English")
                    } else {
                        AppController.setLanguageobj( AppController.getTaLanguageobj())
                        utils.savePreferences("language","Tamil")
                    }
                    recreate()
                    return true
                }
            })
            // Showing the popup menu
            // Showing the popup menu
            popupMenu.show()
        }

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                highLightCurrentTab()
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
                highLightCurrentTab()
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

        var data = ImageScroll()
        data.id ="1"
        data.img = ""
        data.content = "Send Packages"
        data.product = R.mipmap.send_package
        //catgs.add(data)
        data = ImageScroll()
        data.id ="2"
        data.img = ""
        data.content = "Resturants"
        data.product =R.mipmap.resturant
        //catgs.add(data)
        data = ImageScroll()
        data.id ="3"
        data.img = ""
        data.content = "Daily Grocery"
        data.product = R.mipmap.daily_grocery
        //catgs.add(data)
        data = ImageScroll()
        data.id ="4"
        data.img = ""
        data.content = "Fruits and Vegetables"
        data.product =R.mipmap.fruits_veg
        //catgs.add(data)
        data = ImageScroll()
        data.id ="5"
        data.img = ""
        data.content = "Medicine"
        data.product = R.mipmap.medi
        //catgs.add(data)
        data = ImageScroll()
        data.id ="6"
        data.img = ""
        data.content = "Meat and Fish"
        data.product = R.mipmap.meat_fish
        //catgs.add(data)
        data = ImageScroll()
        data.id ="7"
        data.img = ""
        data.content = "Healthy and Wellness"
        data.product = R.mipmap.muscle
        //catgs.add(data)
        data = ImageScroll()
        data.id ="8"
        data.img = ""
        data.content = "Gifts and Lifestyle"
        data.product =R.mipmap.gift
        //catgs.add(data)
        data = ImageScroll()
        data.id ="9"
        data.img = ""
        data.content = "Pet Supplies"
        data.product = R.mipmap.pet
        //catgs.add(data)
        data = ImageScroll()
        data.id ="10"
        data.img = ""
        data.content = "Any Store in City"
        data.product =R.mipmap.all_store
        //catgs.add(data)


        /* val mLayoutManager2 = LinearLayoutManager(activity!!, LinearLayoutManager.HORIZONTAL, false)
         cates.setLayoutManager(mLayoutManager2)*/
        CategAdp = CategoriesAdapter(activity, R.layout.category_list_adapter, catgs)
        cates.adapter = CategAdp
        cates.isExpanded = true

        cates.setOnItemClickListener { parent, view, position, id ->
            if (location_shimmer.visibility==View.GONE) {

                if(catgs[position].timing!!.isEmpty()) {
                    val it = Intent(activity, ProductActivity::class.java)
                    it.putExtra("name", catgs[position].content)
                    it.putExtra("typeid", catgs[position].id)
                    it.putExtra("lat", lat)
                    it.putExtra("lng", longi)
                    utils.setVendor(
                        "1",
                        catgs[position].id.toString(),
                        "",
                        catgs[position].img.toString()
                    )
                    startActivity(it)
                }
                else if(catgs[position].timing!!.isNotEmpty()&&catgs[position].time=="false"){
                    val alert=AlertDialog.Builder(this)
                    alert.setTitle("Closed")
                    alert.setMessage("Available time for this category " + catgs[position].timing)
                    alert.setPositiveButton("ok", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog!!.dismiss()

                        }

                    })
                    val pop=alert.create()
                    pop.show()
                }


            }
        }
        catall.setOnClickListener {
            if (location_shimmer.visibility==View.GONE) {
                val it = Intent(activity, CategoriesActivity::class.java)
                it.putExtra("lat", lat)
                it.putExtra("lng", longi)
                startActivity(it)
            }
        }

        /*profile.setOnClickListener {
            startActivity(Intent(activity, ProfileActivity::class.java))
        }*/

        /*location.setOnClickListener {
            val its =Intent(activity, AddressMapActivity::class.java)
            its.putExtra("id","")
            startActivity(its)
        }*/


    }

    override fun onResume() {
        super.onResume()
        highLightCurrentTab()
        selecttab1(0)
        cartitem()
        getLoyaltypoints()
        if (!utils.login()){
            finish()
        }
    }

    fun pinpop(){

        poparr.clear()
        poparr.add("Select")

        if(pinpop.isEmpty()) {
            val alert = AlertDialog.Builder(activity)
            val v = layoutInflater.inflate(R.layout.pinpode_popup, null)
            alert.setView(v)
            alert.setCancelable(false)
            val po = alert.create()
            val yay = v.findViewById<TextView>(R.id.spinner) as Spinner
            val code = v.findViewById<TextView>(R.id.textView40) as TextView
            // val discn = v.findViewById<TextView>(R.id.discnt) as TextView
            // val dis_disc = v.findViewById<TextView>(R.id.dis_disc) as TextView
            poparr.add("625001")
            poparr.add("625002")
            poparr.add("625010")
            poparr.add("625016")
            poparr.add("625020")
            val adap = ArrayAdapter(
                this@HomeActivity,
                R.layout.support_simple_spinner_dropdown_item,
                poparr
            )
            yay.adapter = adap

            yay.setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    positions: Int,
                    id: Long
                ) {
                    if(positions!=0) {
                        po.dismiss()
                        pinpop = "true";


                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            })


            //couponcan.setImageDrawable(resources.getDrawable(R.drawable.ic_dialog_close_light))
            code.setOnClickListener {
                po.dismiss()
            }
            po.show()
        }

    }

    var locationListenerGPS: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: android.location.Location) {
            val latitude = location.latitude
            val longitude = location.longitude
            val msg = "New Latitude: " + latitude + "New Longitude: " + longitude
            //Toast.makeText(this@HomeActivity, msg, Toast.LENGTH_LONG).show()
            location_shimmer.stopShimmer()
            location_shimmer.visibility=View.GONE
            location_layout.visibility=View.VISIBLE
            getCompleteAddressString(location)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

        }

        override fun onProviderEnabled(provider: String) {

        }

        override fun onProviderDisabled(provider: String) {

        }
    }

    fun cartitem(){
        val tot = db.cartTotal.toString()
        println("db.cartTotal : " + db.cartTotal)
        if (db.cartTotal!=0.toDouble()){
            cartitem_layout.visibility=View.VISIBLE
            val vnm = db.vendor_nm
            val vimg = db.vendor_img
            shop_nm.setText(db.CartList().size.toString() + " item(s)")
            if (vimg.isNotEmpty()){
                Glide.with(activity).load(Appconstands.ImageDomain + vimg).error(R.mipmap.banner).into(
                    shop_img
                )
            }
            cancel_cart.setOnClickListener {
                val alertDialog = AlertDialog.Builder(activity)
                alertDialog.setCancelable(true)
                alertDialog.setMessage("Discard all the items in your cart?")
                alertDialog.setPositiveButton("discard", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, p1: Int) {
                        dialog!!.dismiss()
                        db.dropHoleCart()
                        cartitem()
                    }

                })
                alertDialog.setNegativeButton("cancel", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, p1: Int) {
                        dialog!!.dismiss()
                    }

                })
                alertDialog.show()
            }
        }else{
            cartitem_layout.visibility=View.GONE
        }
        /*if (db.cartTotal!=0.toDouble()*//*&&(db.vendor_id==utils.getvendor_id())*//*) {
            val vendor_nm = db.vendor_nm
            val items = db.cartItem.toString()
            cart_item_layout.visibility = View.VISIBLE
            shop_img
            shop_nm.setText(vendor_nm)
            *//*tot_items.setText(items+" items - ")
            tot_price.setText("₹ "+tot)
            shop.setText("From : "+vendor_nm)*//*
            view_cart.setOnClickListener {
                startActivity(Intent(activity, CartActivity::class.java))
            }
            cancel_cart.setOnClickListener{

            }
        }else{
            cart_item_layout.visibility = View.GONE
        }*/
    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.statusbar)
        }
    }

    fun OpenCart(view: View){
        startActivity(Intent(activity, CartActivity::class.java))
    }

    private fun highLightCurrentTab() {




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

        if (position==1){
            if (location_shimmer.visibility==View.GONE) {
                val it = Intent(activity, ProductSearchActivity::class.java)
                //it.putExtra("name", catgs[position].content)
                //it.putExtra("id", catgs[position].id)
                it.putExtra("frm", "home")

                startActivity(it)
            }
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
        }else{
            //if (location_shimmer.visibility==View.GONE) {
                val it = Intent(activity, ProfileActivity::class.java)
                //it.putExtra("name", catgs[position].content)
                //it.putExtra("id", catgs[position].id)
                it.putExtra("lat", lat)
                it.putExtra("lng", longi)
                startActivity(it)
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

    inner class MyPageAdapter(fm: FragmentManager, private val fragments: List<Fragment>) : FragmentPagerAdapter(
        fm
    ) {

        override fun getItem(position: Int): Fragment {
            return this.fragments[position]
        }

        override fun getCount(): Int {
            return this.fragments.size
        }
    }

    fun getCompleteAddressString(locat: Location) {
        lat = locat.getLatitude()
        longi = locat.getLongitude()
        val geocoder = Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            val listAddresses = geocoder.getFromLocation(
                locat.getLatitude(),
                locat.getLongitude(),
                1
            );
            println("listAddresses : " + listAddresses)
            if(null!=listAddresses&&listAddresses.size>0){
                val area = listAddresses.get(0).getAddressLine(0).toString().split(",")[1]
                val city = listAddresses.get(0).locality
                println("adminArea : " + listAddresses.get(0).adminArea)
                println("subAdminArea : " + listAddresses.get(0).subAdminArea)
                println("subLocality : " + listAddresses.get(0).subLocality)
                println("area : " + area)
                println("city : " + city)
                //location.setText(city)
                /*ab!!.title=city.trimStart()
                ab!!.subtitle =area.trimStart()*/
            }
        } catch (e: IOException) {
            e.printStackTrace();
        }
    }

    fun CheckingPermissionIsEnabledOrNot(): Boolean {

        val INTERNET = ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET)
        val ACCESS_NETWORK_STATE = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_NETWORK_STATE
        )
        //val ACCESS_NOTIFICATION_POLICY = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NOTIFICATION_POLICY)
        val ACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        val ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val CALL_PHONE = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CALL_PHONE
        )

        return INTERNET == PackageManager.PERMISSION_GRANTED &&
                ACCESS_NETWORK_STATE == PackageManager.PERMISSION_GRANTED &&
                //ACCESS_NOTIFICATION_POLICY == PackageManager.PERMISSION_GRANTED &&
                ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED &&
                ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED //&&
                //CALL_PHONE == PackageManager.PERMISSION_GRANTED
    }
    fun RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(
            this, arrayOf<String>
                (
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION//,
                //android.Manifest.permission.CALL_PHONE
            ), RequestPermissionCode
        )


    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {

            RequestPermissionCode ->

                if (grantResults.size > 0) {

                    val INTERNET = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val ACCESS_NETWORK_STATE = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    //val ACCESS_NOTIFICATION_POLICY = grantResults[2] == PackageManager.PERMISSION_GRANTED
                    val ACCESS_FINE_LOCATION = grantResults[2] == PackageManager.PERMISSION_GRANTED
                    val ACCESS_COARSE_LOCATION =
                        grantResults[3] == PackageManager.PERMISSION_GRANTED
                    //val CALL_PHONE = grantResults[4] == PackageManager.PERMISSION_GRANTED

                    if (INTERNET && ACCESS_NETWORK_STATE /*&& ACCESS_NOTIFICATION_POLICY*/ && ACCESS_FINE_LOCATION && ACCESS_COARSE_LOCATION /*&&CALL_PHONE*/) {
                        if (ContextCompat.checkSelfPermission(
                                this,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            /*val mLocationManager =  getSystemService(LOCATION_SERVICE) as (LocationManager);
                            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this);*/
                            getLocation()
                        }
                        //Toast.makeText(this@MainFirstActivity, "Permission Granted", Toast.LENGTH_LONG).show()
                    } else {
                        //Toast.makeText(this@MainFirstActivity, "Permission Denied", Toast.LENGTH_LONG).show()

                    }
                }

        }
    }

    fun getSlider(){
        //loading_show(activity, pDialog).show()
        //scroll2_slider.removeAllSliders()
        slider_layout.visibility = View.GONE
        slider_shimmer.visibility = View.VISIBLE
        slider_shimmer.startShimmer()
        val call = ApproveUtils.Get.getSlider("1")
        call.enqueue(object : Callback<Resp> {
            override fun onResponse(call: Call<Resp>, response: Response<Resp>) {
                Log.e("$tag response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp
                    println(example)
                    if (example.status == "Success") {
                        val slid = response.body() as Resp
                        val res = slid.response!!
                        for (h in 0 until res.size) {
                            val data = ImageScroll()
                            data.id = res[h].id
                            data.img = res[h].image
                            data.content = res[h].name
                            /*fList.add(MyFragment.newInstance("", data))
                            sliderAdp.notifyDataSetChanged()

                            try {
                                viewPager.setCurrentItem(0)
                            } catch (e: Exception) {
                            }*/

                            val textSliderView = CustomCardView(activity,
                                object : CustomCardView.onSliderClick {
                                    override fun OnSliderClick(view: View) {
                                        println("banners[nam].id : " + res[h].id)
                                        if (!res[h].id.isNullOrEmpty()) {
                                           /* val z =
                                                Intent(activity, ProductDetailActivity::class.java)
                                            z.putExtra("id", banners[nam].id)
                                            z.putExtra("edit", "")
                                            startActivity(z)*/
                                        }
                                    }
                                })
                            // initialize a SliderLayout
                            textSliderView
                                .description(res[h].name)
                                .image(Appconstands.ImageDomain + res[h].image)
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(object: BaseSliderView.OnSliderClickListener{
                                    override fun onSliderClick(slider: BaseSliderView?) {

                                    }
                                })
                            //add your extra information
                            textSliderView.bundle(Bundle())
                            textSliderView.getBundle()
                                .putString("id", res[h].id)

                            scroll2_slider.addSlider(textSliderView)
                        }
                        scroll2_slider.setPresetTransformer(SliderLayout.Transformer.Default)
                    }
                }
                //pDialog.dismiss()
                //Handler().postDelayed(Runnable{
                slider_layout.visibility = View.VISIBLE
                slider_shimmer.visibility = View.GONE
                slider_shimmer.stopShimmer()
                /*  val update = Dialog(this@HomeActivity)
                update.requestWindowFeature(Window.FEATURE_NO_TITLE)
                update.window.setBackgroundDrawable(
                    ColorDrawable(Color.WHITE)
                )
                val vs: View =
                    layoutInflater.inflate(R.layout.imgae_popup, null)
                val updatebut =
                    vs.findViewById<View>(R.id.button3) as TextView
                val text =
                    vs.findViewById<View>(R.id.textView21) as TextView
               // laterbut = vs.findViewById<View>(R.id.img) as ImageView
                //val titlename =
                  //  vs.findViewById<View>(R.id.titlename) as TextView
               // download = vs.findViewById<View>(R.id.download) as TextView
                update.setContentView(vs)
                val window = update.window
                window.setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                update.setCancelable(true)
                update.show()

                updatebut.setOnClickListener {
                    update.dismiss()
                }*/
                //},5000)
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
                slider_layout.visibility = View.VISIBLE
                slider_shimmer.visibility = View.GONE
                slider_shimmer.stopShimmer()
            }
        })
    }


    fun getpopup(){
        //loading_show(activity, pDialog).show()
        slider_layout.visibility = View.GONE
        slider_shimmer.visibility = View.VISIBLE
        slider_shimmer.startShimmer()
        val call = ApproveUtils.Get.getpop("1")
        call.enqueue(object : Callback<Respval> {
            override fun onResponse(call: Call<Respval>, response: Response<Respval>) {
                Log.e("$tag response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Respval
                    println(example)

                    try {
                        if (example.status == "Success") {
                            val slid = response.body() as Respval
                            val res = slid.response!!
                            if (res.toString().isNotEmpty()) {

                                val update = AlertDialog.Builder(this@HomeActivity)
                                /* update.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                update.window.setBackgroundDrawable(
                                    ColorDrawable(Color.WHITE)
                                )*/
                                val vs: View =
                                    layoutInflater.inflate(R.layout.imgae_popup, null)
                                val updatebut =
                                    vs.findViewById<View>(R.id.button3) as TextView
                                val img =
                                    vs.findViewById<View>(R.id.imageView22) as ImageView
                                val text =
                                    vs.findViewById<View>(R.id.textView21) as TextView
                                // laterbut = vs.findViewById<View>(R.id.img) as ImageView
                                //val titlename =
                                //  vs.findViewById<View>(R.id.titlename) as TextView
                                // download = vs.findViewById<View>(R.id.download) as TextView

                                text.setText(res.content.toString())
                                Glide.with(activity).load(Appconstands.ImageDomain + res.image)
                                    .into(
                                        img
                                    )
                                update.setView(vs)
                                /*  val window = update.window
                                window.setLayout(
                                    300,
                                    WindowManager.LayoutParams.WRAP_CONTENT
                                )*/

                                update.setCancelable(false)
                                val pop = update.create()
                                pop.show()

                                updatebut.setOnClickListener {
                                    pop.dismiss()
                                }

                                /*try {
                                    viewPager.setCurrentItem(1)
                                } catch (e: Exception) {
                                }*/
                            } else {

                            }
                        }
                    } catch (e: Exception) {

                    }
                }
                //pDialog.dismiss()
                //Handler().postDelayed(Runnable{
                slider_layout.visibility = View.VISIBLE
                slider_shimmer.visibility = View.GONE
                slider_shimmer.stopShimmer()

                //},5000)
            }

            override fun onFailure(call: Call<Respval>, t: Throwable) {
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
                slider_layout.visibility = View.VISIBLE
                slider_shimmer.visibility = View.GONE
                slider_shimmer.stopShimmer()
            }
        })
    }

    fun getCategory(){
        //loading_show(activity, pDialog).show()
        category_layout.visibility = View.GONE
        category_shimmer.visibility = View.VISIBLE
        category_shimmer.startShimmer()
        val call = ApproveUtils.Get.getCategory("1")
        call.enqueue(object : Callback<Resp> {
            override fun onResponse(call: Call<Resp>, response: Response<Resp>) {
                Log.e("$tag response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp
                    println(example)
                    if (example.status == "Success") {
                        val slid = response.body() as Resp
                        val res = slid.response!!
                        for (h in 0 until res.size) {
                            if (h < 13) {
                                val data = ImageScroll()
                                data.id = res[h].id
                                data.img = res[h].image
                                data.content = res[h].name
                                data.time = res[h].time

                                if (data.time == "true") {
                                    data.timing = ""
                                } else if (data.time == "false") {
                                    data.timing = res[h].start_time + " - " + res[h].end_time

                                }
                                data.product = R.mipmap.all_store
                                catgs.add(data)
                                CategAdp.notifyDataSetChanged()
                            }
                        }
                    }
                }
                //pDialog.dismiss()
                category_layout.visibility = View.VISIBLE
                category_shimmer.visibility = View.GONE
                category_shimmer.stopShimmer()
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
                category_layout.visibility = View.VISIBLE
                category_shimmer.visibility = View.GONE
                category_shimmer.stopShimmer()
            }
        })
    }

    fun statusCheck() {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        println("net : " + manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        println("gps : " + manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)&&!manager.isProviderEnabled(
                LocationManager.GPS_PROVIDER
            ))
        {
            println("perform")
            GpsUtils(this).turnGPSOn(object : GpsUtils.onGpsListener {
                override fun gpsStatus(isGPSEnable: Boolean) {
                    // turn on GPS
                    isGPS = isGPSEnable
                    println("network & gps else " + isGPSEnable)
                    getLocation()
                }
            })
            /*val request = LocationRequest()
                .setFastestInterval(1500)
                .setInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            val settingsRequest = LocationSettingsRequest.Builder()
            settingsRequest.addLocationRequest(request)
            val client = LocationServices.getSettingsClient(activity)
            val responseTask = client.checkLocationSettings(settingsRequest.build())
            responseTask.addOnSuccessListener(activity, { locationSettingsResponse-> locationScheduler() })
            responseTask.addOnFailureListener(activity, { e->
                val statusCode = (e as ApiException).getStatusCode()
                when (statusCode) {
                    CommonStatusCodes.RESOLUTION_REQUIRED -> try
                    {
                        val apiException = (e as ResolvableApiException)
                        apiException.startResolutionForResult(LocationSettingsStatusCodes, permissionCode)
                        Log.d(TAG, "Dialog displayed")
                    }
                    catch (sendIntentException:IntentSender.SendIntentException) {
                        Log.d(TAG, "Error displaying dialogBox", sendIntentException)
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.d(TAG, "Unable to turn on location service", e)
                } })*/
            //buildAlertMessageNoGps()
            //if (googleApiClient == null)
            //{
                /*val googleApiClient = GoogleApiClient.Builder(getApplicationContext()).addApi(
                    LocationServices.API).build()
                googleApiClient.connect()
                val locationRequest = LocationRequest.create()
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                locationRequest.setInterval(5 * 1000)
                //locationRequest.setFastestInterval(5 * 1000)
                val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
                // **************************
                builder.setAlwaysShow(true) // this is the key ingredient
                // **************************
                val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
                result.setResultCallback(object: ResultCallback<LocationSettingsResult>
                {
                    override fun onResult(result: LocationSettingsResult)
                    {
                        val status = result.getStatus()
                        val state = result.getLocationSettingsStates()
                        when (status.getStatusCode()) {
                            LocationSettingsStatusCodes.SUCCESS -> {
                                getLocation()
                            }
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->{}
                                *//*try
                                {
                                    status.startResolutionForResult(activity, 1000)
                                }
                                catch (e: IntentSender.SendIntentException)
                                {
                                }*//*
                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                        }
                    }
                })*/
            //}
        }else{
            println("else : ")

            if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            {
                getLocation()
                //buildAlertMessageNoGps()
                //if (googleApiClient == null)
                //{
                /*val googleApiClient = GoogleApiClient.Builder(getApplicationContext()).addApi(
                    LocationServices.API).build()
                googleApiClient.connect()
                val locationRequest = LocationRequest.create()
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                locationRequest.setInterval(5 * 1000)
                //locationRequest.setFastestInterval(5 * 1000)
                val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
                // **************************
                builder.setAlwaysShow(true) // this is the key ingredient
                // **************************
                val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
                result.setResultCallback(object: ResultCallback<LocationSettingsResult>
                {
                    override fun onResult(result: LocationSettingsResult)
                    {
                        val status = result.getStatus()
                        val state = result.getLocationSettingsStates()
                        when (status.getStatusCode()) {
                            LocationSettingsStatusCodes.SUCCESS -> {
                                getLocation()
                            }
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->{}
                            *//*try
                            {
                                status.startResolutionForResult(activity, 1000)
                            }
                            catch (e: IntentSender.SendIntentException)
                            {
                            }*//*
                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                        }
                    }
                })*/
                //}
            }else if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                getLocation()
                //buildAlertMessageNoGps()
                //if (googleApiClient == null)
                //{
                /*val googleApiClient = GoogleApiClient.Builder(getApplicationContext()).addApi(
                    LocationServices.API).build()
                googleApiClient.connect()
                val locationRequest = LocationRequest.create()
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                locationRequest.setInterval(5 * 1000)
                //locationRequest.setFastestInterval(5 * 1000)
                val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
                // **************************
                builder.setAlwaysShow(true) // this is the key ingredient
                // **************************
                val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
                result.setResultCallback(object: ResultCallback<LocationSettingsResult>
                {
                    override fun onResult(result: LocationSettingsResult)
                    {
                        val status = result.getStatus()
                        val state = result.getLocationSettingsStates()
                        when (status.getStatusCode()) {
                            LocationSettingsStatusCodes.SUCCESS -> {
                                getLocation()
                            }
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->{}
                            *//*try
                            {
                                status.startResolutionForResult(activity, 1000)
                            }
                            catch (e: IntentSender.SendIntentException)
                            {
                            }*//*
                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                        }
                    }
                })*/
                //}
            }else {
                GpsUtils(this).turnGPSOn(object : GpsUtils.onGpsListener {
                    override fun gpsStatus(isGPSEnable: Boolean) {
                        // turn on GPS
                        isGPS = isGPSEnable
                        println("network else " + isGPSEnable)
                        getLocation()
                    }
                })
            }
        }
    }
    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id: Int) {
                        startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                })
                .setNegativeButton("No", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id: Int) {
                        dialog.cancel()
                    }
                })
        val alert = builder.create()
        alert.show()
    }

    fun version(){
        Appconstands.maintenance(activity)
        val call = ApproveUtils.Get.Version()
        call.enqueue(object : Callback<Resp> {
            override fun onResponse(call: Call<Resp>, response: Response<Resp>) {
                Log.e("$tag response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as Resp
                    println(example)
                    if (example.status == "Success") {
                        //Toast.makeText(activity, example.message!!, Toast.LENGTH_LONG).show()
                        //val version = Build.VERSION.SDK_INT
                        val version2 = BuildConfig.VERSION_CODE
                        //println("version : "+version)
                        println("example.message!! : " + example.message!!)
                        println("version2 : " + version2)

                        if (example.message!!.toString().toInt() > version2.toString().toInt()) {
                            val alertDialog = AlertDialog.Builder(activity)
                            alertDialog.setCancelable(true)
                            alertDialog.setTitle("Update Available")
                            alertDialog.setMessage("You are using older version of vasantham stores. Click to update new version and get more features also.")
                            alertDialog.setPositiveButton("Update",
                                object : DialogInterface.OnClickListener {
                                    override fun onClick(dialog: DialogInterface?, p1: Int) {
                                        dialog!!.dismiss()
                                        val i = Intent(android.content.Intent.ACTION_VIEW)
                                        i.data =
                                            Uri.parse("https://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID)
                                        startActivity(i)
                                    }

                                })
                            alertDialog.setNegativeButton("cancel",
                                object : DialogInterface.OnClickListener {
                                    override fun onClick(dialog: DialogInterface?, p1: Int) {
                                        dialog!!.dismiss()
                                    }

                                })
                            alertDialog.show()
                        }
                    }
                }
                pDialog.dismiss()
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
                pDialog.dismiss()
            }
        })
    }
    fun lang(){
        profile.setText(if (utils.language=="Tamil") "த" else "En")
        textView9.setText(AppUtil.languageString("home"))
        loyalty_txt.setText(AppUtil.languageString("loyalty_points"))
        points_txt.setText(AppUtil.languageString("points"))
        shopby_txt.setText(AppUtil.languageString("shopbycat"))
        catall.setText(AppUtil.languageString("see_all"))
    }

    fun getLoyaltypoints(){
        //pDialog.show()
        val obj = JsonObject()
        obj.addProperty("mobile", utils.mobile())//"9042215989")//
        Log.d(tag, obj.toString())
        val call = RetrofitClient2.Get.getLoyaltypoints("http://vasanthamhypermart.in/api/loyaltybycustomer",obj)
        call.enqueue(object : Callback<LoyaltyPoints> {
            override fun onResponse(call: Call<LoyaltyPoints>, response: Response<LoyaltyPoints>) {
                Log.e("$tag response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as LoyaltyPoints
                        val data = Gson().toJson(example, LoyaltyPoints::class.java).toString()
                        println("data : "+data)
                    if (example.status == "success") {
                        val res = example.response!!
                        textView61.setText(example.plan.toString())

                        if (example.range_end.toString()!="0") {
                            progressBar2.max = Integer.parseInt(example.range_end.toString())
                            textView62.setText(example.range_start+"/"+example.range_end)
                            if (!example.response.isNullOrEmpty()){
                                //val per=(example.response.toString().toFloat().toInt()/example.range_end.toString().toInt())*100
                                progressBar2.progress = example.response.toString().toFloat().toInt()// per
                                //println("per : "+per)
                            }
                        }else{
                            progressBar2.max = Integer.parseInt(example.range_start.toString())
                            textView62.setText(example.range_start+"/above")
                            if (!example.response.isNullOrEmpty()){
                                //val re=example.response.toString().toDouble().toInt()
                                //val en =example.range_start.toString().toDouble().roundToInt()
                                val per = example.response.toString().toFloat().toInt()//(re/en)*100
                                progressBar2.progress = per
                                //println("re : "+re)
                                //println("en : "+en)
                                println("per : "+per)
                            }
                        }
                        points.setText(res.toString())
                    }
                }
                //pDialog.dismiss()
            }

            override fun onFailure(call: Call<LoyaltyPoints>, t: Throwable) {
                Log.e("$tag Fail response", t.toString())
                if (t.toString().contains("time")) {
                    Toast.makeText(
                        activity,
                        "Poor network connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
                //pDialog.dismiss()
            }
        })
    }
}
