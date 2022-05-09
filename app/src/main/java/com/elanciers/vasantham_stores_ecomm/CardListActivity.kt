package com.elanciers.vasantham_stores_ecomm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_card_list.*

class CardListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_list)

        imageView5.setOnClickListener {
            finish()
        }
        create.setOnClickListener {
            val st = Intent(this,CreateCardActivity::class.java)
            startActivity(st)
        }
    }
}