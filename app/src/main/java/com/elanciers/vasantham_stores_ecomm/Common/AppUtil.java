package com.elanciers.vasantham_stores_ecomm.Common;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import com.elanciers.vasantham_stores_ecomm.AppController;

public class AppUtil {
    public static Spanned languageString(String string) {
        String retunstr="";
        Log.d("Languagetesting"," "+string);
        try {
            if (AppController.getLanguageobj() == null) {
                Log.d("Languagetesting"," Language obje null "+string);
                retunstr=getStringResourceByName(string);
            } else {
                retunstr=AppController.getLanguageobj().has(string) ? AppController.getLanguageobj().getString(string) : getStringResourceByName(string);
                Log.d("Languagetesting"," Language obje NOt nOt null has "+ AppController.getLanguageobj().has(string));
                Log.d("Languagetesting"," Language obje NOt nOt null "+ AppController.getLanguageobj().getString(string));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Html.fromHtml(retunstr);//.toString();
    }

    public static String languageStringSplit(String string) {
        String retunstr="";
        Log.d("Languagetesting"," "+string);
        try {
            if (AppController.getLanguageobj() == null) {
                Log.d("Languagetesting"," Language obje null "+string);
                retunstr=getStringResourceByName(string);
            } else {
                retunstr=AppController.getLanguageobj().has(string) ? AppController.getLanguageobj().getString(string) : getStringResourceByName(string);
                Log.d("Languagetesting"," Language obje NOt nOt null has "+ AppController.getLanguageobj().has(string));
                Log.d("Languagetesting"," Language obje NOt nOt null "+ AppController.getLanguageobj().getString(string));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retunstr.replace(" ","\n");
    }


    private static String getStringResourceByName(String aString) {
        String returnstring="";
        try {
            String packageName = AppController.getContext().getPackageName();
            int resId = AppController.getContext().getResources().getIdentifier(aString, "string", packageName);
            returnstring= AppController.getContext().getString(resId);
        }catch ( Exception e)
        {
            e.printStackTrace();
        }
        return returnstring;
    }
    /*public static String languageString(String string) {
        var retunstr = ""
        Log.d("Languagetesting", " $string")
        try {
            if (AppController.getLanguageobj() == null) {
                Log.d("Languagetesting", " Language obje null $string")
                retunstr = getStringResourceByName(string)
            } else {
                retunstr = if (AppController.getLanguageobj().has(string)) AppController.getLanguageobj().getString(string) else getStringResourceByName(string)
                Log.d("Languagetesting", " Language obje NOt nOt null has " + AppController.getLanguageobj().has(string))
                Log.d("Languagetesting", " Language obje NOt nOt null " + AppController.getLanguageobj().getString(string))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return retunstr
    }

    private String getStringResourceByName(String aString) {
        var returnstring = ""
        try {
            val packageName = AppController.getContext().packageName
            val resId = AppController.getContext().resources.getIdentifier(aString, "string", packageName)
            returnstring = AppController.getContext().getString(resId)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return returnstring
    }*/
}