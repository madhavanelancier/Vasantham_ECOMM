package com.elanciers.vasantham_stores_ecomm.Common

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.*
import com.elanciers.vasantham_stores_ecomm.R
import kotlinx.coroutines.delay
import java.util.*
import kotlin.time.Duration


class CustomLoadingDialog(act: Activity) : Dialog(act) {
    val activity = act
    val dialog = Dialog(act)
    var gethandler = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        this.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        );
        setContentView(R.layout.loading_layout)
        //CustomLoadiingDialog()
    }
    fun CustomLoadiingDialog(){
        val inf = LayoutInflater.from(activity).inflate(R.layout.loading_layout, null)
        dialog.setContentView(inf);
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        );
    }

    fun setHandler(boolean: Boolean){
        gethandler = boolean
    }

    override fun setCancelable(boolean: Boolean){
        super.setCancelable(boolean)
    }

    override fun show(){
        super.show()
    }

    override fun dismiss(){
        super.dismiss()
        /*if (gethandler) {
            Handler().postDelayed(Runnable { super.dismiss() }, 1000)
        }else{
            super.dismiss()
        }*/
    }


}
/*
class CustomDialogClass
    (var c: Activity) : Dialog(c), View.OnClickListener {
    var d: Dialog? = null
    var yes: Button? = null
    var no: Button? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_dialog)
        yes = findViewById<View>(R.id.btn_yes) as Button
        no = findViewById<View>(R.id.btn_no) as Button
        yes.setOnClickListener(this)
        no.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.getId()) {
            R.id.btn_yes -> c.finish()
            R.id.btn_no -> dismiss()
            else -> {
            }
        }
        dismiss()
    }
}*/
