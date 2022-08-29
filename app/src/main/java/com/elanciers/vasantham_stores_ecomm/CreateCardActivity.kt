package com.elanciers.vasantham_stores_ecomm

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.ActionBar
import com.elanciers.vasantham_stores_ecomm.Adapters.AreaSpinnerAdapter
import com.elanciers.vasantham_stores_ecomm.Adapters.ChitGroupSpinnerAdapter
import com.elanciers.vasantham_stores_ecomm.Adapters.FundSpinnerAdapter
import com.elanciers.vasantham_stores_ecomm.Adapters.YearSpinnerAdapter
import com.elanciers.vasantham_stores_ecomm.Common.AppUtil
import com.elanciers.vasantham_stores_ecomm.Common.CustomLoadingDialog
import com.elanciers.vasantham_stores_ecomm.Common.Utils
import com.elanciers.vasantham_stores_ecomm.DataClass.*
import com.elanciers.vasantham_stores_ecomm.retrofit.RetrofitClient2
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_create_card.*
import kotlinx.android.synthetic.main.activity_create_card.area
import kotlinx.android.synthetic.main.activity_create_card.card_number
import kotlinx.android.synthetic.main.activity_create_card.imageView5
import kotlinx.android.synthetic.main.activity_create_card.mob
import kotlinx.android.synthetic.main.activity_create_card.select_area
import kotlinx.android.synthetic.main.activity_create_card.submit
import kotlinx.android.synthetic.main.activity_create_card.textView9
import kotlinx.android.synthetic.main.activity_door_deivery.*
import kotlinx.android.synthetic.main.activity_doordelivery_list.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateCardActivity : AppCompatActivity(), PaymentResultListener {
    var tag = "card"
    lateinit var activity : Activity
    lateinit var pDialog: CustomLoadingDialog
    var years = ArrayList<YearsResponse>()
    var chitgroup = ChitGroupResponse()
    var Funds = FundResponse()
    var Areas = ArrayList<AreaResponse>()
    var areaarrname = ArrayList<String>()
    var selectedYear = YearsResponse()
    var selectedChit = Chit()
    var selectedFund1 = Fund1()
    var selectedFund2 = Fund1()
    var selectedArea = AreaResponse()
    var who = "11"
    var amountint = 0

    var tid=""
    var Paymentmode = "Razorpay"
    var success = "success"
    var failed = "failed"
    lateinit var utils: Utils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_card)
        activity = this
        utils = Utils(this)
        pDialog = CustomLoadingDialog(this)
        pDialog.setHandler(false)
        pDialog.setCancelable(false)
        lang()
        mob.setText(utils.mobile())
        imageView5.setOnClickListener {
            finish()
        }

        select_year.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedYear = years[p2]
                select_year.setText(years[p2].from+" - "+years[p2].to)
                select_year.setAdapter(YearSpinnerAdapter(activity,years))
                select_year.error=null
                getChitGroup()
            }
        }
        select_chitGroup.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedChit = chitgroup.chit[p2]
                select_chitGroup.setText(chitgroup.chit[p2].chiteName)
                amount.setText(chitgroup.chit[p2].amount)
                select_chitGroup.setAdapter(ChitGroupSpinnerAdapter(activity,chitgroup.chit))
                select_chitGroup.error=null
                if(chitgroup.chit[p2].fund.toString()=="1"){
                    getFunds()
                }else{
                    Funds.fund1 = arrayListOf()
                    Funds.fund2 = arrayListOf()
                    select_fund1.setText("")
                    select_fund2.setText("")
                    select_fund1.setAdapter(FundSpinnerAdapter(activity,Funds.fund1))
                    select_fund2.setAdapter(FundSpinnerAdapter(activity,Funds.fund2))
                }
            }
        }
        select_fund1.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedFund1 = Funds.fund1[p2]
                select_fund1.setText(Funds.fund1[p2].fundName)
                select_fund1.setAdapter(FundSpinnerAdapter(activity,Funds.fund1))
                select_fund1.error=null
            }
        }
        select_fund2.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedFund2 = Funds.fund2[p2]
                select_fund2.setText(Funds.fund2[p2].fundName)
                select_fund2.setAdapter(FundSpinnerAdapter(activity,Funds.fund2))
                select_fund2.error=null
            }
        }
        select_area.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedArea = Areas[p2]
                /*select_area.setText(Areas[p2].areaname)
                select_area.setAdapter(AreaSpinnerAdapter(activity,Areas))*/
                select_area.error=null
            }
        }

        submit.setOnClickListener {
            validatename(name)
            /*validatename(adrs)*/
            validatePhoneNo(mob)
            if ( validatename(name) /*&& validatename(adrs)*/ && validatePhoneNo(mob) &&
                !selectedYear.id.isNullOrEmpty()&&
                !selectedChit.id.isNullOrEmpty()&& findarea())
            {
                println("Ready to Submit")
                if (Funds.fund1.isNotEmpty()){
                    if (!selectedFund1.id.isNullOrEmpty()&&
                        !selectedFund2.id.isNullOrEmpty()){
                        getCardCheck()
                    }else{
                        if (selectedFund1.id.isNullOrEmpty()){
                            select_fund1.setError("Required Field")
                        }
                        if (selectedFund2.id.isNullOrEmpty()){
                            select_fund2.setError("Required Field")
                        }
                    }
                }else{
                    getCardCheck()
                }

            }else{
                println("Error")
                if (selectedYear.id.isNullOrEmpty()){
                    select_year.setError("Required Field")
                }
                if (selectedChit.id.isNullOrEmpty()){
                    select_chitGroup.setError("Required Field")
                }
                if(select_area.text.isEmpty()/*city.selectedItemPosition==0*/||!findarea()){
                    val errorText = select_area//.getSelectedView() as TextView
                    errorText.error = "Select Area"
                }
            }
        }

    }

    fun startPayment() { /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        val activity: Activity = this
        val co = Checkout()
        co.setKeyID(Utils(this).razorpay_key)
        try {
            val options = JSONObject()
            options.put("name", "Vasantham Hyper")
            options.put("description", mob.text.toString())
            //You can omit the image option to fetch the image from dashboard
            options.put("image", R.mipmap.ic_logo)
            options.put("currency", "INR")
            amountint = Integer.valueOf(amount.text.toString())
            options.put("amount", amountint * 100)
            val preFill = JSONObject()
            preFill.put("email", "")
            preFill.put("contact", mob.text.toString())
            options.put("prefill", preFill)
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    override fun onPaymentSuccess(razorpayPaymentID: String) {
        try {
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show()
            Log.i(
                "Payment Sucess",
                "Payment Successful: $razorpayPaymentID"
            )
            tid=razorpayPaymentID

            getCardCreate()

        } catch (e: Exception) {
            Log.e("tag", "Exception in onPaymentSuccess", e)
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    override fun onPaymentError(code: Int, response: String) {
        try { // Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
            Log.i("Payment Error", "Payment failed: $code $response")

            Toast.makeText(applicationContext,"Payment Failed. Try Again Later",Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            Log.e("tag", "Exception in onPaymentError", e)
        }
    }
    fun findarea() : Boolean{
        areaarrname.forEach { d->if (select_area.text.toString().trim()==d) return true }
        return false
    }

    override fun onResume() {
        super.onResume()
        getBranch()
        getyear()
        getAreas()
    }

    fun getyear(){
        pDialog.show()
        val obj = JsonObject()
        obj.addProperty("type", "yeardrop")
        Log.d(tag, obj.toString())
        val call = RetrofitClient2.Get.getYears(obj)
        call.enqueue(object : Callback<YearsData> {
            override fun onResponse(call: Call<YearsData>, response: Response<YearsData>) {
                Log.e("$tag response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as YearsData
                    if (example.Status == "Success") {
                        years = example.Response
                        /*val data = Gson().toJson(example, YearsData::class.java).toString()
                        println("data : "+data)*/
                        select_year.setAdapter(YearSpinnerAdapter(activity,years))
                    }
                }
                pDialog.dismiss()
            }

            override fun onFailure(call: Call<YearsData>, t: Throwable) {
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

    fun showthankspopup() { //open = new Dialog(LoginActivity.this,R.style.MyCustomTheme);
        val open = Dialog(this)
        open.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val popup = layoutInflater.inflate(R.layout.card_success_popup1, null)
        val ok = popup.findViewById<View>(R.id.yes) as TextView
        //open.getWindow().getAttributes().windowAnimations = R.style.MyCustomTheme;
        open.setContentView(popup)
        open.window!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
        open.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        val lparam = WindowManager.LayoutParams()
        lparam.copyFrom(open.window!!.attributes)
        open.setCancelable(false)
        open.window!!.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        open.show()
        ok.setOnClickListener {
            // TODO Auto-generated method stub
            open.dismiss()
            finish()
            /*Intent intent = new Intent(PaymentActivity.this, JewelHome.class);
                    startActivity(intent);
                    finish();*/
        }
    }

    fun getChitGroup(){
        pDialog.show()
        val obj = JsonObject()
        obj.addProperty("type", "onlineselectyear")
        obj.addProperty("cardyear", selectedYear.id.toString())
        Log.d(tag, obj.toString())
        val call = RetrofitClient2.Get.getChitGroup(obj)
        call.enqueue(object : Callback<ChitGroupData> {
            override fun onResponse(call: Call<ChitGroupData>, response: Response<ChitGroupData>) {
                Log.e("$tag response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as ChitGroupData
                    if (example.Status == "Success") {
                        chitgroup = example.Response!!
                        /*val data = Gson().toJson(example, ChitGroupData::class.java).toString()
                        println("data : "+data)*/
                        select_chitGroup.setAdapter(ChitGroupSpinnerAdapter(activity,chitgroup.chit))
                        number.setText(chitgroup.regno.toString())
                        card_number.setText(chitgroup.cardNo.toString())
                    }
                }
                pDialog.dismiss()
            }

            override fun onFailure(call: Call<ChitGroupData>, t: Throwable) {
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

    fun getFunds(){
        pDialog.show()
        val obj = JsonObject()
        obj.addProperty("type", "funds")
        Log.d(tag, obj.toString())
        val call = RetrofitClient2.Get.getFunds(obj)
        call.enqueue(object : Callback<FundsData> {
            override fun onResponse(call: Call<FundsData>, response: Response<FundsData>) {
                Log.e("$tag response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as FundsData
                    if (example.Status == "Success") {
                        Funds = example.Response!!
                       /* val data = Gson().toJson(example, FundsData::class.java).toString()
                        println("data : "+data)*/
                        select_fund1.setAdapter(FundSpinnerAdapter(activity,Funds.fund1))
                        select_fund2.setAdapter(FundSpinnerAdapter(activity,Funds.fund2))
                    }
                }
                pDialog.dismiss()
            }

            override fun onFailure(call: Call<FundsData>, t: Throwable) {
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

    fun getAreas(){
        pDialog.show()
        val obj = JsonObject()
        obj.addProperty("type", "areas")
        Log.d(tag, obj.toString())
        val call = RetrofitClient2.Get.getAreas(obj)
        call.enqueue(object : Callback<AreaData> {
            override fun onResponse(call: Call<AreaData>, response: Response<AreaData>) {
                Log.e("$tag response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as AreaData
                    if (example.Status == "Success") {
                        Areas = example.Response!!
                        /*val data = Gson().toJson(example, AreaData::class.java).toString()
                        println("data : "+data)*/
                        //select_area.setAdapter(AreaSpinnerAdapter(activity,Areas))
                        Areas.forEach { d->areaarrname.add(d.areaname!!) }
                        select_area.setAdapter(ArrayAdapter(this@CreateCardActivity,R.layout.spinner_item1,areaarrname))
                    }
                }
                pDialog.dismiss()
            }

            override fun onFailure(call: Call<AreaData>, t: Throwable) {
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

    fun getBranch(){
        pDialog.show()
        val call = RetrofitClient2.Get.getBranchs()
        call.enqueue(object : Callback<BranchData> {
            override fun onResponse(call: Call<BranchData>, response: Response<BranchData>) {
                Log.e("$tag response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as BranchData
                    if (example.Status == "Success") {
                        val res = example.Response
                        /*val data = Gson().toJson(example, BranchData::class.java).toString()
                        println("data : "+data)*/
                        res.forEach {
                            if (it.branch=="Online Cust"){
                                who = it.id.toString()
                            }
                        }
                    }
                }
                pDialog.dismiss()
            }

            override fun onFailure(call: Call<BranchData>, t: Throwable) {
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

    fun getCardCheck(){
        pDialog.show()
        val obj = JsonObject()
        obj.addProperty("type", "onlineCard")
        obj.addProperty("cardyear", selectedYear.id.toString())
        obj.addProperty("card_no", chitgroup.cardNo.toString())
        Log.d(tag, obj.toString())
        val call = RetrofitClient2.Get.CheckCard(obj)
        call.enqueue(object : Callback<CheckCardData> {
            override fun onResponse(call: Call<CheckCardData>, response: Response<CheckCardData>) {
                Log.e("$tag response", response.toString())
                if (response.isSuccessful()) {
                    val example = response.body() as CheckCardData
                    if (example.Status == "Success") {
                        val res = example.Response!!
                        val data = Gson().toJson(example, CheckCardData::class.java).toString()
                        println("data : "+data)
                        chitgroup.cardNo = res.cardNo
                        startPayment()
                    }
                }
                pDialog.dismiss()
            }

            override fun onFailure(call:Call<CheckCardData>, t:Throwable) {
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

    fun getCardCreate(){
        pDialog.show()
        /*{
    "reg_no":"1025",
    "cid":"4",
    "card_year":"2",
    "name":"Test1",
    "card_no":"20221240",
    "phone":"9786039322",
    "area":"Adalai",
    "who":"7",
    "fund1":"",
    "fund2":"",
    "gid":"",
    "collect_id":"1"
}*/
        val obj = JsonObject()
        obj.addProperty("reg_no", chitgroup.regno.toString())
        obj.addProperty("cid", selectedChit.id.toString())
        obj.addProperty("card_year", selectedYear.id.toString())
        obj.addProperty("name", name.text.toString())
        obj.addProperty("card_no", chitgroup.cardNo.toString())
        obj.addProperty("phone", mob.text.toString())
        obj.addProperty("area", selectedArea.areaname.toString())
        obj.addProperty("address", adrs.text.toString())
        obj.addProperty("who", who)
        obj.addProperty("tid", tid)
        obj.addProperty("amount", amount.text.toString())
        obj.addProperty("fund1", if (Funds.fund1.isNullOrEmpty())"" else selectedFund1.id)
        obj.addProperty("fund2", if (Funds.fund2.isNullOrEmpty())"" else selectedFund2.id)
        obj.addProperty("gid", "")
        obj.addProperty("collect_id", "")
        Log.d(tag, obj.toString())
        val call = RetrofitClient2.Get.CreatCard(obj)
        call.enqueue(object : Callback<CreateCardData> {
            override fun onResponse(call: Call<CreateCardData>, response: Response<CreateCardData>) {
                Log.e("$tag response", response.toString())
                pDialog.dismiss()
                if (response.isSuccessful()) {
                    val example = response.body() as CreateCardData
                    Toast.makeText(activity, example.message, Toast.LENGTH_SHORT).show()
                    if (example.Status == "Success") {
                        val data = Gson().toJson(example, CreateCardData::class.java).toString()
                        println("data : "+data)
                        finish()
                    }
                    else{
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<CreateCardData>, t: Throwable) {
                Log.e("$tag Fail response", t.toString())
                finish()
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

    private fun validatePhoneNo(regPhoneNo: EditText): Boolean {
        val `val`: String = regPhoneNo.text.toString()
        try {
            //`val`.toBigInteger()
            return if (`val`.isEmpty()) {
                regPhoneNo.setError("Field cannot be empty")
                false
            } else if (`val`.length != 10) {
                regPhoneNo.setError("Enter Valid Mobile Number")
                false
            } else {
                regPhoneNo.setError(null)
                true
            }
        } catch (e: NumberFormatException) {
            return false
        }
    }

    fun validatename(regEmail: EditText, showError: Boolean = true): Boolean {
        val `val`: String = regEmail.text.toString().trim()
        return if (`val`.isEmpty()) {
            if (showError) regEmail.setError("Field cannot be empty")
            false
        } else {
            if (showError) regEmail.setError(null)
            true
        }
    }
    fun lang() {
        textView9.setText(AppUtil.languageString("creat_card"))
        year.setHint(AppUtil.languageString("select_year"))
        group.setHint(AppUtil.languageString("selecte_chit_group"))
        num.setHint(AppUtil.languageString("number"))
        //number.setHint(AppUtil.languageString("number"))
        name_.setHint(AppUtil.languageString("name"))
        //name.setHint(AppUtil.languageString("name"))
        card.setHint(AppUtil.languageString("card_number"))
        //card_number.setHint(AppUtil.languageString("card_number"))
        mob_.setHint(AppUtil.languageString("mobile_number"))
        //mob.setHint(AppUtil.languageString("mobile_number"))
        adrs_.setHint(AppUtil.languageString("address"))
        //adrs.setHint(AppUtil.languageString("address"))
        group.setHint(AppUtil.languageString("select_group"))
        area.setHint(AppUtil.languageString("select_area"))
        agent.setHint(AppUtil.languageString("agent"))
        fund1.setHint(AppUtil.languageString("select_fund1"))
        fund2.setHint(AppUtil.languageString("selec_fund2"))
        submit.setText(AppUtil.languageString("submit"))
    }
}