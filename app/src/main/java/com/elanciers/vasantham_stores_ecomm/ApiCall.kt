package com.elanciers.vasantham_stores_ecomm

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.*

class ApiCall {
    fun readFromFile(languageFile: File?,language: String): String {
        var ret = ""
        try {
            val inputStream = FileInputStream(languageFile)
            if (inputStream != null) {
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                var receiveString: String? = ""
                val stringBuilder = StringBuilder()
                while (bufferedReader.readLine().also { receiveString = it } != null) {
                    stringBuilder.append(receiveString)
                }
                inputStream.close()
                ret = stringBuilder.toString()
                try {
                    // AppController.languageobj = new JSONObject(ret);
                        val removed = ret.removePrefix("module.exports = ")
                        //println("language : "+language)
                        //println("obj has : "+JSONObject(removed).has(language))
                    val obj = JSONObject(ret.removePrefix("module.exports = ")).getJSONObject(language)
                    val En = JSONObject(ret.removePrefix("module.exports = ")).getJSONObject("English")
                    val Ta = JSONObject(ret.removePrefix("module.exports = ")).getJSONObject("Tamil")
                        //println("obj : "+obj)
                    AppController.setLanguageobj(obj)
                    AppController.setEnLanguageobj(En)
                    AppController.setTaLanguageobj(Ta)
                    //Log.e("onPostExecute","onPostExecute:" + AppController.languageobj);
                } catch (e: Exception) {
                    //println("language obj set error : "+e.toString())
                    e.printStackTrace()
                }
            }

            //Log.e("ret","response ret:" + ret);
        } catch (e: FileNotFoundException) {
            //Log.e("SplashScreenActivity","File not found: " + e.toString());
        } catch (e: IOException) {
            //Log.e("SplashScreenActivity","Can not read file: " + e.toString());
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ret
    }

    /*fun Downloadfile(mActivity: Context, input: String, type: Int, volleyCallback: ResponseHandler, isprogress: Boolean) {
        *//*if (isprogress) {
            Utils.setProgressBar(mActivity)
        }*//*
        var request = Request.Method.POST
        if (type == 1) {
            request = Request.Method.POST
        } else if (type == 2) {
            request = Request.Method.GET
        } else if (type == 3) {
            request = Request.Method.PUT
        } else if (type == 4) {
            request = Request.Method.DELETE
        }
        Log.d("ApiCall.TAG", "Download file Url " + input.toString())
        val downloadRequest = DownLoadFile(request, input, Response.Listener<ByteArray?> { response ->
            val languageFile: File
            var count: Int
            try {
                if (response != null) {


                    //Log.d(TAG," directory name " + mActivity.getCacheDir());
                    languageFile = File(mActivity.cacheDir, "language.txt")
                    val output: OutputStream = FileOutputStream(languageFile)
                    output.write(response)
                    val data = ByteArray(1024)
                    val total: Long = 0


                    // flushing output
                    output.flush()

                    // closing streams
                    output.close()
                    val languageString = readFromFile(languageFile)
                }
                volleyCallback.setDataResponse(null)
                //Utils.cancelProgressBar()
            } catch (e: java.lang.Exception) {
                //Utils.cancelProgressBar()
                //Log.d("KEY_ERROR","UNABLE TO DOWNLOAD FILE");
                e.printStackTrace()
                //Utils.showCyouAlert(mActivity,mActivity.getString(R.string.app_name),Utils.languageString("networkerror"));
            }
        }, Response.ErrorListener { error ->
            //Utils.cancelProgressBar()
            var errorCode: Int
            val errorMessage = ""
            try {
                val responseBody = String(error.networkResponse.data, "utf-8")
                val data = JSONObject(responseBody)
                println("error data : "+data)
                if (data.has("msg")) {
                    //Utils.showCyouAlert(mActivity, mActivity.getString(R.string.app_name), Utils.languageString(data.getString("msg")))
                } else if (data.has("message")) {
                    //Utils.showCyouAlert(mActivity, mActivity.getString(R.string.app_name), Utils.languageString(data.getString("message")))
                } else {
                    //Utils.showCyouAlert(mActivity,mActivity.getString(R.string.app_name),Utils.languageString("networkerror"));
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                //Utils.showCyouAlert(mActivity,mActivity.getString(R.string.app_name),Utils.languageString("networkerror"));
            }
        })
        val mRequestQueue = Volley.newRequestQueue(mActivity, AppController().getSecureHttpConnection(mActivity))
        mRequestQueue.add<Any>(downloadRequest)
    }*/
}