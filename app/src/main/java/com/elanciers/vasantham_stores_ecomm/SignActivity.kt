package com.elanciers.vasantham_stores_ecomm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_sign.*

class SignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_sign)
        Glide.with(this).load(R.drawable.logo_gif).into(imageView3);
        signin.setOnClickListener {
            startActivity(Intent(this, SigninActivity::class.java))
        }

        signup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }


    }
}
