package com.elanciers.vasantham_stores_ecomm;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

public class CustomComponents1 extends AppCompatActivity {

    public Dialog progbar;

    public void showProgressBar(){
        progbar = new Dialog(this);
        progbar.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progbar.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View v = getLayoutInflater().inflate(R.layout.load, null);
        progbar.setContentView(v);
        progbar.show();
    }

    public void closeProgressBar(){
        progbar.dismiss();
    }
}
